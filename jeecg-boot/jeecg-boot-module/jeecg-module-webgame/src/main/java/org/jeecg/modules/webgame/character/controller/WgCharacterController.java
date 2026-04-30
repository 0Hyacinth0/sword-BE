package org.jeecg.modules.webgame.character.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.webgame.character.dto.AttributePointDTO;
import org.jeecg.modules.webgame.character.dto.CreateCharacterDTO;
import org.jeecg.modules.webgame.character.service.IWgCharacterService;
import org.jeecg.modules.webgame.character.vo.CharacterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 游戏角色控制器
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Slf4j
@Tag(name = "游戏角色接口")
@RestController
@RequestMapping("/webgame/character")
public class WgCharacterController {

    @Autowired
    private IWgCharacterService wgCharacterService;

    /**
     * 创建角色
     * @param request HTTP请求
     * @param createDTO 创建参数
     * @return 角色信息
     */
    @Operation(summary = "创建角色")
    @PostMapping("/create")
    public Result<CharacterVO> createCharacter(HttpServletRequest request, 
                                               @Validated @RequestBody CreateCharacterDTO createDTO) {
        try {
            // 从 Token 中获取用户ID
            String token = request.getHeader("X-Access-Token");
            String userId = getUserIdFromToken(token);
            
            CharacterVO characterVO = wgCharacterService.createCharacter(userId, createDTO);
            return Result.OK("角色创建成功", characterVO);
        } catch (Exception e) {
            log.error("创建角色失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取角色信息
     * @param characterId 角色ID
     * @return 角色信息
     */
    @Operation(summary = "获取角色信息")
    @GetMapping("/info/{characterId}")
    public Result<CharacterVO> getCharacterInfo(@PathVariable("characterId") String characterId) {
        try {
            CharacterVO characterVO = wgCharacterService.getCharacterInfo(characterId);
            return Result.OK(characterVO);
        } catch (Exception e) {
            log.error("获取角色信息失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 属性加点
     * @param characterId 角色ID
     * @param pointDTO 加点参数
     * @return 更新后的角色信息
     */
    @Operation(summary = "属性加点")
    @PostMapping("/add-points/{characterId}")
    public Result<CharacterVO> addAttributePoints(@PathVariable("characterId") String characterId,
                                                   @Validated @RequestBody AttributePointDTO pointDTO) {
        try {
            CharacterVO characterVO = wgCharacterService.addAttributePoints(characterId, pointDTO);
            return Result.OK("加点成功", characterVO);
        } catch (Exception e) {
            log.error("属性加点失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取角色列表
     * @param request HTTP请求
     * @return 角色列表
     */
    @Operation(summary = "获取角色列表")
    @GetMapping("/list")
    public Result<List<CharacterVO>> getCharacterList(HttpServletRequest request) {
        try {
            // 从 Token 中获取用户ID
            String token = request.getHeader("X-Access-Token");
            log.info("收到的 Token: {}", token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null");
            
            // 打印所有 Header 用于调试
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                log.info("Header: {} = {}", headerName, request.getHeader(headerName));
            }
            
            String userId = getUserIdFromToken(token);
            
            List<CharacterVO> characterList = wgCharacterService.getCharacterList(userId);
            return Result.OK(characterList);
        } catch (Exception e) {
            log.error("获取角色列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除角色
     * @param request HTTP请求
     * @param characterId 角色ID
     * @return 删除结果
     */
    @Operation(summary = "删除角色")
    @DeleteMapping("/delete/{characterId}")
    public Result<Void> deleteCharacter(HttpServletRequest request,
                                        @PathVariable("characterId") String characterId) {
        try {
            // 从 Token 中获取用户ID
            String token = request.getHeader("X-Access-Token");
            String userId = getUserIdFromToken(token);
            
            wgCharacterService.deleteCharacter(characterId, userId);
            return Result.OK("角色删除成功");
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 从 Token 中解析用户ID
     * 注：这里简化处理，实际应该调用系统的用户服务获取完整用户信息
     */
    private String getUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("未登录，请先登录");
        }
        
        try {
            // 从 Token 中解析用户名
            String username = JwtUtil.getUsername(token);
            
            // TODO: 实际项目中应该调用系统用户服务，通过用户名查询用户ID
            // 这里简化处理，直接使用用户名作为 userId
            // 真实场景：LoginUser loginUser = commonApi.getUserByName(username);
            //         return loginUser.getId();
            
            return username;  // 简化处理
        } catch (Exception e) {
            throw new RuntimeException("Token 无效，请重新登录");
        }
    }
}
