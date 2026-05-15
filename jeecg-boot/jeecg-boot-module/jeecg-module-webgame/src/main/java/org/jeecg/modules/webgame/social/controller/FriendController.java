package org.jeecg.modules.webgame.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.social.dto.FriendRequestDTO;
import org.jeecg.modules.webgame.social.dto.HandleFriendRequestDTO;
import org.jeecg.modules.webgame.social.service.IFriendService;
import org.jeecg.modules.webgame.social.vo.FriendInfoVO;
import org.jeecg.modules.webgame.social.vo.FriendListVO;
import org.jeecg.modules.webgame.social.vo.SearchPlayerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 好友系统 Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@RestController
@RequestMapping("/webgame/social")
public class FriendController {
    
    @Autowired
    private IFriendService friendService;
    
    /**
     * 获取好友列表
     */
    @GetMapping("/friends")
    public Result<FriendListVO> getFriendList(HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("获取好友列表, characterId: {}", characterId);
        
        FriendListVO friendList = friendService.getFriendList(characterId);
        return Result.OK("获取成功", friendList);
    }
    
    /**
     * 搜索玩家
     */
    @GetMapping("/search")
    public Result<List<SearchPlayerVO>> searchPlayers(
            @RequestParam String keyword,
            HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("搜索玩家, keyword: {}, characterId: {}", keyword, characterId);
        
        List<SearchPlayerVO> results = friendService.searchPlayers(keyword, characterId);
        return Result.OK("搜索成功", results);
    }
    
    /**
     * 发送好友请求
     */
    @PostMapping("/request")
    public Result<String> sendFriendRequest(
            @Validated @RequestBody FriendRequestDTO dto,
            HttpServletRequest request) {
        String fromCharacterId = (String) request.getAttribute("characterId");
        log.info("发送好友请求, from: {}, to: {}", fromCharacterId, dto.getToCharacterId());
        
        String requestId = friendService.sendFriendRequest(fromCharacterId, dto);
        return Result.OK("好友请求已发送", requestId);
    }
    
    /**
     * 接受好友请求
     */
    @PostMapping("/accept")
    public Result<FriendInfoVO> acceptFriendRequest(
            @Validated @RequestBody HandleFriendRequestDTO dto,
            HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("接受好友请求, characterId: {}, requestId: {}", characterId, dto.getRequestId());
        
        FriendInfoVO friendInfo = friendService.acceptFriendRequest(characterId, dto);
        return Result.OK("好友请求已接受", friendInfo);
    }
    
    /**
     * 拒绝好友请求
     */
    @PostMapping("/reject")
    public Result<Void> rejectFriendRequest(
            @Validated @RequestBody HandleFriendRequestDTO dto,
            HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("拒绝好友请求, characterId: {}, requestId: {}", characterId, dto.getRequestId());
        
        friendService.rejectFriendRequest(characterId, dto);
        return Result.OK("好友请求已拒绝", null);
    }
    
    /**
     * 删除好友
     */
    @DeleteMapping("/remove")
    public Result<Void> removeFriend(
            @RequestParam String friendCharacterId,
            HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("删除好友, characterId: {}, friendCharacterId: {}", characterId, friendCharacterId);
        
        friendService.removeFriend(characterId, friendCharacterId);
        return Result.OK("好友已删除", null);
    }
    
    /**
     * 取消好友请求
     */
    @PostMapping("/cancel")
    public Result<Void> cancelFriendRequest(
            @Validated @RequestBody HandleFriendRequestDTO dto,
            HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("取消好友请求, characterId: {}, requestId: {}", characterId, dto.getRequestId());
        
        friendService.cancelFriendRequest(characterId, dto.getRequestId());
        return Result.OK("好友请求已取消", null);
    }
}
