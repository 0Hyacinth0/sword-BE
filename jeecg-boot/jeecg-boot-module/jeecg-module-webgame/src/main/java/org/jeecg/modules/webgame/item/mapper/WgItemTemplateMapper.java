package org.jeecg.modules.webgame.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.webgame.item.entity.WgItemTemplate;

import java.util.List;

/**
 * @Description: 物品模板Mapper
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
public interface WgItemTemplateMapper extends BaseMapper<WgItemTemplate> {

    /**
     * 根据物品ID列表查询物品模板
     * @param itemIds 物品ID列表
     * @return 物品模板列表
     */
    @Select("<script>" +
            "SELECT * FROM wg_item_template WHERE item_id IN " +
            "<foreach collection='itemIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<WgItemTemplate> selectByIds(List<Integer> itemIds);
}
