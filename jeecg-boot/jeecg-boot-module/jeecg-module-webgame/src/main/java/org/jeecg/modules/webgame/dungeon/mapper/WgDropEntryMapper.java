package org.jeecg.modules.webgame.dungeon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.dungeon.entity.WgDropEntry;

import java.util.List;

/**
 * @Description: 掉落条目Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Mapper
public interface WgDropEntryMapper extends BaseMapper<WgDropEntry> {
    
    /**
     * 根据副本ID和楼层类型查询掉落条目
     */
    @Select("SELECT * FROM wg_drop_entries WHERE dungeon_id = #{dungeonId} AND floor_type = #{floorType}")
    List<WgDropEntry> selectByDungeonAndFloorType(@Param("dungeonId") String dungeonId, 
                                                   @Param("floorType") String floorType);
}
