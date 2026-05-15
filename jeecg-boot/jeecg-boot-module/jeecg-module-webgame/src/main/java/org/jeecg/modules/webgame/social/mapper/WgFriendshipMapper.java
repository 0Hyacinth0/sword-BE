package org.jeecg.modules.webgame.social.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.webgame.social.entity.WgFriendship;

import java.util.List;

/**
 * @Description: 好友关系 Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Mapper
public interface WgFriendshipMapper extends BaseMapper<WgFriendship> {
    
    /**
     * 查询角色的好友列表（通过角色ID）
     * @param characterId 角色ID
     * @return 好友关系列表
     */
    List<WgFriendship> selectFriendsByCharacterId(@Param("characterId") String characterId);
}
