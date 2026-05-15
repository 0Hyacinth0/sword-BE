package org.jeecg.modules.webgame.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.social.dto.*;
import org.jeecg.modules.webgame.social.service.ITeamService;
import org.jeecg.modules.webgame.social.vo.TeamApplicationVO;
import org.jeecg.modules.webgame.social.vo.TeamInfoVO;
import org.jeecg.modules.webgame.social.vo.TeamMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 组队系统 Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@RestController
@RequestMapping("/webgame/team")
public class TeamController {
    
    @Autowired
    private ITeamService teamService;
    
    /**
     * 创建队伍
     */
    @PostMapping("/create")
    public Result<TeamInfoVO> createTeam(HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("创建队伍, characterId: {}", characterId);
        
        TeamInfoVO team = teamService.createTeam(characterId);
        return Result.OK("队伍创建成功", team);
    }
    
    /**
     * 获取公开队伍列表
     */
    @GetMapping("/list")
    public Result<List<TeamInfoVO>> getPublicTeams() {
        log.info("获取公开队伍列表");
        
        List<TeamInfoVO> teams = teamService.getPublicTeams();
        return Result.OK("获取成功", teams);
    }
    
    /**
     * 获取我的队伍
     */
    @GetMapping("/my")
    public Result<TeamInfoVO> getMyTeam(HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("获取我的队伍, characterId: {}", characterId);
        
        TeamInfoVO team = teamService.getMyTeam(characterId);
        return Result.OK("获取成功", team);
    }
    
    /**
     * 邀请好友
     */
    @PostMapping("/invite")
    public Result<TeamMemberVO> inviteMember(
            @Validated @RequestBody InviteDTO dto,
            HttpServletRequest request) {
        String leaderId = (String) request.getAttribute("characterId");
        log.info("邀请好友, leaderId: {}, targetId: {}", leaderId, dto.getCharacterId());
        
        TeamMemberVO member = teamService.inviteMember(leaderId, dto);
        return Result.OK("邀请已发送", member);
    }
    
    /**
     * 申请加入队伍
     */
    @PostMapping("/apply")
    public Result<TeamApplicationVO> applyTeam(
            @Validated @RequestBody ApplyTeamDTO dto,
            HttpServletRequest request) {
        String applicantId = (String) request.getAttribute("characterId");
        log.info("申请加入队伍, applicantId: {}, teamId: {}", applicantId, dto.getTeamId());
        
        TeamApplicationVO application = teamService.applyTeam(applicantId, dto);
        return Result.OK("申请已提交", application);
    }
    
    /**
     * 获取入队申请列表
     */
    @GetMapping("/applications/{teamId}")
    public Result<List<TeamApplicationVO>> getApplications(
            @PathVariable String teamId,
            HttpServletRequest request) {
        String leaderId = (String) request.getAttribute("characterId");
        log.info("获取入队申请列表, teamId: {}, leaderId: {}", teamId, leaderId);
        
        List<TeamApplicationVO> applications = teamService.getApplications(teamId, leaderId);
        return Result.OK("获取成功", applications);
    }
    
    /**
     * 接受入队申请
     */
    @PostMapping("/accept")
    public Result<TeamMemberVO> acceptApplication(
            @Validated @RequestBody HandleApplicationDTO dto,
            HttpServletRequest request) {
        String leaderId = (String) request.getAttribute("characterId");
        log.info("接受入队申请, leaderId: {}, applicationId: {}", leaderId, dto.getId());
        
        TeamMemberVO member = teamService.acceptApplication(leaderId, dto);
        return Result.OK("已接受申请", member);
    }
    
    /**
     * 拒绝入队申请
     */
    @PostMapping("/reject")
    public Result<Void> rejectApplication(
            @Validated @RequestBody HandleApplicationDTO dto,
            HttpServletRequest request) {
        String leaderId = (String) request.getAttribute("characterId");
        log.info("拒绝入队申请, leaderId: {}, applicationId: {}", leaderId, dto.getId());
        
        teamService.rejectApplication(leaderId, dto);
        return Result.OK("已拒绝申请", null);
    }
    
    /**
     * 踢出队伍成员
     */
    @DeleteMapping("/kick/{characterId}")
    public Result<Void> kickMember(
            @PathVariable String characterId,
            HttpServletRequest request) {
        String leaderId = (String) request.getAttribute("characterId");
        log.info("踢出成员, leaderId: {}, targetId: {}", leaderId, characterId);
        
        teamService.kickMember(leaderId, characterId);
        return Result.OK("已踢出成员", null);
    }
    
    /**
     * 离开队伍
     */
    @PostMapping("/leave")
    public Result<Void> leaveTeam(HttpServletRequest request) {
        String characterId = (String) request.getAttribute("characterId");
        log.info("离开队伍, characterId: {}", characterId);
        
        teamService.leaveTeam(characterId);
        return Result.OK("已离开队伍", null);
    }
    
    /**
     * 解散队伍
     */
    @DeleteMapping("/disband")
    public Result<Void> disbandTeam(HttpServletRequest request) {
        String leaderId = (String) request.getAttribute("characterId");
        log.info("解散队伍, leaderId: {}", leaderId);
        
        teamService.disbandTeam(leaderId);
        return Result.OK("队伍已解散", null);
    }
    
    /**
     * 转让队长
     */
    @PostMapping("/change-leader")
    public Result<TeamInfoVO> changeLeader(
            @Validated @RequestBody ChangeLeaderDTO dto,
            HttpServletRequest request) {
        String leaderId = (String) request.getAttribute("characterId");
        log.info("转让队长, leaderId: {}, newLeaderId: {}", leaderId, dto.getCharacterId());
        
        TeamInfoVO team = teamService.changeLeader(leaderId, dto);
        return Result.OK("队长已转让", team);
    }
    
    /**
     * 切换队伍开放状态
     */
    @PostMapping("/status")
    public Result<TeamInfoVO> changeStatus(
            @Validated @RequestBody ChangeStatusDTO dto,
            HttpServletRequest request) {
        String leaderId = (String) request.getAttribute("characterId");
        log.info("切换队伍状态, leaderId: {}, status: {}", leaderId, dto.getStatus());
        
        TeamInfoVO team = teamService.changeStatus(leaderId, dto);
        return Result.OK("状态已更新", team);
    }
}
