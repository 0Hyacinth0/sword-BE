package org.jeecg.modules.webgame.map.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.map.entity.WgAreaMonster;

import java.util.List;

/**
 * @Description: 区域怪物Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Mapper
public interface WgAreaMonsterMapper extends BaseMapper<WgAreaMonster> {
    
    /**
     * 根据区域ID查询怪物列表
     */
    @Select("SELECT * FROM wg_area_monsters WHERE area_id = #{areaId} ORDER BY sort_order ASC")
    List<WgAreaMonster> selectByAreaId(String areaId);
}
