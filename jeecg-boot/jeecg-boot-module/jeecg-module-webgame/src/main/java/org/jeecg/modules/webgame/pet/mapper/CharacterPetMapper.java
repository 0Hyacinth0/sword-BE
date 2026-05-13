package org.jeecg.modules.webgame.pet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.webgame.pet.entity.CharacterPet;

import java.util.List;

/**
 * @Description: 角色战宠实例Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Mapper
public interface CharacterPetMapper extends BaseMapper<CharacterPet> {

    /**
     * 查询角色的所有战宠
     * @param characterId 角色ID
     * @return 战宠列表
     */
    @Select("SELECT * FROM wg_character_pets WHERE character_id = #{characterId} ORDER BY is_active DESC, id")
    List<CharacterPet> selectByCharacterId(@Param("characterId") String characterId);

    /**
     * 根据ID查询战宠
     * @param petId 战宠实例ID
     * @return 战宠实例
     */
    @Select("SELECT * FROM wg_character_pets WHERE id = #{petId}")
    CharacterPet selectByPetId(@Param("petId") String petId);

    /**
     * 查询角色的出战战宠
     * @param characterId 角色ID
     * @return 出战战宠
     */
    @Select("SELECT * FROM wg_character_pets WHERE character_id = #{characterId} AND is_active = 1 LIMIT 1")
    CharacterPet selectActivePet(@Param("characterId") String characterId);

    /**
     * 取消角色所有战宠的出战状态
     * @param characterId 角色ID
     */
    @Update("UPDATE wg_character_pets SET is_active = 0 WHERE character_id = #{characterId}")
    void clearActiveStatus(@Param("characterId") String characterId);

    /**
     * 设置战宠为出战状态
     * @param petId 战宠实例ID
     */
    @Update("UPDATE wg_character_pets SET is_active = 1 WHERE id = #{petId}")
    void setActiveStatus(@Param("petId") String petId);

    /**
     * 统计角色的战宠数量
     * @param characterId 角色ID
     * @return 战宠数量
     */
    @Select("SELECT COUNT(*) FROM wg_character_pets WHERE character_id = #{characterId}")
    Integer countByCharacterId(@Param("characterId") String characterId);
}
