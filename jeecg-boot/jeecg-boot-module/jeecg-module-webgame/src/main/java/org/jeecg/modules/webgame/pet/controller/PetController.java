package org.jeecg.modules.webgame.pet.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.common.controller.BaseWebGameController;
import org.jeecg.modules.webgame.pet.dto.*;
import org.jeecg.modules.webgame.pet.service.IPetService;
import org.jeecg.modules.webgame.pet.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 战宠系统Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Slf4j
@RestController
@RequestMapping("/webgame/pet")
public class PetController extends BaseWebGameController {

    @Autowired
    private IPetService petService;

    /**
     * 获取角色的战宠列表
     * @param request HTTP请求
     * @param characterId 角色ID
     * @return 战宠列表
     */
    @GetMapping("/list/{characterId}")
    public Result<PetListVO> getPetList(HttpServletRequest request, @PathVariable String characterId) {
        try {
            String userId = getCurrentUserId(request);
            PetListVO result = petService.getPetList(characterId, userId);
            return Result.OK("获取成功", result);
        } catch (Exception e) {
            log.error("获取战宠列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 设置出战战宠
     * @param request HTTP请求
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    @PostMapping("/set-active")
    public Result<PetListVO> setActivePet(HttpServletRequest request, @RequestBody @Validated SetActivePetDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            PetListVO result = petService.setActivePet(dto);
            return Result.OK("设置成功", result);
        } catch (Exception e) {
            log.error("设置出战战宠失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 喂食战宠
     * @param request HTTP请求
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    @PostMapping("/feed")
    public Result<PetListVO> feedPet(HttpServletRequest request, @RequestBody @Validated FeedPetDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            PetListVO result = petService.feedPet(dto);
            return Result.OK("喂食成功", result);
        } catch (Exception e) {
            log.error("喂食战宠失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 进化战宠
     * @param request HTTP请求
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    @PostMapping("/evolve")
    public Result<PetListVO> evolvePet(HttpServletRequest request, @RequestBody @Validated EvolvePetDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            PetListVO result = petService.evolvePet(dto);
            return Result.OK("进化成功", result);
        } catch (Exception e) {
            log.error("进化战宠失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 重命名战宠
     * @param request HTTP请求
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    @PostMapping("/rename")
    public Result<PetListVO> renamePet(HttpServletRequest request, @RequestBody @Validated RenamePetDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            PetListVO result = petService.renamePet(dto);
            return Result.OK("重命名成功", result);
        } catch (Exception e) {
            log.error("重命名战宠失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取战宠详情
     * @param request HTTP请求
     * @param petId 战宠实例ID
     * @return 战宠详情
     */
    @GetMapping("/detail/{petId}")
    public Result<PetInfoVO> getPetDetail(HttpServletRequest request, @PathVariable String petId) {
        try {
            String userId = getCurrentUserId(request);
            PetInfoVO result = petService.getPetDetail(petId, userId);
            return Result.OK("获取成功", result);
        } catch (Exception e) {
            log.error("获取战宠详情失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有战宠类型（图鉴用）
     * @return 战宠类型列表
     */
    @GetMapping("/types")
    public Result<PetTypeListVO> getAllPetTypes() {
        try {
            PetTypeListVO result = petService.getAllPetTypes();
            return Result.OK("获取成功", result);
        } catch (Exception e) {
            log.error("获取战宠类型列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取角色已拥有的战宠类型ID列表
     * @param request HTTP请求
     * @param characterId 角色ID
     * @return 已拥有的战宠类型ID列表
     */
    @GetMapping("/owned-types/{characterId}")
    public Result<OwnedPetTypesVO> getOwnedPetTypes(HttpServletRequest request, @PathVariable String characterId) {
        try {
            String userId = getCurrentUserId(request);
            OwnedPetTypesVO result = petService.getOwnedPetTypes(characterId, userId);
            return Result.OK("获取成功", result);
        } catch (Exception e) {
            log.error("获取已拥有战宠类型失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 给战宠穿戴装备
     * @param request HTTP请求
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    @PostMapping("/equip-item")
    public Result<PetListVO> equipPetItem(HttpServletRequest request, @RequestBody @Validated EquipPetItemDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            PetListVO result = petService.equipPetItem(dto);
            return Result.OK("穿戴成功", result);
        } catch (Exception e) {
            log.error("战宠穿戴装备失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 卸下战宠装备
     * @param request HTTP请求
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    @PostMapping("/unequip-item")
    public Result<PetListVO> unequipPetItem(HttpServletRequest request, @RequestBody @Validated UnequipPetItemDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            PetListVO result = petService.unequipPetItem(dto);
            return Result.OK("卸下成功", result);
        } catch (Exception e) {
            log.error("卸下战宠装备失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 装备技能到槽位
     * @param request HTTP请求
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    @PostMapping("/equip-skill")
    public Result<PetListVO> equipPetSkill(HttpServletRequest request, @RequestBody @Validated EquipPetSkillDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            PetListVO result = petService.equipPetSkill(dto);
            return Result.OK("装备成功", result);
        } catch (Exception e) {
            log.error("装备技能失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 卸下技能
     * @param request HTTP请求
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    @PostMapping("/unequip-skill")
    public Result<PetListVO> unequipPetSkill(HttpServletRequest request, @RequestBody @Validated UnequipPetSkillDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            PetListVO result = petService.unequipPetSkill(dto);
            return Result.OK("卸下成功", result);
        } catch (Exception e) {
            log.error("卸下技能失败", e);
            return Result.error(e.getMessage());
        }
    }
}
