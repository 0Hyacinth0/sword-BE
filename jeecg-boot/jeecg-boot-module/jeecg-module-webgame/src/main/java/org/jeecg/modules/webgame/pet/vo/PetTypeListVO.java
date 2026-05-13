package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 战宠类型列表响应VO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class PetTypeListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**总数*/
    private Integer total;

    /**战宠类型列表*/
    private List<PetTypeConfigVO> types;
}
