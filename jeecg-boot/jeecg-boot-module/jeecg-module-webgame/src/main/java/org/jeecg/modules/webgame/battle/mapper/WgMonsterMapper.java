package org.jeecg.modules.webgame.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.battle.entity.WgMonster;

import java.util.List;

/**
 * @Description: 怪物模板Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Mapper
public interface WgMonsterMapper extends BaseMapper<WgMonster> {

    /**
     * 查询所有怪物
     * @return 怪物列表
     */
    @Select("SELECT * FROM wg_monster ORDER BY min_level, monster_name")
    List<WgMonster> selectAllMonsters();

    /**
     * 根据ID查询怪物
     * @param id 怪物ID
     * @return 怪物信息
     */
    @Select("SELECT * FROM wg_monster WHERE id = #{id}")
    WgMonster selectById(@Param("id") String id);
}
