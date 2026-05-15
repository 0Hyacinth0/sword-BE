package org.jeecg.modules.webgame.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.webgame.social.entity.WgChatUnread;

/**
 * @Description: 未读消息计数 Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Mapper
public interface WgChatUnreadMapper extends BaseMapper<WgChatUnread> {
    
    /**
     * 查询会话中角色的未读�?     * @param conversationId 会话ID
     * @param characterId 角色ID
     * @return 未读消息对象
     */
    WgChatUnread selectByConversationAndCharacter(
            @Param("conversationId") String conversationId,
            @Param("characterId") String characterId);
}
