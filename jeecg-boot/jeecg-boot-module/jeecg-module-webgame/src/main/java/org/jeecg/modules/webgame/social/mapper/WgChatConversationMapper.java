package org.jeecg.modules.webgame.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.webgame.social.entity.WgChatConversation;

import java.util.List;

/**
 * @Description: 私聊会话 Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Mapper
public interface WgChatConversationMapper extends BaseMapper<WgChatConversation> {
    
    /**
     * 查询角色的私聊会话列�?     * @param characterId 角色ID
     * @return 会话列表
     */
    List<WgChatConversation> selectConversationsByCharacter(@Param("characterId") String characterId);
}
