package org.jeecg.modules.webgame.pet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.pet.entity.PetEvolveItem;

import java.util.List;

/**
 * @Description: 进化材料配置Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Mapper
public interface PetEvolveItemMapper extends BaseMapper<PetEvolveItem> {

    /**
     * 查询指定战宠类型的进化材料
     * @param petTypeId 战宠类型ID
     * @return 进化材料列表
     */
    @Select("SELECT * FROM wg_pet_evolve_items WHERE pet_type_id = #{petTypeId}")
    List<PetEvolveItem> selectByPetTypeId(@Param("petTypeId") Integer petTypeId);
}
