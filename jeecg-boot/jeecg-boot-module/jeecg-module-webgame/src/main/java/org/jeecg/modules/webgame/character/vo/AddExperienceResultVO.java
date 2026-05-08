package org.jeecg.modules.webgame.character.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 增加经验响应VO
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddExperienceResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**更新后的角色数据*/
    private CharacterVO character;
    
    /**升级结果（未升级时为null）*/
    private LevelUpResultVO levelUp;
}
