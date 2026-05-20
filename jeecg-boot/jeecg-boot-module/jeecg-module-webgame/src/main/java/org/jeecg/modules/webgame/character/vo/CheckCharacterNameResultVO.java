package org.jeecg.modules.webgame.character.vo;

import lombok.Data;

/**
 * @Description: 角色名称检查结果VO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class CheckCharacterNameResultVO {

    /** 是否可用 */
    private Boolean available;

    /** 提示信息 */
    private String message;
}
