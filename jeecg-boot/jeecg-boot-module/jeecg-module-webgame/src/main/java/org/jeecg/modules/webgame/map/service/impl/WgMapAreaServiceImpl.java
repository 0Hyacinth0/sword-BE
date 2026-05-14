package org.jeecg.modules.webgame.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.webgame.item.entity.WgItemTemplate;
import org.jeecg.modules.webgame.item.mapper.WgItemTemplateMapper;
import org.jeecg.modules.webgame.map.dto.AreaDropDTO;
import org.jeecg.modules.webgame.map.dto.AreaMonsterDTO;
import org.jeecg.modules.webgame.map.dto.MapAreaDTO;
import org.jeecg.modules.webgame.map.entity.WgAreaDrop;
import org.jeecg.modules.webgame.map.entity.WgAreaMonster;
import org.jeecg.modules.webgame.map.entity.WgMapArea;
import org.jeecg.modules.webgame.map.mapper.WgAreaDropMapper;
import org.jeecg.modules.webgame.map.mapper.WgAreaMonsterMapper;
import org.jeecg.modules.webgame.map.mapper.WgMapAreaMapper;
import org.jeecg.modules.webgame.map.service.IWgMapAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 地图区域Service实现
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Slf4j
@Service
public class WgMapAreaServiceImpl extends ServiceImpl<WgMapAreaMapper, WgMapArea> implements IWgMapAreaService {

    @Autowired
    private WgAreaMonsterMapper areaMonsterMapper;

    @Autowired
    private WgAreaDropMapper areaDropMapper;

    @Autowired
    private WgItemTemplateMapper itemTemplateMapper;

    @Override
    public List<MapAreaDTO> getAllAreas() {
        // 1. 查询所有区域（按排序顺序）
        LambdaQueryWrapper<WgMapArea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(WgMapArea::getSortOrder);
        List<WgMapArea> areas = this.list(queryWrapper);

        // 2. 转换为 DTO 并组装怪物和掉落信息
        return areas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MapAreaDTO getAreaById(String areaId) {
        // 1. 查询区域
        WgMapArea area = this.getById(areaId);
        if (area == null) {
            return null;
        }

        // 2. 转换为 DTO
        return convertToDTO(area);
    }

    /**
     * 将实体转换为 DTO
     */
    private MapAreaDTO convertToDTO(WgMapArea area) {
        MapAreaDTO dto = new MapAreaDTO();
        dto.setId(area.getId());
        dto.setName(area.getName());
        dto.setIcon(area.getIcon());
        dto.setLevelRange(new Integer[]{area.getLevelMin(), area.getLevelMax()});
        dto.setDescription(area.getDescription());
        dto.setUnlockLevel(area.getUnlockLevel());

        // 3. 查询怪物列表
        List<WgAreaMonster> monsters = areaMonsterMapper.selectByAreaId(area.getId());
        List<AreaMonsterDTO> monsterDTOs = monsters.stream()
                .map(this::convertMonsterToDTO)
                .collect(Collectors.toList());
        dto.setMonsters(monsterDTOs);

        // 4. 查询掉落列表
        List<WgAreaDrop> drops = areaDropMapper.selectByAreaId(area.getId());
        List<AreaDropDTO> dropDTOs = drops.stream()
                .map(this::convertDropToDTO)
                .collect(Collectors.toList());
        dto.setDrops(dropDTOs);

        return dto;
    }

    /**
     * 转换怪物实体为 DTO
     */
    private AreaMonsterDTO convertMonsterToDTO(WgAreaMonster monster) {
        AreaMonsterDTO dto = new AreaMonsterDTO();
        dto.setId(monster.getId());
        dto.setName(monster.getName());
        dto.setLevel(monster.getLevel());
        dto.setType(monster.getType());
        return dto;
    }

    /**
     * 转换掉落实体为 DTO（需要查询物品名称）
     */
    private AreaDropDTO convertDropToDTO(WgAreaDrop drop) {
        AreaDropDTO dto = new AreaDropDTO();
        dto.setItemId(drop.getItemId());
        dto.setRarity(drop.getRarity());

        // 查询物品名称（需要将 Integer 转为 String）
        String itemIdStr = drop.getItemId() != null ? String.valueOf(drop.getItemId()) : null;
        WgItemTemplate itemTemplate = itemIdStr != null ? itemTemplateMapper.selectById(itemIdStr) : null;
        if (itemTemplate != null) {
            dto.setName(itemTemplate.getName());
        } else {
            dto.setName("未知物品");
        }

        return dto;
    }
}
