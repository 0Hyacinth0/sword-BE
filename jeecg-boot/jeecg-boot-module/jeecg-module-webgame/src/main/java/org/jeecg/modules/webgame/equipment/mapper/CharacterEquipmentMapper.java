package org.jeecg.modules.webgame.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.equipment.entity.CharacterEquipment;

import java.util.List;

/**
 * @Description: 角色装备Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Mapper
public interface CharacterEquipmentMapper extends BaseMapper<CharacterEquipment> {

    /**
     * 查询角色的所有装备
     * @param characterId 角色ID
     * @return 装备列表
     */
    @Select("SELECT * FROM wg_character_equipments WHERE character_id = #{characterId}")
    List<CharacterEquipment> selectByCharacterId(@Param("characterId") String characterId);

    /**
     * 查询角色某个槽位的装备
     * @param characterId 角色ID
     * @param slotType 槽位类型
     * @return 装备记录
     */
    @Select("SELECT * FROM wg_character_equipments WHERE character_id = #{characterId} AND slot_type = #{slotType}")
    CharacterEquipment selectByCharacterAndSlot(@Param("characterId") String characterId, @Param("slotType") String slotType);
}
