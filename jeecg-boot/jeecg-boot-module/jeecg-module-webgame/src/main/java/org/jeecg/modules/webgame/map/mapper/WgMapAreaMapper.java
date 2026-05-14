package org.jeecg.modules.webgame.map.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.map.entity.WgMapArea;

import java.util.List;

/**
 * @Description: 地图区域Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Mapper
public interface WgMapAreaMapper extends BaseMapper<WgMapArea> {
    
    /**
     * 查询所有区域（按排序顺序）
     */
    @Select("SELECT * FROM wg_map_areas ORDER BY sort_order ASC")
    List<WgMapArea> selectAllAreas();
}
