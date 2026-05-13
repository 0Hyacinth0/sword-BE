package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 战宠装备VO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class PetEquipmentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**护甲装备*/
    private EquipmentItem armor;

    /**饰品装备*/
    private EquipmentItem accessory;

    /**
     * 装备物品信息
     */
    @Data
    public static class EquipmentItem implements Serializable {
        private static final long serialVersionUID = 1L;

        /**装备ID*/
        private String id;

        /**装备名称*/
        private String name;

        /**稀有度*/
        private String rarity;

        /**槽位类型*/
        private String slotType;

        /**属性加成(JSON字符串)*/
        private String stats;

        /**强化等级*/
        private Integer enhanceLevel;
    }
}
