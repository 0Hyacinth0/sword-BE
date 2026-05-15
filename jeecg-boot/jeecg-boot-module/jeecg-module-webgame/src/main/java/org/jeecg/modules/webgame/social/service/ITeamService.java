package org.jeecg.modules.webgame.social.service;

import org.jeecg.modules.webgame.social.dto.*;
import org.jeecg.modules.webgame.social.vo.TeamApplicationVO;
import org.jeecg.modules.webgame.social.vo.TeamInfoVO;
import org.jeecg.modules.webgame.social.vo.TeamMemberVO;

import java.util.List;

/**
 * @Description: 组队系统 Service
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
public interface ITeamService {
    
    /**
     * 创建队伍
     * @param characterId 创建者角色ID
     * @return 队伍信息
     */
    TeamInfoVO createTeam(String characterId);
    
    /**
     * 获取公开队伍列表
     * @return 队伍列表
     */
    List<TeamInfoVO> getPublicTeams();
    
    /**
     * 获取我的队伍
     * @param characterId 角色ID
     * @return 队伍信息
     */
    TeamInfoVO getMyTeam(String characterId);
    
    /**
     * 邀请好友
     * @param leaderId 队长角色ID
     * @param dto 邀请参数
     * @return 被邀请成员信息
     */
    TeamMemberVO inviteMember(String leaderId, InviteDTO dto);
    
    /**
     * 申请加入队伍
     * @param applicantId 申请者角色ID
     * @param dto 申请参数
     * @return 申请信息
     */
    TeamApplicationVO applyTeam(String applicantId, ApplyTeamDTO dto);
    
    /**
     * 获取队伍的待处理申请
     * @param teamId 队伍ID
     * @param leaderId 队长角色ID
     * @return 申请列表
     */
    List<TeamApplicationVO> getApplications(String teamId, String leaderId);
    
    /**
     * 接受申请
     * @param leaderId 队长角色ID
     * @param dto 申请ID
     * @return 新成员信息
     */
    TeamMemberVO acceptApplication(String leaderId, HandleApplicationDTO dto);
    
    /**
     * 拒绝申请
     * @param leaderId 队长角色ID
     * @param dto 申请ID
     */
    void rejectApplication(String leaderId, HandleApplicationDTO dto);
    
    /**
     * 踢出成员
     * @param leaderId 队长角色ID
     * @param characterId 被踢出成员ID
     */
    void kickMember(String leaderId, String characterId);
    
    /**
     * 离开队伍
     * @param characterId 角色ID
     */
    void leaveTeam(String characterId);
    
    /**
     * 解散队伍
     * @param leaderId 队长角色ID
     */
    void disbandTeam(String leaderId);
    
    /**
     * 转让队长
     * @param leaderId 当前队长ID
     * @param dto 新队长ID
     * @return 更新后的队伍信息
     */
    TeamInfoVO changeLeader(String leaderId, ChangeLeaderDTO dto);
    
    /**
     * 切换队伍状态
     * @param leaderId 队长角色ID
     * @param dto 新状态
     * @return 更新后的队伍信息
     */
    TeamInfoVO changeStatus(String leaderId, ChangeStatusDTO dto);
}
