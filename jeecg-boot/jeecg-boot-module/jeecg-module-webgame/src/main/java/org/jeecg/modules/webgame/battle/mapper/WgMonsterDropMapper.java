package org.jeecg.modules.webgame.battle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.battle.entity.WgMonsterDrop;

import java.util.List;

/**
 * @Description: 怪物掉落配置Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Mapper
public interface WgMonsterDropMapper extends BaseMapper<WgMonsterDrop> {

    /**
     * 根据怪物ID查询掉落物品列表
     * @param monsterId 怪物ID
     * @return 掉落物品列表
     */
    @Select("SELECT * FROM wg_monster_drops WHERE monster_id = #{monsterId} ORDER BY drop_weight DESC")
    List<WgMonsterDrop> selectDropsByMonsterId(@Param("monsterId") String monsterId);
}
