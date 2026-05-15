package org.jeecg.modules.webgame.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.webgame.social.entity.WgChatMessage;

import java.util.List;

/**
 * @Description: 聊天消息 Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Mapper
public interface WgChatMessageMapper extends BaseMapper<WgChatMessage> {
    
    /**
     * 查询世界频道消息（最近N条）
     * @param limit 消息条数
     * @return 消息列表
     */
    List<WgChatMessage> selectWorldMessages(@Param("limit") Integer limit);
    
    /**
     * 查询私聊消息记录
     * @param user1Id 用户1 ID
     * @param user2Id 用户2 ID
     * @param limit 消息条数
     * @return 消息列表
     */
    List<WgChatMessage> selectPrivateMessages(
            @Param("user1Id") String user1Id,
            @Param("user2Id") String user2Id,
            @Param("limit") Integer limit);
}
