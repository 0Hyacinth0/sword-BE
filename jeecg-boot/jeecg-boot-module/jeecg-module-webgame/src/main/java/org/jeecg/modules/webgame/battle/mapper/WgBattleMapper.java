package org.jeecg.modules.webgame.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.battle.entity.WgBattle;

/**
 * @Description: 战斗实例Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Mapper
public interface WgBattleMapper extends BaseMapper<WgBattle> {

    /**
     * 根据角色ID查询进行中的战斗
     * @param characterId 角色ID
     * @return 战斗实例
     */
    @Select("SELECT * FROM wg_battle WHERE character_id = #{characterId} AND status = 'in_progress' ORDER BY create_time DESC LIMIT 1")
    WgBattle selectInProgressBattle(@Param("characterId") String characterId);

    /**
     * 根据战斗ID查询
     * @param battleId 战斗ID
     * @return 战斗实例
     */
    @Select("SELECT * FROM wg_battle WHERE battle_id = #{battleId}")
    WgBattle selectByBattleId(@Param("battleId") String battleId);
}
