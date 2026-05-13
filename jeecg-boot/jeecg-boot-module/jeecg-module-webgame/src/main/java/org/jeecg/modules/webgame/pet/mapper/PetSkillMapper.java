package org.jeecg.modules.webgame.pet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.pet.entity.PetSkill;

import java.util.List;

/**
 * @Description: 战宠技能配置Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Mapper
public interface PetSkillMapper extends BaseMapper<PetSkill> {

    /**
     * 查询所有战宠技能
     * @return 战宠技能列表
     */
    @Select("SELECT * FROM wg_pet_skills ORDER BY skill_id")
    List<PetSkill> selectAllPetSkills();

    /**
     * 根据ID查询战宠技能
     * @param skillId 技能ID
     * @return 战宠技能
     */
    @Select("SELECT * FROM wg_pet_skills WHERE skill_id = #{skillId}")
    PetSkill selectBySkillId(@Param("skillId") Integer skillId);
}
