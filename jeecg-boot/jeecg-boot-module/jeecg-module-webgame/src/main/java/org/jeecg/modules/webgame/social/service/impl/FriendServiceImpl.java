package org.jeecg.modules.webgame.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.social.dto.FriendRequestDTO;
import org.jeecg.modules.webgame.social.dto.HandleFriendRequestDTO;
import org.jeecg.modules.webgame.social.entity.WgFriendRequest;
import org.jeecg.modules.webgame.social.entity.WgFriendship;
import org.jeecg.modules.webgame.social.mapper.WgFriendRequestMapper;
import org.jeecg.modules.webgame.social.mapper.WgFriendshipMapper;
import org.jeecg.modules.webgame.social.service.IFriendService;
import org.jeecg.modules.webgame.social.vo.FriendInfoVO;
import org.jeecg.modules.webgame.social.vo.FriendListVO;
import org.jeecg.modules.webgame.social.vo.FriendRequestVO;
import org.jeecg.modules.webgame.social.vo.SearchPlayerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 好友系统 Service 实现
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@Service
public class FriendServiceImpl implements IFriendService {
    
    @Autowired
    private WgFriendshipMapper friendshipMapper;
    
    @Autowired
    private WgFriendRequestMapper friendRequestMapper;
    
    @Autowired
    private WgCharacterMapper characterMapper;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    @Override
    public FriendListVO getFriendList(String characterId) {
        log.info("获取好友列表, characterId: {}", characterId);
        
        FriendListVO result = new FriendListVO();
        
        // 1. 查询好友列表
        List<WgFriendship> friendships = friendshipMapper.selectFriendsByCharacterId(characterId);
        List<FriendInfoVO> friends = friendships.stream()
            .map(f -> buildFriendInfoVO(f, characterId))
            .collect(Collectors.toList());
        result.setFriends(friends);
        
        // 2. 查询待处理请求
        List<WgFriendRequest> pendingRequests = friendRequestMapper.selectPendingRequestsByReceiver(characterId);
        List<FriendRequestVO> pendingVOs = pendingRequests.stream()
            .map(this::buildFriendRequestVO)
            .collect(Collectors.toList());
        result.setPendingRequests(pendingVOs);
        
        // 3. 查询已发送请求
        List<WgFriendRequest> sentRequests = friendRequestMapper.selectSentRequestsBySender(characterId);
        List<FriendRequestVO> sentVOs = sentRequests.stream()
            .map(this::buildFriendRequestVO)
            .collect(Collectors.toList());
        result.setSentRequests(sentVOs);
        
        return result;
    }
    
    @Override
    public List<SearchPlayerVO> searchPlayers(String keyword, String characterId) {
        log.info("搜索玩家, keyword: {}, characterId: {}", keyword, characterId);
        
        if (keyword == null || keyword.length() < 2) {
            throw new JeecgBootException("搜索关键词至少2个字符");
        }
        
        // 查询匹配的角色
        LambdaQueryWrapper<WgCharacter> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(WgCharacter::getCharacterName, keyword)
               .ne(WgCharacter::getId, characterId)
               .last("LIMIT 20");
        
        List<WgCharacter> characters = characterMapper.selectList(wrapper);
        
        // 构建搜索结果
        return characters.stream()
            .map(chara -> {
                SearchPlayerVO vo = new SearchPlayerVO();
                vo.setCharacterId(chara.getId());
                vo.setCharacterName(chara.getCharacterName());
                vo.setProfession(getProfessionName(chara.getProfession()));
                vo.setLevel(chara.getLevel());
                
                // 检查是否已是好友
                vo.setIsFriend(isFriend(characterId, chara.getId()));
                
                // 检查是否已发送请求
                vo.setHasPendingRequest(hasPendingRequest(characterId, chara.getId()));
                
                return vo;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String sendFriendRequest(String fromCharacterId, FriendRequestDTO dto) {
        log.info("发送好友请求, from: {}, to: {}", fromCharacterId, dto.getToCharacterId());
        
        String toCharacterId = dto.getToCharacterId();
        
        // 1. 检查目标角色是否存在
        WgCharacter targetChara = characterMapper.selectById(toCharacterId);
        if (targetChara == null) {
            throw new JeecgBootException("目标角色不存在");
        }
        
        // 2. 检查是否已是好友
        if (isFriend(fromCharacterId, toCharacterId)) {
            throw new JeecgBootException("已经是好友关系");
        }
        
        // 3. 检查是否已发送过请求
        if (hasPendingRequest(fromCharacterId, toCharacterId)) {
            throw new JeecgBootException("已发送过好友请求");
        }
        
        // 4. 创建好友请求
        WgFriendRequest request = new WgFriendRequest();
        request.setId(UUID.randomUUID().toString());
        request.setFromCharacterId(fromCharacterId);
        request.setToCharacterId(toCharacterId);
        request.setStatus("pending");
        request.setCreatedAt(new Date());
        request.setUpdatedAt(new Date());
        
        friendRequestMapper.insert(request);
        
        log.info("好友请求发送成功, requestId: {}", request.getId());
        return request.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FriendInfoVO acceptFriendRequest(String characterId, HandleFriendRequestDTO dto) {
        log.info("接受好友请求, characterId: {}, requestId: {}", characterId, dto.getRequestId());
        
        // 1. 查询请求
        WgFriendRequest request = friendRequestMapper.selectById(dto.getRequestId());
        if (request == null) {
            throw new JeecgBootException("请求不存在");
        }
        
        // 2. 校验接收者
        if (!request.getToCharacterId().equals(characterId)) {
            throw new JeecgBootException("无权处理此请求");
        }
        
        // 3. 校验状态
        if (!"pending".equals(request.getStatus())) {
            throw new JeecgBootException("请求状态不正确");
        }
        
        // 4. 更新请求状态
        request.setStatus("accepted");
        request.setUpdatedAt(new Date());
        friendRequestMapper.updateById(request);
        
        // 5. 创建好友关系（确保 character_id_1 < character_id_2）
        String charId1 = request.getFromCharacterId();
        String charId2 = request.getToCharacterId();
        if (charId1.compareTo(charId2) > 0) {
            String temp = charId1;
            charId1 = charId2;
            charId2 = temp;
        }
        
        WgFriendship friendship = new WgFriendship();
        friendship.setId(UUID.randomUUID().toString());
        friendship.setCharacterId1(charId1);
        friendship.setCharacterId2(charId2);
        friendship.setCreatedAt(new Date());
        
        friendshipMapper.insert(friendship);
        
        // 6. 返回好友信息
        WgCharacter friendChara = characterMapper.selectById(request.getFromCharacterId());
        return buildFriendInfoVOFromCharacter(friendChara);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectFriendRequest(String characterId, HandleFriendRequestDTO dto) {
        log.info("拒绝好友请求, characterId: {}, requestId: {}", characterId, dto.getRequestId());
        
        // 1. 查询请求
        WgFriendRequest request = friendRequestMapper.selectById(dto.getRequestId());
        if (request == null) {
            throw new JeecgBootException("请求不存在");
        }
        
        // 2. 校验接收者
        if (!request.getToCharacterId().equals(characterId)) {
            throw new JeecgBootException("无权处理此请求");
        }
        
        // 3. 更新状态
        request.setStatus("rejected");
        request.setUpdatedAt(new Date());
        friendRequestMapper.updateById(request);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFriend(String characterId, String friendCharacterId) {
        log.info("删除好友, characterId: {}, friendCharacterId: {}", characterId, friendCharacterId);
        
        // 1. 查询好友关系
        String charId1 = characterId;
        String charId2 = friendCharacterId;
        if (charId1.compareTo(charId2) > 0) {
            String temp = charId1;
            charId1 = charId2;
            charId2 = temp;
        }
        
        LambdaQueryWrapper<WgFriendship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgFriendship::getCharacterId1, charId1)
               .eq(WgFriendship::getCharacterId2, charId2);
        
        WgFriendship friendship = friendshipMapper.selectOne(wrapper);
        if (friendship == null) {
            throw new JeecgBootException("好友关系不存在");
        }
        
        // 2. 删除好友关系
        friendshipMapper.deleteById(friendship.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelFriendRequest(String characterId, String requestId) {
        log.info("取消好友请求, characterId: {}, requestId: {}", characterId, requestId);
        
        // 1. 查询请求
        WgFriendRequest request = friendRequestMapper.selectById(requestId);
        if (request == null) {
            throw new JeecgBootException("请求不存在");
        }
        
        // 2. 校验发送者
        if (!request.getFromCharacterId().equals(characterId)) {
            throw new JeecgBootException("无权取消此请求");
        }
        
        // 3. 删除请求
        friendRequestMapper.deleteById(requestId);
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 构建好友信息 VO
     */
    private FriendInfoVO buildFriendInfoVO(WgFriendship friendship, String currentCharacterId) {
        // 确定好友的角色ID
        String friendCharacterId = friendship.getCharacterId1().equals(currentCharacterId) 
            ? friendship.getCharacterId2() 
            : friendship.getCharacterId1();
        
        WgCharacter friendChara = characterMapper.selectById(friendCharacterId);
        if (friendChara == null) {
            return null;
        }
        
        return buildFriendInfoVOFromCharacter(friendChara);
    }
    
    /**
     * 从角色对象构建好友信息 VO
     */
    private FriendInfoVO buildFriendInfoVOFromCharacter(WgCharacter character) {
        FriendInfoVO vo = new FriendInfoVO();
        vo.setCharacterId(character.getId());
        vo.setCharacterName(character.getCharacterName());
        vo.setProfession(getProfessionName(character.getProfession()));
        vo.setLevel(character.getLevel());
        vo.setStatus("offline"); // TODO: 后续通过 WebSocket 实现在线状态
        vo.setLastOnlineAt(null);
        vo.setAddedAt(sdf.format(new Date()));
        return vo;
    }
    
    /**
     * 构建好友请求 VO
     */
    private FriendRequestVO buildFriendRequestVO(WgFriendRequest request) {
        FriendRequestVO vo = new FriendRequestVO();
        BeanUtils.copyProperties(request, vo);
        
        // 填充发送者信息
        WgCharacter fromChara = characterMapper.selectById(request.getFromCharacterId());
        if (fromChara != null) {
            vo.setFromCharacterName(fromChara.getCharacterName());
            vo.setFromProfession(getProfessionName(fromChara.getProfession()));
            vo.setFromLevel(fromChara.getLevel());
        }
        
        vo.setCreatedAt(sdf.format(request.getCreatedAt()));
        return vo;
    }
    
    /**
     * 检查是否是好友
     */
    private boolean isFriend(String characterId1, String characterId2) {
        String charId1 = characterId1;
        String charId2 = characterId2;
        if (charId1.compareTo(charId2) > 0) {
            String temp = charId1;
            charId1 = charId2;
            charId2 = temp;
        }
        
        LambdaQueryWrapper<WgFriendship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgFriendship::getCharacterId1, charId1)
               .eq(WgFriendship::getCharacterId2, charId2);
        
        return friendshipMapper.selectCount(wrapper) > 0;
    }
    
    /**
     * 检查是否有待处理的请求
     */
    private boolean hasPendingRequest(String fromCharacterId, String toCharacterId) {
        LambdaQueryWrapper<WgFriendRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgFriendRequest::getFromCharacterId, fromCharacterId)
               .eq(WgFriendRequest::getToCharacterId, toCharacterId)
               .eq(WgFriendRequest::getStatus, "pending");
        
        return friendRequestMapper.selectCount(wrapper) > 0;
    }
    
    /**
     * 获取职业名称
     */
    private String getProfessionName(Integer profession) {
        if (profession == null) {
            return "Unknown";
        }
        switch (profession) {
            case 1: return "Warrior";
            case 2: return "Mage";
            case 3: return "Hunter";
            default: return "Unknown";
        }
    }
}
