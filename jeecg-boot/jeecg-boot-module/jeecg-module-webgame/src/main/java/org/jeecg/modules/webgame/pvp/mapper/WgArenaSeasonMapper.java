package org.jeecg.modules.webgame.pvp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.pvp.entity.WgArenaSeason;

/**
 * @Description: 竞技场赛季Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Mapper
public interface WgArenaSeasonMapper extends BaseMapper<WgArenaSeason> {

    /**
     * 查询当前活跃赛季
     * @return 活跃赛季
     */
    @Select("SELECT * FROM wg_arena_season WHERE is_active = 1 LIMIT 1")
    WgArenaSeason selectActiveSeason();

    /**
     * 根据赛季ID查询
     * @param seasonId 赛季ID
     * @return 赛季信息
     */
    @Select("SELECT * FROM wg_arena_season WHERE season_id = #{seasonId}")
    WgArenaSeason selectBySeasonId(@Param("seasonId") String seasonId);
}
