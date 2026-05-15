package org.jeecg.modules.webgame.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.social.service.IDungeonRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @Description: 多人副本大厅 Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@RestController
@RequestMapping("/webgame/dungeon-room")
public class DungeonRoomController {
    
    @Autowired
    private IDungeonRoomService dungeonRoomService;
    
    /**
     * 创建副本房间
     */
    @PostMapping("/create")
    public Result<Object> createDungeonRoom(
            @RequestParam String teamId,
            @RequestParam String dungeonId,
            HttpServletRequest request) {
        
        String leaderId = (String) request.getAttribute("characterId");
        log.info("创建副本房间, leaderId: {}, teamId: {}, dungeonId: {}", leaderId, teamId, dungeonId);
        
        Object room = dungeonRoomService.createDungeonRoom(leaderId, teamId, dungeonId);
        return Result.OK("房间创建成功", room);
    }
    
    /**
     * 获取房间信息
     */
    @GetMapping("/{roomId}")
    public Result<Object> getDungeonRoom(@PathVariable String roomId) {
        log.info("获取房间信息, roomId: {}", roomId);
        
        Object room = dungeonRoomService.getDungeonRoom(roomId);
        return Result.OK("获取成功", room);
    }
    
    /**
     * 切换准备状态
     */
    @PostMapping("/ready")
    public Result<Object> toggleReady(
            @RequestParam String roomId,
            HttpServletRequest request) {
        
        String characterId = (String) request.getAttribute("characterId");
        log.info("切换准备状态, characterId: {}, roomId: {}", characterId, roomId);
        
        Object room = dungeonRoomService.toggleReady(characterId, roomId);
        return Result.OK("状态已更新", room);
    }
    
    /**
     * 开始挑战
     */
    @PostMapping("/start")
    public Result<Object> startChallenge(
            @RequestParam String roomId,
            HttpServletRequest request) {
        
        String leaderId = (String) request.getAttribute("characterId");
        log.info("开始挑战, leaderId: {}, roomId: {}", leaderId, roomId);
        
        Object battleData = dungeonRoomService.startChallenge(leaderId, roomId);
        return Result.OK("挑战开始", battleData);
    }
    
    /**
     * 离开房间
     */
    @PostMapping("/leave")
    public Result<Void> leaveRoom(
            @RequestParam String roomId,
            HttpServletRequest request) {
        
        String characterId = (String) request.getAttribute("characterId");
        log.info("离开房间, characterId: {}, roomId: {}", characterId, roomId);
        
        dungeonRoomService.leaveRoom(characterId, roomId);
        return Result.OK("已离开房间", null);
    }
    
    /**
     * 取消房间
     */
    @DeleteMapping("/cancel/{roomId}")
    public Result<Void> cancelRoom(
            @PathVariable String roomId,
            HttpServletRequest request) {
        
        String leaderId = (String) request.getAttribute("characterId");
        log.info("取消房间, leaderId: {}, roomId: {}", leaderId, roomId);
        
        dungeonRoomService.cancelRoom(leaderId, roomId);
        return Result.OK("房间已取消", null);
    }
}
