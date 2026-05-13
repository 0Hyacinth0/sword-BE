package org.jeecg.modules.webgame.pet.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 已拥有战宠类型ID列表响应VO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class OwnedPetTypesVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**已拥有的战宠类型ID列表*/
    private List<Integer> ownedTypeIds;
}
