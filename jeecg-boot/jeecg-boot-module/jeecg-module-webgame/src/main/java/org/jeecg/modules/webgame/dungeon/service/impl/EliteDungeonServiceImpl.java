package org.jeecg.modules.webgame.dungeon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.battle.vo.BattleSkillVO;
import org.jeecg.modules.webgame.dungeon.dto.EliteDungeonConfigDTO;
import org.jeecg.modules.webgame.dungeon.entity.WgDungeonConfig;
import org.jeecg.modules.webgame.dungeon.entity.WgEliteSkillGroupEntry;
import org.jeecg.modules.webgame.dungeon.mapper.WgDungeonConfigMapper;
import org.jeecg.modules.webgame.dungeon.mapper.WgEliteSkillGroupEntryMapper;
import org.jeecg.modules.webgame.dungeon.service.IEliteDungeonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 精英副本Service实现
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Slf4j
@Service
public class EliteDungeonServiceImpl implements IEliteDungeonService {

    @Autowired
    private WgDungeonConfigMapper dungeonConfigMapper;

    @Autowired
    private WgEliteSkillGroupEntryMapper eliteSkillGroupEntryMapper;

    @Override
    public EliteDungeonConfigDTO getEliteConfig(String dungeonId) {
        // 1. 查询副本配置
        WgDungeonConfig dungeonConfig = dungeonConfigMapper.selectById(dungeonId);
        if (dungeonConfig == null) {
            throw new JeecgBootException("副本不存在");
        }

        // 2. 检查是否为精英副本
        if (!dungeonConfig.getIsElite()) {
            throw new JeecgBootException("该副本不是精英副本");
        }

        // 3. 组装 DTO
        EliteDungeonConfigDTO dto = new EliteDungeonConfigDTO();
        dto.setDungeonId(dungeonId);
        dto.setIsElite(dungeonConfig.getIsElite());
        dto.setStatMultiplier(dungeonConfig.getEliteStatMultiplier());
        dto.setSkillGroupId(dungeonConfig.getEliteSkillGroupId());

        // 4. 查询技能列表
        if (dungeonConfig.getEliteSkillGroupId() != null) {
            List<WgEliteSkillGroupEntry> entries = eliteSkillGroupEntryMapper
                    .selectByGroupIdOrderByPriority(dungeonConfig.getEliteSkillGroupId());
            
            List<BattleSkillVO> skills = new ArrayList<>();
            // TODO: 这里需要根据 skillId 查询完整的技能信息
            // 目前简化处理，只返回技能ID
            for (WgEliteSkillGroupEntry entry : entries) {
                BattleSkillVO skillVO = new BattleSkillVO();
                skillVO.setId(entry.getSkillId());
                skillVO.setName("技能" + entry.getSkillId()); // 临时名称
                skills.add(skillVO);
            }
            dto.setSkills(skills);
        } else {
            dto.setSkills(new ArrayList<>());
        }

        log.info("获取精英副本配置成功: dungeonId={}, isElite={}, statMultiplier={}", 
                dungeonId, dto.getIsElite(), dto.getStatMultiplier());
        return dto;
    }
}
