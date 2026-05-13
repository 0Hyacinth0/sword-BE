package org.jeecg.modules.webgame.pet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.pet.entity.PetType;

import java.util.List;

/**
 * @Description: 战宠类型配置Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Mapper
public interface PetTypeMapper extends BaseMapper<PetType> {

    /**
     * 查询所有战宠类型
     * @return 战宠类型列表
     */
    @Select("SELECT * FROM wg_pet_types ORDER BY pet_type_id")
    List<PetType> selectAllPetTypes();

    /**
     * 根据ID查询战宠类型
     * @param petTypeId 战宠类型ID
     * @return 战宠类型
     */
    @Select("SELECT * FROM wg_pet_types WHERE pet_type_id = #{petTypeId}")
    PetType selectByPetTypeId(@Param("petTypeId") Integer petTypeId);

    /**
     * 查询可进化到指定类型的战宠类型
     * @param evolveTo 进化目标类型ID
     * @return 战宠类型列表
     */
    @Select("SELECT * FROM wg_pet_types WHERE evolve_to = #{evolveTo}")
    List<PetType> selectByEvolveTo(@Param("evolveTo") Integer evolveTo);
}
