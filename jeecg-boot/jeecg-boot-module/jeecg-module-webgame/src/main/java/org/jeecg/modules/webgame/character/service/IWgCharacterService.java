package org.jeecg.modules.webgame.character.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.webgame.character.dto.AttributePointDTO;
import org.jeecg.modules.webgame.character.dto.CreateCharacterDTO;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.vo.CharacterVO;

import java.util.List;

/**
 * @Description: 游戏角色Service
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
public interface IWgCharacterService extends IService<WgCharacter> {

    /**
     * 创建角色
     * @param userId 用户ID
     * @param createDTO 创建参数
     * @return 角色信息
     */
    CharacterVO createCharacter(String userId, CreateCharacterDTO createDTO);

    /**
     * 获取角色信息
     * @param characterId 角色ID
     * @return 角色信息(包含计算的衍生属性)
     */
    CharacterVO getCharacterInfo(String characterId);

    /**
     * 属性加点
     * @param characterId 角色ID
     * @param pointDTO 加点参数
     * @return 更新后的角色信息
     */
    CharacterVO addAttributePoints(String characterId, AttributePointDTO pointDTO);

    /**
     * 获取用户的角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<CharacterVO> getCharacterList(String userId);

    /**
     * 删除角色
     * @param characterId 角色ID
     * @param userId 用户ID（用于验证权限）
     */
    void deleteCharacter(String characterId, String userId);
}
