package org.jeecg.modules.webgame.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.battle.vo.BattleRewardItemVO;
import org.jeecg.modules.webgame.character.dto.AddExperienceDTO;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.character.service.IWgCharacterService;
import org.jeecg.modules.webgame.item.entity.WgItemTemplate;
import org.jeecg.modules.webgame.item.mapper.WgItemTemplateMapper;
import org.jeecg.modules.webgame.map.dto.WildBattleSettleDTO;
import org.jeecg.modules.webgame.map.entity.WgAreaDrop;
import org.jeecg.modules.webgame.map.entity.WgAreaMonster;
import org.jeecg.modules.webgame.map.mapper.WgAreaDropMapper;
import org.jeecg.modules.webgame.map.mapper.WgAreaMonsterMapper;
import org.jeecg.modules.webgame.map.service.IWildBattleService;
import org.jeecg.modules.webgame.map.vo.WildBattleSettleResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description: 野外战斗Service实现
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Slf4j
@Service
public class WildBattleServiceImpl implements IWildBattleService {

    @Autowired
    private WgCharacterMapper characterMapper;

    @Autowired
    private IWgCharacterService characterService;

    @Autowired
    private WgAreaMonsterMapper areaMonsterMapper;

    @Autowired
    private WgAreaDropMapper areaDropMapper;

    @Autowired
    private WgItemTemplateMapper itemTemplateMapper;

    private static final Random random = new Random();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WildBattleSettleResultVO settleWildBattle(WildBattleSettleDTO settleDTO) {
        // 1. 验证角色存在
        WgCharacter character = characterMapper.selectById(settleDTO.getCharacterId());
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 2. 验证区域存在
        LambdaQueryWrapper<WgAreaMonster> monsterQuery = new LambdaQueryWrapper<>();
        monsterQuery.eq(WgAreaMonster::getId, settleDTO.getMonsterId());
        WgAreaMonster monster = areaMonsterMapper.selectOne(monsterQuery);
        if (monster == null) {
            throw new JeecgBootException("怪物不存在");
        }

        // 3. 验证怪物属于该区域（可选）
        if (!monster.getAreaId().equals(settleDTO.getAreaId())) {
            log.warn("怪物 {} 不属于区域 {}", settleDTO.getMonsterId(), settleDTO.getAreaId());
        }

        // 4. 验证战斗结果
        if (!"win".equals(settleDTO.getBattleResult()) && !"lose".equals(settleDTO.getBattleResult())) {
            throw new JeecgBootException("无效的战斗结果");
        }

        // 5. 只有胜利才发放奖励
        WildBattleSettleResultVO result = new WildBattleSettleResultVO();
        if ("win".equals(settleDTO.getBattleResult())) {
            // 6. 计算经验值
            int exp = calculateExp(monster);
            result.setExp(exp);

            // 7. 计算金币
            int gold = calculateGold(monster);
            result.setGold(gold);

            // 8. 计算掉落物品
            List<BattleRewardItemVO> items = calculateDrops(settleDTO.getAreaId(), monster.getType());
            result.setItems(items);

            // 9. 计算战宠经验（经验的30%）
            int petExp = (int) Math.floor(exp * 0.3);
            result.setPetExp(petExp);

            // 10. 给角色增加经验
            AddExperienceDTO expDTO = new AddExperienceDTO();
            expDTO.setCharacterId(character.getId());
            expDTO.setExpToAdd(exp);
            var expResult = characterService.addExperience(expDTO);
            boolean levelUp = expResult.getLevelUp() != null && expResult.getLevelUp().getLevelsGained() > 0;
            result.setLevelUp(levelUp);

            // 11. 给角色增加金币（这里简化处理，实际应该有金币表）
            // TODO: 实现金币系统

            // 12. 添加掉落物品到背包
            // TODO: 实现背包添加物品功能
            // for (BattleRewardItemVO item : items) {
            //     characterService.addItemToInventory(character.getId(), item.getItemId(), item.getQuantity());
            // }

            log.info("野外战斗结算成功: characterId={}, monsterId={}, exp={}, gold={}, items={}",
                    character.getId(), monster.getId(), exp, gold, items.size());
        } else {
            // 战斗失败，不给奖励
            result.setExp(0);
            result.setGold(0);
            result.setItems(new ArrayList<>());
            result.setLevelUp(false);
            result.setPetExp(0);
        }

        return result;
    }

    /**
     * 计算经验值
     * baseExp = 10 + monster.level × 5
     * typeMultiplier = boss ? 3.0 : elite ? 2.0 : 1.0
     * totalExp = floor(baseExp × typeMultiplier)
     */
    private int calculateExp(WgAreaMonster monster) {
        int baseExp = 10 + monster.getLevel() * 5;
        double typeMultiplier = getTypeMultiplier(monster.getType());
        return (int) Math.floor(baseExp * typeMultiplier);
    }

    /**
     * 计算金币
     * baseGold = 5 + monster.level × 2
     * typeMultiplier = boss ? 5.0 : elite ? 3.0 : 1.0
     * totalGold = floor(baseGold × typeMultiplier)
     */
    private int calculateGold(WgAreaMonster monster) {
        int baseGold = 5 + monster.getLevel() * 2;
        double typeMultiplier = getGoldMultiplier(monster.getType());
        return (int) Math.floor(baseGold * typeMultiplier);
    }

    /**
     * 计算掉落物品
     */
    private List<BattleRewardItemVO> calculateDrops(String areaId, String monsterType) {
        List<BattleRewardItemVO> drops = new ArrayList<>();

        // 查询该区域的掉落配置
        LambdaQueryWrapper<WgAreaDrop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WgAreaDrop::getAreaId, areaId);
        List<WgAreaDrop> areaDrops = areaDropMapper.selectList(queryWrapper);

        // 根据稀有度和怪物类型过滤
        for (WgAreaDrop drop : areaDrops) {
            boolean shouldDrop = false;

            // 根据稀有度判断是否可能掉落
            if ("Rare".equals(drop.getRarity()) && "normal".equals(monsterType)) {
                shouldDrop = true;
            } else if ("Epic".equals(drop.getRarity()) && ("elite".equals(monsterType) || "boss".equals(monsterType))) {
                shouldDrop = true;
            } else if ("Legendary".equals(drop.getRarity()) && "boss".equals(monsterType)) {
                shouldDrop = true;
            }

            // 掷骰判定
            if (shouldDrop && random.nextDouble() < drop.getDropRate().doubleValue()) {
                BattleRewardItemVO rewardItem = new BattleRewardItemVO();
                // 需要将 Integer 转为 String
                String itemIdStr = drop.getItemId() != null ? String.valueOf(drop.getItemId()) : null;
                rewardItem.setItemId(itemIdStr);

                // 查询物品名称（需要将 Integer 转为 String）
                WgItemTemplate itemTemplate = itemIdStr != null ? itemTemplateMapper.selectById(itemIdStr) : null;
                if (itemTemplate != null) {
                    rewardItem.setName(itemTemplate.getName());
                } else {
                    rewardItem.setName("未知物品");
                }

                rewardItem.setQuantity(1); // 简化处理，固定数量为1
                rewardItem.setQuality(drop.getRarity());

                drops.add(rewardItem);
            }
        }

        return drops;
    }

    /**
     * 获取经验倍率
     */
    private double getTypeMultiplier(String type) {
        if ("boss".equals(type)) {
            return 3.0;
        } else if ("elite".equals(type)) {
            return 2.0;
        }
        return 1.0;
    }

    /**
     * 获取金币倍率
     */
    private double getGoldMultiplier(String type) {
        if ("boss".equals(type)) {
            return 5.0;
        } else if ("elite".equals(type)) {
            return 3.0;
        }
        return 1.0;
    }
}
