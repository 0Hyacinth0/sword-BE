package org.jeecg.modules.webgame.dungeon.service;

import org.jeecg.modules.webgame.dungeon.dto.EliteDungeonConfigDTO;

/**
 * @Description: 精英副本Service
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
public interface IEliteDungeonService {
    
    /**
     * 获取精英副本配置
     * @param dungeonId 副本ID
     * @return 精英副本配置
     */
    EliteDungeonConfigDTO getEliteConfig(String dungeonId);
}
