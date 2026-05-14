package org.jeecg.modules.webgame.dungeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.battle.vo.BattleRewardItemVO;
import org.jeecg.modules.webgame.dungeon.dto.DungeonDropTableDTO;
import org.jeecg.modules.webgame.dungeon.entity.WgDropEntry;
import org.jeecg.modules.webgame.dungeon.entity.WgDropTable;
import org.jeecg.modules.webgame.dungeon.mapper.WgDropEntryMapper;
import org.jeecg.modules.webgame.dungeon.mapper.WgDropTableMapper;
import org.jeecg.modules.webgame.dungeon.service.IDungeonDropService;
import org.jeecg.modules.webgame.item.entity.WgItemTemplate;
import org.jeecg.modules.webgame.item.mapper.WgItemTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description: 副本掉落Service实现
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Slf4j
@Service
public class DungeonDropServiceImpl implements IDungeonDropService {

    @Autowired
    private WgDropTableMapper dropTableMapper;

    @Autowired
    private WgDropEntryMapper dropEntryMapper;

    @Autowired
    private WgItemTemplateMapper itemTemplateMapper;

    private static final Random random = new Random();

    @Override
    public DungeonDropTableDTO getDropTable(String dungeonId) {
        // 1. 查询掉落表配置
        WgDropTable dropTable = dropTableMapper.selectById(dungeonId);
        if (dropTable == null) {
            throw new JeecgBootException("副本掉落表不存在");
        }

        // 2. 组装 DTO
        DungeonDropTableDTO dto = new DungeonDropTableDTO();
        dto.setDungeonId(dungeonId);
        dto.setDropRateMultiplier(dropTable.getDropRateMultiplier());

        // 3. 查询普通层掉落
        List<WgDropEntry> floorEntries = dropEntryMapper.selectByDungeonAndFloorType(dungeonId, "floor");
        dto.setFloorDrops(convertToRewardItems(floorEntries));

        // 4. 查询Boss层掉落
        List<WgDropEntry> bossEntries = dropEntryMapper.selectByDungeonAndFloorType(dungeonId, "boss");
        dto.setBossDrops(convertToRewardItems(bossEntries));

        return dto;
    }

    @Override
    public List<BattleRewardItemVO> resolveDrops(String dungeonId, boolean isBossFloor) {
        // 1. 查询掉落表配置
        WgDropTable dropTable = dropTableMapper.selectById(dungeonId);
        if (dropTable == null) {
            log.warn("副本 {} 的掉落表不存在，返回空列表", dungeonId);
            return new ArrayList<>();
        }

        // 2. 查询对应楼层类型的掉落条目
        String floorType = isBossFloor ? "boss" : "floor";
        List<WgDropEntry> entries = dropEntryMapper.selectByDungeonAndFloorType(dungeonId, floorType);

        // 3. 掷骰判定掉落
        List<BattleRewardItemVO> drops = new ArrayList<>();
        for (WgDropEntry entry : entries) {
            // 计算实际掉落率
            double actualDropRate = entry.getDropRate().doubleValue() * 
                                   dropTable.getDropRateMultiplier().doubleValue();
            actualDropRate = Math.min(actualDropRate, 1.0);

            // 掷骰
            if (random.nextDouble() < actualDropRate) {
                BattleRewardItemVO rewardItem = createRewardItem(entry);
                
                // 随机数量
                int quantity = entry.getMinQuantity();
                if (entry.getMaxQuantity() > entry.getMinQuantity()) {
                    quantity += random.nextInt(entry.getMaxQuantity() - entry.getMinQuantity() + 1);
                }
                rewardItem.setQuantity(quantity);

                drops.add(rewardItem);
            }
        }

        log.info("副本 {} {} 层掉落解析完成，共 {} 个物品", 
                dungeonId, isBossFloor ? "Boss" : "普通", drops.size());
        return drops;
    }

    /**
     * 转换掉落条目为奖励物品列表（仅展示用，不掷骰）
     */
    private List<BattleRewardItemVO> convertToRewardItems(List<WgDropEntry> entries) {
        List<BattleRewardItemVO> items = new ArrayList<>();
        for (WgDropEntry entry : entries) {
            items.add(createRewardItem(entry));
        }
        return items;
    }

    /**
     * 创建奖励物品VO
     */
    private BattleRewardItemVO createRewardItem(WgDropEntry entry) {
        BattleRewardItemVO vo = new BattleRewardItemVO();
        // itemId 需要从 Integer 转换为 String
        String itemIdStr = entry.getItemId() != null ? String.valueOf(entry.getItemId()) : null;
        vo.setItemId(itemIdStr);
        vo.setQuantity(1);

        // 查询物品名称（WgItemTemplate 的主键是 String itemId）
        WgItemTemplate itemTemplate = itemIdStr != null ? itemTemplateMapper.selectById(itemIdStr) : null;
        if (itemTemplate != null) {
            vo.setName(itemTemplate.getName());
            // 使用物品模板的稀有度
            vo.setQuality(itemTemplate.getRarity() != null ? itemTemplate.getRarity() : "common");
        } else {
            vo.setName("未知物品");
            vo.setQuality("common");
        }

        return vo;
    }
}
