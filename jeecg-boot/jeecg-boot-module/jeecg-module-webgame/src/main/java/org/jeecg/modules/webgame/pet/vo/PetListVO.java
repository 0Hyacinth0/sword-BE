package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 战宠列表响应VO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class PetListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**战宠列表*/
    private List<PetInfoVO> pets;

    /**容量信息*/
    private PetCapacityVO capacity;

    /**
     * 容量信息
     */
    @Data
    public static class PetCapacityVO implements Serializable {
        private static final long serialVersionUID = 1L;

        /**最大容量*/
        private Integer max;

        /**当前数量*/
        private Integer current;
    }
}
