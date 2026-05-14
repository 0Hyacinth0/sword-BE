package org.jeecg.modules.webgame.dungeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.dungeon.entity.WgEliteSkillGroupEntry;

import java.util.List;

/**
 * @Description: 精英技能组条目Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Mapper
public interface WgEliteSkillGroupEntryMapper extends BaseMapper<WgEliteSkillGroupEntry> {
    
    /**
     * 根据技能组ID查询技能条目列表（按优先级排序）
     */
    @Select("SELECT * FROM wg_elite_skill_group_entries WHERE group_id = #{groupId} ORDER BY priority ASC")
    List<WgEliteSkillGroupEntry> selectByGroupIdOrderByPriority(@Param("groupId") Integer groupId);
}
