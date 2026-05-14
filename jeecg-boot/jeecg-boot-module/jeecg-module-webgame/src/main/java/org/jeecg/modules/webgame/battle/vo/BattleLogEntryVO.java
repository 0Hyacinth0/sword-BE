package org.jeecg.modules.webgame.battle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @Description: 战斗日志条目VO
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Data
public class BattleLogEntryVO {

    /** 回合数 */
    private Integer round;

    /** 日志类型: system/action/damage/critical/dodge/miss/heal/buff/death/flee/reward */
    private String type;

    /** 日志内容 */
    private String message;

    /** 行动者UID */
    private String actorUid;

    /** 目标UID */
    private String targetUid;

    /** 时间戳 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Long timestamp;
}
