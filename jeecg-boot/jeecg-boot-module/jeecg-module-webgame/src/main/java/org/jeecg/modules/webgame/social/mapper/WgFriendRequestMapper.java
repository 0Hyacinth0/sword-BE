package org.jeecg.modules.webgame.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.webgame.social.entity.WgFriendRequest;

import java.util.List;

/**
 * @Description: 好友请求 Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Mapper
public interface WgFriendRequestMapper extends BaseMapper<WgFriendRequest> {
    
    /**
     * 查询接收者的待处理请�?     * @param toCharacterId 接收者角色ID
     * @return 待处理请求列�?     */
    List<WgFriendRequest> selectPendingRequestsByReceiver(@Param("toCharacterId") String toCharacterId);
    
    /**
     * 查询发送者的已发送请�?     * @param fromCharacterId 发送者角色ID
     * @return 已发送请求列�?     */
    List<WgFriendRequest> selectSentRequestsBySender(@Param("fromCharacterId") String fromCharacterId);
}
