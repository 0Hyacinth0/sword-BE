-- ============================================
-- WebGame 模块网关路由配置
-- 执行此脚本将 webgame 模块添加到网关路由
-- ============================================

-- 方案一：如果使用 MySQL，执行以下 SQL

-- 1. 更新 DEFAULT_GROUP 的路由配置（添加 webgame 路由）
UPDATE config_info 
SET content = '[{
  "id": "jeecg-system",
  "order": 0,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/sys/**",
      "_genkey_1": "/jmreport/**",
      "_genkey_3": "/online/**",
      "_genkey_4": "/generic/**",
      "_genkey_5": "/oauth2/**",
      "_genkey_6": "/drag/**",
      "_genkey_7": "/actuator/**",
      "_genkey_8": "/airag/**",
      "_genkey_9": "/jimubi/**",
      "_genkey_10": "/openapi/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-demo",
  "order": 1,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/mock/**",
      "_genkey_1": "/test/**",
      "_genkey_2": "/bigscreen/template1/**",
      "_genkey_3": "/bigscreen/template2/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-demo"
}, {
  "id": "webgame",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/webgame/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-system-websocket",
  "order": 3,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/websocket/**",
      "_genkey_1": "/newsWebsocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}, {
  "id": "jeecg-demo-websocket",
  "order": 4,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/vxeSocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-demo"
}, {
  "id": "webgame-websocket",
  "order": 5,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/webgame/ws/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}]',
    md5 = MD5('[{
  "id": "jeecg-system",
  "order": 0,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/sys/**",
      "_genkey_1": "/jmreport/**",
      "_genkey_3": "/online/**",
      "_genkey_4": "/generic/**",
      "_genkey_5": "/oauth2/**",
      "_genkey_6": "/drag/**",
      "_genkey_7": "/actuator/**",
      "_genkey_8": "/airag/**",
      "_genkey_9": "/jimubi/**",
      "_genkey_10": "/openapi/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-demo",
  "order": 1,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/mock/**",
      "_genkey_1": "/test/**",
      "_genkey_2": "/bigscreen/template1/**",
      "_genkey_3": "/bigscreen/template2/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-demo"
}, {
  "id": "webgame",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/webgame/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
}, {
  "id": "jeecg-system-websocket",
  "order": 3,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/websocket/**",
      "_genkey_1": "/newsWebsocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}, {
  "id": "jeecg-demo-websocket",
  "order": 4,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/vxeSocket/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-demo"
}, {
  "id": "webgame-websocket",
  "order": 5,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/webgame/ws/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}]'),
    gmt_modified = NOW()
WHERE data_id = 'jeecg-gateway-router.json' 
  AND group_id = 'DEFAULT_GROUP'
  AND tenant_id = '';

-- 2. 插入新的历史记录
INSERT INTO his_config_info (
  id, nid, data_id, group_id, app_name, content, md5, 
  gmt_create, gmt_modified, src_user, src_ip, op_type, tenant_id
)
SELECT 
  id, NULL, data_id, group_id, app_name, content, md5,
  NOW(), NOW(), src_user, src_ip, 'U', tenant_id
FROM config_info
WHERE data_id = 'jeecg-gateway-router.json' 
  AND group_id = 'DEFAULT_GROUP'
  AND tenant_id = '';


-- ============================================
-- 验证查询
-- ============================================
-- 查看更新后的配置
SELECT data_id, group_id, LEFT(content, 200) as content_preview
FROM config_info
WHERE data_id = 'jeecg-gateway-router.json';
