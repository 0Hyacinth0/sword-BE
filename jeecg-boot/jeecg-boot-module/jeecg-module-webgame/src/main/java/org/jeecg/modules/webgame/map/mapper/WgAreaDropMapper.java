package org.jeecg.modules.webgame.map.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.map.entity.WgAreaDrop;

import java.util.List;

/**
 * @Description: 区域掉落Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Mapper
public interface WgAreaDropMapper extends BaseMapper<WgAreaDrop> {
    
    /**
     * 根据区域ID查询掉落列表
     */
    @Select("SELECT * FROM wg_area_drops WHERE area_id = #{areaId}")
    List<WgAreaDrop> selectByAreaId(String areaId);
}
