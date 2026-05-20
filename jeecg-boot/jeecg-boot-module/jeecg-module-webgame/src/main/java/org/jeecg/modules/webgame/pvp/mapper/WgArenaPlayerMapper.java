package org.jeecg.modules.webgame.pvp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.pvp.entity.WgArenaPlayer;

/**
 * @Description: 玩家竞技场数据Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Mapper
public interface WgArenaPlayerMapper extends BaseMapper<WgArenaPlayer> {

    /**
     * 根据角色ID和赛季ID查询玩家竞技数据
     * @param characterId 角色ID
     * @param seasonId 赛季ID
     * @return 玩家竞技数据
     */
    @Select("SELECT * FROM wg_arena_player WHERE character_id = #{characterId} AND season_id = #{seasonId}")
    WgArenaPlayer selectByCharacterAndSeason(@Param("characterId") String characterId, @Param("seasonId") String seasonId);

    /**
     * 根据角色ID查询当前赛季的竞技数据
     * @param characterId 角色ID
     * @return 玩家竞技数据
     */
    @Select("SELECT ap.* FROM wg_arena_player ap " +
            "JOIN wg_arena_season s ON ap.season_id = s.season_id " +
            "WHERE ap.character_id = #{characterId} AND s.is_active = 1")
    WgArenaPlayer selectCurrentSeasonData(@Param("characterId") String characterId);
}
