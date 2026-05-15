package org.jeecg.modules.webgame.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.social.entity.WgDungeonRoom;
import org.jeecg.modules.webgame.social.entity.WgDungeonRoomMember;
import org.jeecg.modules.webgame.social.entity.WgTeam;
import org.jeecg.modules.webgame.social.entity.WgTeamMember;
import org.jeecg.modules.webgame.social.mapper.WgDungeonRoomMapper;
import org.jeecg.modules.webgame.social.mapper.WgDungeonRoomMemberMapper;
import org.jeecg.modules.webgame.social.mapper.WgTeamMapper;
import org.jeecg.modules.webgame.social.mapper.WgTeamMemberMapper;
import org.jeecg.modules.webgame.social.service.IDungeonRoomService;
import org.jeecg.modules.webgame.social.vo.TeamInfoVO;
import org.jeecg.modules.webgame.social.vo.TeamMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 多人副本大厅 Service 实现
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@Service
public class DungeonRoomServiceImpl implements IDungeonRoomService {
    
    @Autowired
    private WgDungeonRoomMapper dungeonRoomMapper;
    
    @Autowired
    private WgDungeonRoomMemberMapper roomMemberMapper;
    
    @Autowired
    private WgTeamMapper teamMapper;
    
    @Autowired
    private WgTeamMemberMapper teamMemberMapper;
    
    @Autowired
    private WgCharacterMapper characterMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamInfoVO createDungeonRoom(String leaderId, String teamId, String dungeonId) {
        log.info("创建副本房间, leaderId: {}, teamId: {}, dungeonId: {}", leaderId, teamId, dungeonId);
        
        // 1. 校验队长权限
        WgTeam team = teamMapper.selectById(teamId);
        if (team == null || !team.getLeaderId().equals(leaderId)) {
            throw new JeecgBootException("不是队长或队伍不存在");
        }
        
        // 2. 检查是否已有房间
        LambdaQueryWrapper<WgDungeonRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgDungeonRoom::getTeamId, teamId);
        if (dungeonRoomMapper.selectCount(wrapper) > 0) {
            throw new JeecgBootException("队伍已在副本房间中");
        }
        
        // 3. 创建房间
        WgDungeonRoom room = new WgDungeonRoom();
        room.setId(UUID.randomUUID().toString());
        room.setTeamId(teamId);
        room.setDungeonId(dungeonId);
        room.setLeaderId(leaderId);
        room.setStatus("waiting");
        room.setMonsterScale(BigDecimal.valueOf(1.0));
        room.setCreatedAt(new Date());
        room.setUpdatedAt(new Date());
        
        dungeonRoomMapper.insert(room);
        
        // 4. 添加队伍成员到房间
        addTeamMembersToRoom(room.getId(), teamId);
        
        // 5. 返回房间信息
        return getDungeonRoomInfo(room);
    }
    
    @Override
    public TeamInfoVO getDungeonRoom(String roomId) {
        log.info("获取房间信息, roomId: {}", roomId);
        
        WgDungeonRoom room = dungeonRoomMapper.selectById(roomId);
        if (room == null) {
            throw new JeecgBootException("房间不存在");
        }
        
        return getDungeonRoomInfo(room);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamInfoVO toggleReady(String characterId, String roomId) {
        log.info("切换准备状态, characterId: {}, roomId: {}", characterId, roomId);
        
        // 1. 校验成员权限
        WgDungeonRoom room = dungeonRoomMapper.selectById(roomId);
        if (room == null) {
            throw new JeecgBootException("房间不存在");
        }
        
        // 2. 检查是否为队伍成员
        LambdaQueryWrapper<WgDungeonRoomMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgDungeonRoomMember::getRoomId, roomId)
               .eq(WgDungeonRoomMember::getCharacterId, characterId);
        WgDungeonRoomMember member = roomMemberMapper.selectOne(wrapper);
        if (member == null) {
            throw new JeecgBootException("不是房间成员");
        }
        
        // 3. 切换准备状态
        member.setReadyStatus("ready".equals(member.getReadyStatus()) ? "not_ready" : "ready");
        member.setJoinedAt(new Date());
        roomMemberMapper.updateById(member);
        
        // 4. 检查是否全员准备，如果是则更新房间状态
        checkAllReady(room);
        
        // 5. 返回更新后的房间信息
        return getDungeonRoomInfo(room);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object startChallenge(String leaderId, String roomId) {
        log.info("开始挑战, leaderId: {}, roomId: {}", leaderId, roomId);
        
        // 1. 校验队长权限
        WgDungeonRoom room = dungeonRoomMapper.selectById(roomId);
        if (room == null || !room.getLeaderId().equals(leaderId)) {
            throw new JeecgBootException("不是队长或房间不存在");
        }
        
        if (!"ready".equals(room.getStatus())) {
            throw new JeecgBootException("房间状态不正确，无法开始挑战");
        }
        
        // 2. 更新房间状态
        room.setStatus("starting");
        room.setUpdatedAt(new Date());
        dungeonRoomMapper.updateById(room);
        
        // 3. 计算怪物缩放倍率
        BigDecimal scale = calculateMonsterScale(roomId);
        room.setMonsterScale(scale);
        dungeonRoomMapper.updateById(room);
        
        // 4. 返回战斗初始化数据
        return buildBattleInitializationData(room);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leaveRoom(String characterId, String roomId) {
        log.info("离开房间, characterId: {}, roomId: {}", characterId, roomId);
        
        // 1. 校验成员权限
        WgDungeonRoom room = dungeonRoomMapper.selectById(roomId);
        if (room == null) {
            throw new JeecgBootException("房间不存在");
        }
        
        // 2. 检查是否为队长
        if (room.getLeaderId().equals(characterId)) {
            // 队长离开，解散房间
            cancelRoom(characterId, roomId);
        } else {
            // 普通成员离开
            LambdaQueryWrapper<WgDungeonRoomMember> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WgDungeonRoomMember::getRoomId, roomId)
                   .eq(WgDungeonRoomMember::getCharacterId, characterId);
            roomMemberMapper.delete(wrapper);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRoom(String leaderId, String roomId) {
        log.info("取消房间, leaderId: {}, roomId: {}", leaderId, roomId);
        
        // 1. 校验队长权限
        WgDungeonRoom room = dungeonRoomMapper.selectById(roomId);
        if (room == null || !room.getLeaderId().equals(leaderId)) {
            throw new JeecgBootException("不是队长或房间不存在");
        }
        
        // 2. 删除房间和成员
        dungeonRoomMapper.deleteById(roomId);
        
        LambdaQueryWrapper<WgDungeonRoomMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgDungeonRoomMember::getRoomId, roomId);
        roomMemberMapper.delete(wrapper);
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 添加队伍成员到房间
     */
    private void addTeamMembersToRoom(String roomId, String teamId) {
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getTeamId, teamId);
        
        List<WgTeamMember> teamMembers = teamMemberMapper.selectList(wrapper);
        for (WgTeamMember teamMember : teamMembers) {
            WgDungeonRoomMember roomMember = new WgDungeonRoomMember();
            roomMember.setId(UUID.randomUUID().toString());
            roomMember.setRoomId(roomId);
            roomMember.setCharacterId(teamMember.getCharacterId());
            roomMember.setReadyStatus("leader".equals(teamMember.getRole()) ? "ready" : "not_ready");
            roomMember.setJoinedAt(new Date());
            
            roomMemberMapper.insert(roomMember);
        }
    }
    
    /**
     * 检查是否全员准备
     */
    private void checkAllReady(WgDungeonRoom room) {
        LambdaQueryWrapper<WgDungeonRoomMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgDungeonRoomMember::getRoomId, room.getId())
               .eq(WgDungeonRoomMember::getReadyStatus, "not_ready");
        
        long notReadyCount = roomMemberMapper.selectCount(wrapper);
        if (notReadyCount == 0) {
            // 全员准备，更新房间状态
            room.setStatus("ready");
            room.setUpdatedAt(new Date());
            dungeonRoomMapper.updateById(room);
        } else {
            // 有人未准备，如果当前状态是ready则改回waiting
            if ("ready".equals(room.getStatus())) {
                room.setStatus("waiting");
                room.setUpdatedAt(new Date());
                dungeonRoomMapper.updateById(room);
            }
        }
    }
    
    /**
     * 计算怪物缩放倍率
     */
    private BigDecimal calculateMonsterScale(String roomId) {
        LambdaQueryWrapper<WgDungeonRoomMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgDungeonRoomMember::getRoomId, roomId);
        
        long memberCount = roomMemberMapper.selectCount(wrapper);
        // 根据人数计算缩放倍率: 1 + 0.5 * (人数-1)
        double scale = 1 + 0.5 * (memberCount - 1);
        return BigDecimal.valueOf(scale);
    }
    
    /**
     * 构建房间信息VO
     */
    private TeamInfoVO getDungeonRoomInfo(WgDungeonRoom room) {
        TeamInfoVO vo = new TeamInfoVO();
        vo.setId(room.getId());
        vo.setLeaderId(room.getLeaderId());
        vo.setStatus(room.getStatus());
        vo.setTargetDungeon(room.getDungeonId());
        
        // 查询队长名称
        WgCharacter leader = characterMapper.selectById(room.getLeaderId());
        if (leader != null) {
            vo.setLeaderName(leader.getCharacterName());
        }
        
        // 查询房间成员
        LambdaQueryWrapper<WgDungeonRoomMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgDungeonRoomMember::getRoomId, room.getId());
        
        List<WgDungeonRoomMember> members = roomMemberMapper.selectList(wrapper);
        List<TeamMemberVO> memberVOs = new ArrayList<>();
        
        for (WgDungeonRoomMember member : members) {
            WgCharacter chara = characterMapper.selectById(member.getCharacterId());
            if (chara != null) {
                TeamMemberVO memberVO = new TeamMemberVO();
                memberVO.setCharacterId(chara.getId());
                memberVO.setCharacterName(chara.getCharacterName());
                memberVO.setProfession(getProfessionName(chara.getProfession()));
                memberVO.setLevel(chara.getLevel());
                // 简化处理，添加额外字段
                memberVO.setRole("member");
                memberVOs.add(memberVO);
            }
        }
        
        vo.setMembers(memberVOs);
        
        return vo;
    }
    
    /**
     * 构建战斗初始化数据
     */
    private Object buildBattleInitializationData(WgDungeonRoom room) {
        Map<String, Object> data = new HashMap<>();
        data.put("roomId", room.getId());
        data.put("dungeonId", room.getDungeonId());
        data.put("monsterScale", room.getMonsterScale());
        data.put("status", "in_progress");
        
        // 这里应该包含更多战斗初始化数据，如怪物配置、队伍成员等
        // 简化实现，返回基本数据
        return data;
    }
    
    /**
     * 获取职业名称
     */
    private String getProfessionName(Integer profession) {
        if (profession == null) return "Unknown";
        switch (profession) {
            case 1: return "Warrior";
            case 2: return "Mage";
            case 3: return "Hunter";
            default: return "Unknown";
        }
    }
}
