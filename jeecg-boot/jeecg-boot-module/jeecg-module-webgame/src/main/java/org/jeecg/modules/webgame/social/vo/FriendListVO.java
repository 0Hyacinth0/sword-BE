package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description: 好友列表响应 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class FriendListVO {
    
    /**好友列表*/
    private List<FriendInfoVO> friends;
    
    /**待处理请求列表*/
    private List<FriendRequestVO> pendingRequests;
    
    /**已发送请求列表*/
    private List<FriendRequestVO> sentRequests;
}
