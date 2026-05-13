package org.jeecg.modules.webgame.pet.service;

import org.jeecg.modules.webgame.pet.dto.*;
import org.jeecg.modules.webgame.pet.vo.*;

import java.util.List;

/**
 * @Description: 战宠服务接口
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
public interface IPetService {

    /**
     * 获取角色的战宠列表
     * @param characterId 角色ID
     * @param userId 用户ID
     * @return 战宠列表
     */
    PetListVO getPetList(String characterId, String userId);

    /**
     * 设置出战战宠
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    PetListVO setActivePet(SetActivePetDTO dto);

    /**
     * 喂食战宠
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    PetListVO feedPet(FeedPetDTO dto);

    /**
     * 进化战宠
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    PetListVO evolvePet(EvolvePetDTO dto);

    /**
     * 重命名战宠
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    PetListVO renamePet(RenamePetDTO dto);

    /**
     * 获取战宠详情
     * @param petId 战宠实例ID
     * @param userId 用户ID
     * @return 战宠详情
     */
    PetInfoVO getPetDetail(String petId, String userId);

    /**
     * 获取所有战宠类型（图鉴用）
     * @return 战宠类型列表
     */
    PetTypeListVO getAllPetTypes();

    /**
     * 获取角色已拥有的战宠类型ID列表
     * @param characterId 角色ID
     * @param userId 用户ID
     * @return 已拥有的战宠类型ID列表
     */
    OwnedPetTypesVO getOwnedPetTypes(String characterId, String userId);

    /**
     * 给战宠穿戴装备
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    PetListVO equipPetItem(EquipPetItemDTO dto);

    /**
     * 卸下战宠装备
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    PetListVO unequipPetItem(UnequipPetItemDTO dto);

    /**
     * 装备技能到槽位
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    PetListVO equipPetSkill(EquipPetSkillDTO dto);

    /**
     * 卸下技能
     * @param dto 请求参数
     * @return 更新后的战宠列表
     */
    PetListVO unequipPetSkill(UnequipPetSkillDTO dto);
}
