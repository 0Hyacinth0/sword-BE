package org.jeecg.modules.webgame.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.battle.entity.WgLevelConfig;

/**
 * @Description: 等级配置Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Mapper
public interface WgLevelConfigMapper extends BaseMapper<WgLevelConfig> {

    /**
     * 根据等级查询配置
     * @param level 等级
     * @return 等级配置
     */
    @Select("SELECT * FROM wg_level_config WHERE level = #{level}")
    WgLevelConfig selectByLevel(@Param("level") Integer level);

    /**
     * 查询下一级所需经验
     * @param level 当前等级
     * @return 下一级配置
     */
    @Select("SELECT * FROM wg_level_config WHERE level = #{level} + 1")
    WgLevelConfig selectNextLevel(@Param("level") Integer level);
}
