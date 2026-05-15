package org.jeecg.modules.webgame.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.social.dto.*;
import org.jeecg.modules.webgame.social.entity.WgTeam;
import org.jeecg.modules.webgame.social.entity.WgTeamApplication;
import org.jeecg.modules.webgame.social.entity.WgTeamMember;
import org.jeecg.modules.webgame.social.mapper.WgTeamApplicationMapper;
import org.jeecg.modules.webgame.social.mapper.WgTeamMapper;
import org.jeecg.modules.webgame.social.mapper.WgTeamMemberMapper;
import org.jeecg.modules.webgame.social.service.ITeamService;
import org.jeecg.modules.webgame.social.vo.TeamApplicationVO;
import org.jeecg.modules.webgame.social.vo.TeamInfoVO;
import org.jeecg.modules.webgame.social.vo.TeamMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 组队系统 Service 实现（精简版）
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@Service
public class TeamServiceImpl implements ITeamService {
    
    @Autowired
    private WgTeamMapper teamMapper;
    
    @Autowired
    private WgTeamMemberMapper teamMemberMapper;
    
    @Autowired
    private WgTeamApplicationMapper applicationMapper;
    
    @Autowired
    private WgCharacterMapper characterMapper;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final int MAX_TEAM_MEMBERS = 4;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamInfoVO createTeam(String characterId) {
        log.info("创建队伍, characterId: {}", characterId);
        
        // 1. 检查是否已在队伍中
        if (isInTeam(characterId)) {
            throw new JeecgBootException("已在队伍中，不能重复创建");
        }
        
        // 2. 创建队伍
        WgTeam team = new WgTeam();
        team.setId(UUID.randomUUID().toString());
        team.setLeaderId(characterId);
        team.setMaxMembers(MAX_TEAM_MEMBERS);
        team.setStatus("open");
        team.setCreatedAt(new Date());
        team.setUpdatedAt(new Date());
        
        teamMapper.insert(team);
        
        // 3. 添加队长为成员
        WgTeamMember member = new WgTeamMember();
        member.setId(UUID.randomUUID().toString());
        member.setTeamId(team.getId());
        member.setCharacterId(characterId);
        member.setRole("leader");
        member.setJoinedAt(new Date());
        
        teamMemberMapper.insert(member);
        
        // 4. 返回队伍信息
        return buildTeamInfoVO(team);
    }
    
    @Override
    public List<TeamInfoVO> getPublicTeams() {
        log.info("获取公开队伍列表");
        
        LambdaQueryWrapper<WgTeam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeam::getStatus, "open")
               .orderByDesc(WgTeam::getCreatedAt);
        
        List<WgTeam> teams = teamMapper.selectList(wrapper);
        return teams.stream()
            .map(this::buildTeamInfoVO)
            .collect(Collectors.toList());
    }
    
    @Override
    public TeamInfoVO getMyTeam(String characterId) {
        log.info("获取我的队伍, characterId: {}", characterId);
        
        // 查询成员记录
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getCharacterId, characterId);
        
        WgTeamMember member = teamMemberMapper.selectOne(wrapper);
        if (member == null) {
            return null;
        }
        
        // 查询队伍信息
        WgTeam team = teamMapper.selectById(member.getTeamId());
        if (team == null) {
            return null;
        }
        
        return buildTeamInfoVO(team);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamMemberVO inviteMember(String leaderId, InviteDTO dto) {
        log.info("邀请好友, leaderId: {}, targetId: {}", leaderId, dto.getCharacterId());
        
        // TODO: 实现邀请逻辑（需要WebSocket支持）
        // 这里先返回占位实现
        
        WgCharacter targetChara = characterMapper.selectById(dto.getCharacterId());
        if (targetChara == null) {
            throw new JeecgBootException("目标角色不存在");
        }
        
        TeamMemberVO vo = new TeamMemberVO();
        vo.setCharacterId(targetChara.getId());
        vo.setCharacterName(targetChara.getCharacterName());
        vo.setProfession(getProfessionName(targetChara.getProfession()));
        vo.setLevel(targetChara.getLevel());
        vo.setRole("member");
        vo.setStatus("online");
        vo.setJoinedAt(sdf.format(new Date()));
        
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamApplicationVO applyTeam(String applicantId, ApplyTeamDTO dto) {
        log.info("申请加入队伍, applicantId: {}, teamId: {}", applicantId, dto.getTeamId());
        
        // 1. 检查是否已在队伍中
        if (isInTeam(applicantId)) {
            throw new JeecgBootException("已在队伍中");
        }
        
        // 2. 检查队伍是否存在且开放
        WgTeam team = teamMapper.selectById(dto.getTeamId());
        if (team == null) {
            throw new JeecgBootException("队伍不存在");
        }
        if (!"open".equals(team.getStatus())) {
            throw new JeecgBootException("队伍已关闭");
        }
        
        // 3. 检查是否已申请过
        LambdaQueryWrapper<WgTeamApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamApplication::getTeamId, dto.getTeamId())
               .eq(WgTeamApplication::getApplicantId, applicantId)
               .eq(WgTeamApplication::getStatus, "pending");
        
        if (applicationMapper.selectCount(wrapper) > 0) {
            throw new JeecgBootException("已申请过该队伍");
        }
        
        // 4. 创建申请
        WgTeamApplication application = new WgTeamApplication();
        application.setId(UUID.randomUUID().toString());
        application.setTeamId(dto.getTeamId());
        application.setApplicantId(applicantId);
        application.setStatus("pending");
        application.setCreatedAt(new Date());
        application.setUpdatedAt(new Date());
        
        applicationMapper.insert(application);
        
        // 5. 返回申请VO
        return buildApplicationVO(application);
    }
    
    @Override
    public List<TeamApplicationVO> getApplications(String teamId, String leaderId) {
        log.info("获取入队申请列表, teamId: {}, leaderId: {}", teamId, leaderId);
        
        // 1. 校验是否为队长
        WgTeam team = teamMapper.selectById(teamId);
        if (team == null || !team.getLeaderId().equals(leaderId)) {
            throw new JeecgBootException("不是队长，无权查看");
        }
        
        // 2. 查询待处理申请
        LambdaQueryWrapper<WgTeamApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamApplication::getTeamId, teamId)
               .eq(WgTeamApplication::getStatus, "pending")
               .orderByDesc(WgTeamApplication::getCreatedAt);
        
        List<WgTeamApplication> applications = applicationMapper.selectList(wrapper);
        return applications.stream()
            .map(this::buildApplicationVO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamMemberVO acceptApplication(String leaderId, HandleApplicationDTO dto) {
        log.info("接受申请, leaderId: {}, applicationId: {}", leaderId, dto.getId());
        
        // 1. 查询申请
        WgTeamApplication application = applicationMapper.selectById(dto.getId());
        if (application == null) {
            throw new JeecgBootException("申请不存在");
        }
        
        // 2. 校验队长权限
        WgTeam team = teamMapper.selectById(application.getTeamId());
        if (team == null || !team.getLeaderId().equals(leaderId)) {
            throw new JeecgBootException("不是队长");
        }
        
        // 3. 检查队伍人数
        long memberCount = getTeamMemberCount(team.getId());
        if (memberCount >= team.getMaxMembers()) {
            throw new JeecgBootException("队伍已满");
        }
        
        // 4. 更新申请状态
        application.setStatus("accepted");
        application.setUpdatedAt(new Date());
        applicationMapper.updateById(application);
        
        // 5. 添加成员
        WgTeamMember member = new WgTeamMember();
        member.setId(UUID.randomUUID().toString());
        member.setTeamId(team.getId());
        member.setCharacterId(application.getApplicantId());
        member.setRole("member");
        member.setJoinedAt(new Date());
        
        teamMemberMapper.insert(member);
        
        // 6. 返回成员信息
        WgCharacter chara = characterMapper.selectById(application.getApplicantId());
        return buildMemberVO(chara, "member");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectApplication(String leaderId, HandleApplicationDTO dto) {
        log.info("拒绝申请, leaderId: {}, applicationId: {}", leaderId, dto.getId());
        
        WgTeamApplication application = applicationMapper.selectById(dto.getId());
        if (application == null) {
            throw new JeecgBootException("申请不存在");
        }
        
        WgTeam team = teamMapper.selectById(application.getTeamId());
        if (team == null || !team.getLeaderId().equals(leaderId)) {
            throw new JeecgBootException("不是队长");
        }
        
        application.setStatus("rejected");
        application.setUpdatedAt(new Date());
        applicationMapper.updateById(application);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void kickMember(String leaderId, String characterId) {
        log.info("踢出成员, leaderId: {}, targetId: {}", leaderId, characterId);
        
        // 1. 校验队长权限
        if (leaderId.equals(characterId)) {
            throw new JeecgBootException("不能踢出自己");
        }
        
        // 2. 查询队伍
        WgTeamMember leaderMember = getTeamMemberByCharacter(leaderId);
        if (leaderMember == null || !"leader".equals(leaderMember.getRole())) {
            throw new JeecgBootException("不是队长");
        }
        
        // 3. 删除成员
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getTeamId, leaderMember.getTeamId())
               .eq(WgTeamMember::getCharacterId, characterId);
        
        teamMemberMapper.delete(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leaveTeam(String characterId) {
        log.info("离开队伍, characterId: {}", characterId);
        
        WgTeamMember member = getTeamMemberByCharacter(characterId);
        if (member == null) {
            throw new JeecgBootException("未在队伍中");
        }
        
        WgTeam team = teamMapper.selectById(member.getTeamId());
        
        // 如果是队长
        if ("leader".equals(member.getRole())) {
            long memberCount = getTeamMemberCount(team.getId());
            
            if (memberCount <= 1) {
                // 只剩队长一人，解散队伍
                disbandTeam(characterId);
            } else {
                // 转让队长给最早加入的成员
                transferLeadership(team.getId(), characterId);
            }
        }
        
        // 删除成员记录
        teamMemberMapper.deleteById(member.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disbandTeam(String leaderId) {
        log.info("解散队伍, leaderId: {}", leaderId);
        
        WgTeamMember member = getTeamMemberByCharacter(leaderId);
        if (member == null || !"leader".equals(member.getRole())) {
            throw new JeecgBootException("不是队长");
        }
        
        // 删除所有成员
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getTeamId, member.getTeamId());
        teamMemberMapper.delete(wrapper);
        
        // 删除队伍
        teamMapper.deleteById(member.getTeamId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamInfoVO changeLeader(String leaderId, ChangeLeaderDTO dto) {
        log.info("转让队长, leaderId: {}, newLeaderId: {}", leaderId, dto.getCharacterId());
        
        if (leaderId.equals(dto.getCharacterId())) {
            throw new JeecgBootException("不能转让给自己");
        }
        
        WgTeamMember leaderMember = getTeamMemberByCharacter(leaderId);
        if (leaderMember == null || !"leader".equals(leaderMember.getRole())) {
            throw new JeecgBootException("不是队长");
        }
        
        // 检查新队长是否是成员
        WgTeamMember newLeaderMember = getTeamMemberByCharacter(dto.getCharacterId());
        if (newLeaderMember == null || !newLeaderMember.getTeamId().equals(leaderMember.getTeamId())) {
            throw new JeecgBootException("目标不是队伍成员");
        }
        
        // 更新角色
        leaderMember.setRole("member");
        teamMemberMapper.updateById(leaderMember);
        
        newLeaderMember.setRole("leader");
        teamMemberMapper.updateById(newLeaderMember);
        
        // 更新队伍的leader_id
        WgTeam team = teamMapper.selectById(leaderMember.getTeamId());
        team.setLeaderId(dto.getCharacterId());
        team.setUpdatedAt(new Date());
        teamMapper.updateById(team);
        
        return buildTeamInfoVO(team);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamInfoVO changeStatus(String leaderId, ChangeStatusDTO dto) {
        log.info("切换队伍状态, leaderId: {}, status: {}", leaderId, dto.getStatus());
        
        WgTeamMember member = getTeamMemberByCharacter(leaderId);
        if (member == null || !"leader".equals(member.getRole())) {
            throw new JeecgBootException("不是队长");
        }
        
        if (!"open".equals(dto.getStatus()) && !"closed".equals(dto.getStatus())) {
            throw new JeecgBootException("无效的状态值");
        }
        
        WgTeam team = teamMapper.selectById(member.getTeamId());
        team.setStatus(dto.getStatus());
        team.setUpdatedAt(new Date());
        teamMapper.updateById(team);
        
        return buildTeamInfoVO(team);
    }
    
    // ==================== 私有辅助方法 ====================
    
    private boolean isInTeam(String characterId) {
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getCharacterId, characterId);
        return teamMemberMapper.selectCount(wrapper) > 0;
    }
    
    private WgTeamMember getTeamMemberByCharacter(String characterId) {
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getCharacterId, characterId);
        return teamMemberMapper.selectOne(wrapper);
    }
    
    private long getTeamMemberCount(String teamId) {
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getTeamId, teamId);
        return teamMemberMapper.selectCount(wrapper);
    }
    
    private void transferLeadership(String teamId, String oldLeaderId) {
        // 查询最早加入的成员
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getTeamId, teamId)
               .ne(WgTeamMember::getCharacterId, oldLeaderId)
               .orderByAsc(WgTeamMember::getJoinedAt)
               .last("LIMIT 1");
        
        WgTeamMember newLeader = teamMemberMapper.selectOne(wrapper);
        if (newLeader != null) {
            newLeader.setRole("leader");
            teamMemberMapper.updateById(newLeader);
            
            WgTeam team = teamMapper.selectById(teamId);
            team.setLeaderId(newLeader.getCharacterId());
            teamMapper.updateById(team);
        }
    }
    
    private TeamInfoVO buildTeamInfoVO(WgTeam team) {
        TeamInfoVO vo = new TeamInfoVO();
        vo.setId(team.getId());
        vo.setLeaderId(team.getLeaderId());
        vo.setMaxMembers(team.getMaxMembers());
        vo.setStatus(team.getStatus());
        vo.setTargetDungeon(team.getTargetDungeon());
        vo.setCreatedAt(sdf.format(team.getCreatedAt()));
        
        // 填充队长名称
        WgCharacter leader = characterMapper.selectById(team.getLeaderId());
        if (leader != null) {
            vo.setLeaderName(leader.getCharacterName());
        }
        
        // 填充成员列表
        LambdaQueryWrapper<WgTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgTeamMember::getTeamId, team.getId())
               .orderByAsc(WgTeamMember::getJoinedAt);
        
        List<WgTeamMember> members = teamMemberMapper.selectList(wrapper);
        List<TeamMemberVO> memberVOs = members.stream()
            .map(m -> {
                WgCharacter chara = characterMapper.selectById(m.getCharacterId());
                return chara != null ? buildMemberVO(chara, m.getRole()) : null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        vo.setMembers(memberVOs);
        
        return vo;
    }
    
    private TeamMemberVO buildMemberVO(WgCharacter character, String role) {
        TeamMemberVO vo = new TeamMemberVO();
        vo.setCharacterId(character.getId());
        vo.setCharacterName(character.getCharacterName());
        vo.setProfession(getProfessionName(character.getProfession()));
        vo.setLevel(character.getLevel());
        vo.setRole(role);
        vo.setStatus("online"); // TODO: 实现在线状态
        vo.setJoinedAt(sdf.format(new Date()));
        return vo;
    }
    
    private TeamApplicationVO buildApplicationVO(WgTeamApplication application) {
        TeamApplicationVO vo = new TeamApplicationVO();
        vo.setId(application.getId());
        vo.setTeamId(application.getTeamId());
        vo.setApplicantId(application.getApplicantId());
        vo.setStatus(application.getStatus());
        vo.setCreatedAt(sdf.format(application.getCreatedAt()));
        
        WgCharacter applicant = characterMapper.selectById(application.getApplicantId());
        if (applicant != null) {
            vo.setApplicantName(applicant.getCharacterName());
            vo.setApplicantProfession(getProfessionName(applicant.getProfession()));
            vo.setApplicantLevel(applicant.getLevel());
        }
        
        return vo;
    }
    
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
