package org.jeecg.modules.webgame.pvp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.pvp.dto.PvpMatchRequestDTO;
import org.jeecg.modules.webgame.pvp.entity.WgArenaPlayer;
import org.jeecg.modules.webgame.pvp.entity.WgArenaSeason;
import org.jeecg.modules.webgame.pvp.mapper.WgArenaPlayerMapper;
import org.jeecg.modules.webgame.pvp.mapper.WgArenaSeasonMapper;
import org.jeecg.modules.webgame.pvp.service.IArenaService;
import org.jeecg.modules.webgame.pvp.vo.ArenaPlayerVO;
import org.jeecg.modules.webgame.pvp.vo.ArenaSeasonVO;
import org.jeecg.modules.webgame.pvp.vo.PvpMatchOpponentVO;
import org.jeecg.modules.webgame.pvp.vo.PvpSettlementResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 竞技场服务实现
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Slf4j
@Service
public class ArenaServiceImpl implements IArenaService {

    @Autowired
    private WgArenaSeasonMapper seasonMapper;

    @Autowired
    private WgArenaPlayerMapper playerMapper;

    @Autowired
    private WgCharacterMapper characterMapper;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public ArenaSeasonVO getCurrentSeason() {
        log.info("获取当前活跃赛季信息");
        WgArenaSeason season = seasonMapper.selectActiveSeason();
        
        if (season == null) {
            log.warn("未找到活跃赛季");
            return null;
        }

        ArenaSeasonVO vo = new ArenaSeasonVO();
        BeanUtils.copyProperties(season, vo);
        
        // 格式化日期
        vo.setStartDate(dateFormat.format(season.getStartDate()));
        vo.setEndDate(dateFormat.format(season.getEndDate()));
        
        log.info("当前赛季: {} - {}", season.getSeasonId(), season.getSeasonName());
        return vo;
    }

    @Override
    public ArenaPlayerVO getPlayerArenaData(String characterId) {
        log.info("获取玩家竞技数据: characterId={}", characterId);
        
        // 查询当前赛季的玩家数据
        WgArenaPlayer playerData = playerMapper.selectCurrentSeasonData(characterId);
        
        if (playerData == null) {
            log.info("玩家暂无竞技数据，创建默认数据: characterId={}", characterId);
            // 如果玩家没有数据，创建默认数据
            WgArenaSeason currentSeason = seasonMapper.selectActiveSeason();
            if (currentSeason != null) {
                playerData = new WgArenaPlayer();
                playerData.setCharacterId(characterId);
                playerData.setSeasonId(currentSeason.getSeasonId());
                playerData.setTier("bronze");
                playerData.setSubTier("I");
                playerData.setScore(0);
                playerData.setWins(0);
                playerData.setLosses(0);
                playerData.setDraws(0);
                
                playerMapper.insert(playerData);
            } else {
                log.error("无法创建玩家数据：无活跃赛季");
                return null;
            }
        }

        ArenaPlayerVO vo = new ArenaPlayerVO();
        BeanUtils.copyProperties(playerData, vo);
        
        // 计算胜率
        int totalGames = playerData.getWins() + playerData.getLosses() + playerData.getDraws();
        double winRate = totalGames > 0 ? (double) playerData.getWins() / totalGames * 100 : 0.0;
        vo.setWinRate(Math.round(winRate * 100.0) / 100.0); // 保留两位小数
        
        log.info("玩家竞技数据: tier={}, score={}, wins={}, losses={}", 
                vo.getTier(), vo.getScore(), vo.getWins(), vo.getLosses());
        return vo;
    }

    @Override
    public PvpMatchOpponentVO matchOpponent(String playerCharacterId, PvpMatchRequestDTO request) {
        log.info("PVP匹配对手: playerCharacterId={}, score={}", playerCharacterId, request.getScore());
        
        // 获取当前活跃赛季
        WgArenaSeason currentSeason = seasonMapper.selectActiveSeason();
        if (currentSeason == null) {
            log.error("无活跃赛季，无法匹配");
            return null;
        }

        // 简单匹配逻辑：查找积分相近的对手（这里简化为随机选择一个其他玩家）
        // 实际项目中应使用更复杂的匹配算法
        List<WgArenaPlayer> allPlayers = playerMapper.selectList(null);
        if (allPlayers == null || allPlayers.isEmpty()) {
            log.warn("暂无其他玩家可匹配");
            return null;
        }

        // 过滤掉自己，并找到积分最接近的对手
        WgArenaPlayer bestOpponent = null;
        int minScoreDiff = Integer.MAX_VALUE;
        
        // 获取请求中的积分，如果为空则使用默认值0
        int requestScore = request.getScore() != null ? request.getScore() : 0;
        
        for (WgArenaPlayer player : allPlayers) {
            if (!player.getCharacterId().equals(playerCharacterId) && 
                player.getSeasonId().equals(currentSeason.getSeasonId())) {
                int scoreDiff = Math.abs(player.getScore() - requestScore);
                if (scoreDiff < minScoreDiff) {
                    minScoreDiff = scoreDiff;
                    bestOpponent = player;
                }
            }
        }

        if (bestOpponent == null) {
            log.warn("未找到合适的对手");
            return null;
        }

        // 获取对手角色信息
        WgCharacter opponentChar = characterMapper.selectById(bestOpponent.getCharacterId());
        if (opponentChar == null) {
            log.error("对手角色不存在: characterId={}", bestOpponent.getCharacterId());
            return null;
        }

        PvpMatchOpponentVO vo = new PvpMatchOpponentVO();
        vo.setCharacterId(opponentChar.getId());
        vo.setCharacterName(opponentChar.getCharacterName());
        vo.setProfession(getProfessionName(opponentChar.getProfession()));
        vo.setLevel(opponentChar.getLevel());
        vo.setTier(bestOpponent.getTier());
        vo.setSubTier(bestOpponent.getSubTier());
        vo.setScore(bestOpponent.getScore());

        log.info("匹配成功: opponent={}, tier={}, score={}", 
                vo.getCharacterName(), vo.getTier(), vo.getScore());
        return vo;
    }

    @Override
    public PvpSettlementResultVO settleBattle(String playerCharacterId, String opponentCharacterId, boolean won) {
        log.info("结算PVP战斗: player={}, opponent={}, won={}", playerCharacterId, opponentCharacterId, won);
        
        // 获取当前活跃赛季
        WgArenaSeason currentSeason = seasonMapper.selectActiveSeason();
        if (currentSeason == null) {
            log.error("无活跃赛季，无法结算");
            return null;
        }

        // 获取玩家和对手的竞技数据
        WgArenaPlayer playerData = playerMapper.selectByCharacterAndSeason(playerCharacterId, currentSeason.getSeasonId());
        WgArenaPlayer opponentData = playerMapper.selectByCharacterAndSeason(opponentCharacterId, currentSeason.getSeasonId());
        
        if (playerData == null || opponentData == null) {
            log.error("玩家或对手竞技数据不存在");
            return null;
        }

        // 保存原段位信息
        PvpSettlementResultVO.TierInfoVO oldTierInfo = createTierInfo(playerData);
        
        int oldScore = playerData.getScore();
        int opponentScore = opponentData.getScore();
        
        // 计算积分变化（简化Elo算法）
        int baseChange = won ? 25 : -25;
        int adjustment = (int) Math.round((opponentScore - oldScore) / 100.0);
        adjustment = Math.max(-10, Math.min(10, adjustment)); // 限制调整范围在±10
        
        int scoreChange = baseChange + adjustment;
        // 确保积分不会低于0
        int newScore = Math.max(0, oldScore + scoreChange);
        
        // 更新玩家数据
        playerData.setScore(newScore);
        if (won) {
            playerData.setWins(playerData.getWins() + 1);
        } else {
            playerData.setLosses(playerData.getLosses() + 1);
        }
        
        // 更新段位
        updateTier(playerData);
        playerMapper.updateById(playerData);
        
        // 更新对手数据（如果对手输了，也要记录）
        if (!won) {
            opponentData.setWins(opponentData.getWins() + 1);
        } else {
            opponentData.setLosses(opponentData.getLosses() + 1);
        }
        updateTier(opponentData);
        playerMapper.updateById(opponentData);
        
        // 创建结果VO
        PvpSettlementResultVO result = new PvpSettlementResultVO();
        result.setScoreChange(scoreChange);
        result.setOldScore(oldScore);
        result.setNewScore(newScore);
        
        PvpSettlementResultVO.TierInfoVO newTierInfo = createTierInfo(playerData);
        result.setOldTier(oldTierInfo);
        result.setNewTier(newTierInfo);
        result.setTierChanged(!oldTierInfo.getTier().equals(newTierInfo.getTier()) || 
                             !oldTierInfo.getSubTier().equals(newTierInfo.getSubTier()));
        
        log.info("结算完成: scoreChange={}, newScore={}, tierChanged={}", 
                scoreChange, newScore, result.getTierChanged());
        return result;
    }

    /**
     * 根据职业代码获取职业名称
     */
    private String getProfessionName(Integer profession) {
        switch (profession) {
            case 1: return "战士";
            case 2: return "法师";
            case 3: return "猎人";
            default: return "未知";
        }
    }

    /**
     * 创建段位信息VO
     */
    private PvpSettlementResultVO.TierInfoVO createTierInfo(WgArenaPlayer player) {
        PvpSettlementResultVO.TierInfoVO tierInfo = new PvpSettlementResultVO.TierInfoVO();
        tierInfo.setTier(player.getTier());
        tierInfo.setSubTier(player.getSubTier());
        tierInfo.setTierName(formatTierName(player.getTier(), player.getSubTier()));
        
        // 计算进度和剩余积分
        int[] tierRanges = getTierRange(player.getTier());
        int minScore = tierRanges[0];
        int maxScore = tierRanges[1];
        
        int range = maxScore - minScore;
        int progressInTier = player.getScore() - minScore;
        double progress = range > 0 ? (double) progressInTier / range : 0.0;
        
        tierInfo.setProgress(Math.round(progress * 100.0) / 100.0); // 保留两位小数
        tierInfo.setRemainingScore(maxScore - player.getScore());
        
        return tierInfo;
    }

    /**
     * 格式化段位名称
     */
    private String formatTierName(String tier, String subTier) {
        Map<String, String> tierNames = new HashMap<>();
        tierNames.put("bronze", "青铜");
        tierNames.put("silver", "白银");
        tierNames.put("gold", "黄金");
        tierNames.put("platinum", "铂金");
        tierNames.put("diamond", "钻石");
        tierNames.put("master", "王者");
        
        String name = tierNames.getOrDefault(tier, "未知");
        if (!"master".equals(tier)) {
            return "◆ " + name + " " + subTier;
        } else {
            return "◆ " + name;
        }
    }

    /**
     * 获取段位积分范围 [min, max]
     */
    private int[] getTierRange(String tier) {
        switch (tier) {
            case "bronze": return new int[]{0, 599};
            case "silver": return new int[]{600, 1199};
            case "gold": return new int[]{1200, 1799};
            case "platinum": return new int[]{1800, 2399};
            case "diamond": return new int[]{2400, 2799};
            case "master": return new int[]{2800, Integer.MAX_VALUE};
            default: return new int[]{0, 599};
        }
    }

    /**
     * 根据积分更新段位
     */
    private void updateTier(WgArenaPlayer player) {
        int score = player.getScore();
        String newTier;
        String newSubTier;
        
        if (score >= 2800) {
            newTier = "master";
            newSubTier = "";
        } else if (score >= 2400) {
            newTier = "diamond";
            newSubTier = calculateSubTier(score, 2400, 2799);
        } else if (score >= 1800) {
            newTier = "platinum";
            newSubTier = calculateSubTier(score, 1800, 2399);
        } else if (score >= 1200) {
            newTier = "gold";
            newSubTier = calculateSubTier(score, 1200, 1799);
        } else if (score >= 600) {
            newTier = "silver";
            newSubTier = calculateSubTier(score, 600, 1199);
        } else {
            newTier = "bronze";
            newSubTier = calculateSubTier(score, 0, 599);
        }
        
        player.setTier(newTier);
        player.setSubTier(newSubTier);
    }

    /**
     * 计算小级 (I/II/III)
     */
    private String calculateSubTier(int score, int min, int max) {
        int range = max - min + 1;
        int position = score - min;
        
        if (position < range / 3) {
            return "III";
        } else if (position < 2 * range / 3) {
            return "II";
        } else {
            return "I";
        }
    }
}
