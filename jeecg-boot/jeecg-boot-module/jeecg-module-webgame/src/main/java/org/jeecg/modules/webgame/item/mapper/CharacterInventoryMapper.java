package org.jeecg.modules.webgame.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.webgame.item.entity.CharacterInventory;

import java.util.List;

/**
 * @Description: 角色背包Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
public interface CharacterInventoryMapper extends BaseMapper<CharacterInventory> {

    /**
     * 查询角色的背包物品列表（JOIN物品模板表）
     * @param characterId 角色ID
     * @return 背包物品列表
     */
    @Select("SELECT ci.id, ci.character_id, ci.item_id, ci.quantity, ci.obtained_at, " +
            "it.item_name as name, it.category, it.rarity, it.description, it.icon_url, " +
            "it.max_stack, it.sell_price, it.effect_type, it.effect_value, it.source, it.usage " +
            "FROM character_inventory ci " +
            "LEFT JOIN wg_item_template it ON ci.item_id = it.item_id " +
            "WHERE ci.character_id = #{characterId} " +
            "ORDER BY ci.obtained_at DESC")
    List<org.jeecg.modules.webgame.item.vo.InventoryItemVO> selectInventoryWithItems(@Param("characterId") String characterId);

    /**
     * 查询角色是否已有某个物品
     * @param characterId 角色ID
     * @param itemId 物品模板ID
     * @return 背包记录
     */
    @Select("SELECT * FROM character_inventory WHERE character_id = #{characterId} AND item_id = #{itemId}")
    CharacterInventory selectByCharacterAndItem(@Param("characterId") String characterId, @Param("itemId") Integer itemId);

    /**
     * 更新物品数量
     * @param id 背包记录ID
     * @param quantity 新数量
     * @return 影响行数
     */
    @Update("UPDATE character_inventory SET quantity = #{quantity}, update_time = NOW() WHERE id = #{id}")
    int updateQuantity(@Param("id") String id, @Param("quantity") Integer quantity);

    /**
     * 统计角色的物品种类数
     * @param characterId 角色ID
     * @return 物品种类数
     */
    @Select("SELECT COUNT(*) FROM character_inventory WHERE character_id = #{characterId}")
    int countByCharacterId(@Param("characterId") String characterId);
}
