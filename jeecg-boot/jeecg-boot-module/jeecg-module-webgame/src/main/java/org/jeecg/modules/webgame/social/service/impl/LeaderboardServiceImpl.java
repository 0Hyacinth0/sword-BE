package org.jeecg.modules.webgame.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.social.entity.WgFriendship;
import org.jeecg.modules.webgame.social.mapper.WgFriendshipMapper;
import org.jeecg.modules.webgame.social.service.ILeaderboardService;
import org.jeecg.modules.webgame.social.vo.LeaderboardEntryVO;
import org.jeecg.modules.webgame.social.vo.LeaderboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 排行榜系统 Service 实现
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@Service
public class LeaderboardServiceImpl implements ILeaderboardService {
    
    @Autowired
    private WgCharacterMapper characterMapper;
    
    @Autowired
    private WgFriendshipMapper friendshipMapper;
    
    @Override
    public LeaderboardVO getLeaderboard(String category, String scope, String characterId) {
        log.info("获取排行榜, category: {}, scope: {}, characterId: {}", category, scope, characterId);
        
        // 1. 验证参数
        if (!isValidCategory(category)) {
            throw new JeecgBootException("无效的排行榜分类");
        }
        
        if (!"all".equals(scope) && !"friends".equals(scope)) {
            throw new JeecgBootException("无效的范围参数");
        }
        
        // 2. 查询角色列表
        List<WgCharacter> characters;
        if ("friends".equals(scope)) {
            // 好友排行：只查询好友
            characters = getFriendsCharacters(characterId);
        } else {
            // 全服排行：查询所有角色（取前20名）
            characters = getAllCharacters(category);
        }
        
        // 3. 构建排行榜条目
        List<LeaderboardEntryVO> entries = buildLeaderboardEntries(characters, category);
        
        // 4. 计算我的排名和值
        LeaderboardVO vo = new LeaderboardVO();
        vo.setEntries(entries);
        
        // 查询当前角色的排名
        WgCharacter myCharacter = characterMapper.selectById(characterId);
        if (myCharacter != null) {
            Object myValue = getValueByCategory(myCharacter, category);
            Integer myRank = calculateRank(characterId, category, scope);
            
            vo.setMyRank(myRank);
            vo.setMyValue(myValue);
        }
        
        return vo;
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 验证分类是否有效
     */
    private boolean isValidCategory(String category) {
        return "level".equals(category) || "power".equals(category) || "arena".equals(category);
    }
    
    /**
     * 获取好友角色列表
     */
    private List<WgCharacter> getFriendsCharacters(String characterId) {
        // 查询好友关系
        LambdaQueryWrapper<WgFriendship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgFriendship::getCharacterId1, characterId)
               .or()
               .eq(WgFriendship::getCharacterId2, characterId);
        
        List<WgFriendship> friendships = friendshipMapper.selectList(wrapper);
        
        // 提取好友ID
        List<String> friendIds = friendships.stream()
            .map(f -> f.getCharacterId1().equals(characterId) ? f.getCharacterId2() : f.getCharacterId1())
            .collect(Collectors.toList());
        
        if (friendIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 查询好友角色信息
        return characterMapper.selectBatchIds(friendIds);
    }
    
    /**
     * 获取所有角色（按分类排序，取前20）
     */
    private List<WgCharacter> getAllCharacters(String category) {
        LambdaQueryWrapper<WgCharacter> wrapper = new LambdaQueryWrapper<>();
        
        // 简化实现：默认查询 level 不为空
        wrapper.isNotNull(WgCharacter::getLevel);
        
        switch (category) {
            case "level":
                wrapper.orderByDesc(WgCharacter::getLevel)
                       .last("LIMIT 20");
                break;
            case "power":
                // 战力需要计算，这里简化为按等级排序
                wrapper.orderByDesc(WgCharacter::getLevel)
                       .last("LIMIT 20");
                break;
            case "arena":
                // TODO: 竞技积分字段待添加
                wrapper.orderByDesc(WgCharacter::getLevel)
                       .last("LIMIT 20");
                break;
        }
        
        return characterMapper.selectList(wrapper);
    }
    
    /**
     * 构建排行榜条目列表
     */
    private List<LeaderboardEntryVO> buildLeaderboardEntries(List<WgCharacter> characters, String category) {
        List<LeaderboardEntryVO> entries = new ArrayList<>();
        
        for (int i = 0; i < characters.size(); i++) {
            WgCharacter chara = characters.get(i);
            LeaderboardEntryVO entry = new LeaderboardEntryVO();
            
            entry.setRank(i + 1);
            entry.setCharacterId(chara.getId());
            entry.setCharacterName(chara.getCharacterName());
            entry.setProfession(getProfessionName(chara.getProfession()));
            entry.setLevel(chara.getLevel());
            entry.setValue(getValueByCategory(chara, category));
            entry.setIsOnline(false); // TODO: 实现在线状态
            
            entries.add(entry);
        }
        
        return entries;
    }
    
    /**
     * 根据分类获取排序值
     */
    private Object getValueByCategory(WgCharacter character, String category) {
        switch (category) {
            case "level":
                return character.getLevel();
            case "power":
                // TODO: 实现战力计算公式
                return calculatePower(character);
            case "arena":
                // TODO: 返回竞技积分
                return 0;
            default:
                return 0;
        }
    }
    
    /**
     * 计算战力（简化版）
     */
    private Long calculatePower(WgCharacter character) {
        // 简化公式：战力 = 等级 * 100 + 属性总和
        long power = character.getLevel() * 100L;
        power += (character.getStrength() != null ? character.getStrength() : 0);
        power += (character.getIntelligence() != null ? character.getIntelligence() : 0);
        power += (character.getAgility() != null ? character.getAgility() : 0);
        power += (character.getPhysicalAttack() != null ? character.getPhysicalAttack() : 0);
        power += (character.getMagicAttack() != null ? character.getMagicAttack() : 0);
        power += (character.getDefense() != null ? character.getDefense() : 0);
        return power;
    }
    
    /**
     * 计算我的排名
     */
    private Integer calculateRank(String characterId, String category, String scope) {
        WgCharacter myCharacter = characterMapper.selectById(characterId);
        if (myCharacter == null) {
            return null;
        }
        
        Object myValue = getValueByCategory(myCharacter, category);
        
        LambdaQueryWrapper<WgCharacter> wrapper = new LambdaQueryWrapper<>();
        
        if ("friends".equals(scope)) {
            // 好友排行
            List<String> friendIds = getFriendIds(characterId);
            if (friendIds.isEmpty()) {
                return 1; // 没有好友时自己是第1
            }
            wrapper.in(WgCharacter::getId, friendIds);
        }
        
        // 查询比我高的角色数量
        // 简化实现：按等级比较
        wrapper.gt(WgCharacter::getLevel, myValue);
        long higherCount = characterMapper.selectCount(wrapper);
        
        return (int) (higherCount + 1);
    }
    
    /**
     * 获取好友ID列表
     */
    private List<String> getFriendIds(String characterId) {
        LambdaQueryWrapper<WgFriendship> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgFriendship::getCharacterId1, characterId)
               .or()
               .eq(WgFriendship::getCharacterId2, characterId);
        
        List<WgFriendship> friendships = friendshipMapper.selectList(wrapper);
        
        return friendships.stream()
            .map(f -> f.getCharacterId1().equals(characterId) ? f.getCharacterId2() : f.getCharacterId1())
            .collect(Collectors.toList());
    }
    
    /**
     * 获取排序字段
     */
    private String getSortColumn(String category) {
        switch (category) {
            case "level":
                return "level";
            case "power":
                return "level"; // 简化处理
            case "arena":
                return "level"; // TODO: 改为竞技积分字段
            default:
                return "level";
        }
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
