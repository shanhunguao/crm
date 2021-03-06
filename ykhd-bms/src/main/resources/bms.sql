--微信公众号
CREATE TABLE `wm_office_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(30) DEFAULT NULL COMMENT '公众号名称',
  `category` varchar(30) DEFAULT NULL COMMENT '公众号类别',
  `wechat` varchar(30) DEFAULT NULL COMMENT '微信号',
  `company` varchar(60) DEFAULT NULL COMMENT '公司主体',
  `fans` decimal(8,2) DEFAULT '0' COMMENT '粉丝数量',
  `fans_source` varchar(255) DEFAULT NULL COMMENT '粉丝来源',
  `sex_ratio` varchar(10) DEFAULT NULL COMMENT '男女比例',
  `headline_price` decimal(10,2) DEFAULT NULL COMMENT '直投刊例价',
  `headline_cost` decimal(10,2) DEFAULT NULL COMMENT '直投成本价',
  `nothead_price` decimal(10,2) DEFAULT NULL COMMENT '其他条直投刊例价',
  `nothead_cost` decimal(10,2) DEFAULT NULL COMMENT '其他条直投成本价',
  `cpa_price` decimal(10,2) DEFAULT NULL COMMENT 'cpa单价',
  `is_fapiao` tinyint(1) DEFAULT NULL COMMENT '是否含发票',
  `fapiao_point` decimal(4,2) DEFAULT NULL COMMENT '发票税点',
  `refuse_type` varchar(50) DEFAULT NULL COMMENT '拒/接广告类型',
  `is_brush` varchar(50) DEFAULT NULL COMMENT '是否刷号',
  `phone` varchar(50) DEFAULT NULL COMMENT '号主联系方式',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `creator` int(11) NOT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `medium_id` int(11) DEFAULT NULL COMMENT '正在对接的媒介id',
  `score` char(1) DEFAULT NULL COMMENT '评分',
  `comment` varchar(255) DEFAULT NULL COMMENT '评论',
  `cooperate_mode` varchar(20) NOT NULL DEFAULT '直投' COMMENT '合作模式',
  `customer_type` varchar(255) DEFAULT NULL COMMENT '适合客户类型',
  `advantage` tinyint(1) DEFAULT NULL COMMENT '优势号标记： 0申请中  1优势号',
  `advantage_medium` int(11) DEFAULT NULL COMMENT '优势号专属媒介',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--公众号排期
CREATE TABLE `wm_oa_schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `office_account` int(11) DEFAULT NULL COMMENT '公众号id',
  `customer` int(11) DEFAULT NULL COMMENT '客户id',
  `brand` varchar(20) DEFAULT NULL COMMENT '客户品牌',
  `cooperate_mode` varchar(10) DEFAULT NULL COMMENT '合作模式',
  `put_date` datetime DEFAULT NULL COMMENT '投放日期',
  `put_position` varchar(255) DEFAULT NULL COMMENT '投放位置',
  `count` int(10) DEFAULT NULL COMMENT 'cap投放数量',
  `execute_price` decimal(10,2) DEFAULT NULL COMMENT '执行价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `sell_expense` decimal(10,2) DEFAULT NULL COMMENT '销售费用',
  `channel_expense` decimal(10,2) DEFAULT NULL COMMENT '渠道费用',
  `cust_fapiao` tinyint(1) DEFAULT NULL COMMENT '客户是否开票（-1不开票，开票税点）',
  `oa_fapiao` tinyint(1) DEFAULT NULL COMMENT '号主是否含票（-1不开票，开票税点）',
  `oa_fapiao_type` tinyint(1) DEFAULT NULL COMMENT '号主含票类型（0 专票  1普票）',
  `medium` int(11) DEFAULT NULL COMMENT '媒介id',
  `medium_name` varchar(10) DEFAULT NULL COMMENT '媒介名字',
  `confirm_remark` varchar(255) DEFAULT NULL COMMENT '媒介确认备注',
  `remark` varchar(255) DEFAULT NULL COMMENT '创建备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建人id',
  `creator_name` varchar(10) NOT NULL COMMENT '创建人名称',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  `reviewer` int(11) DEFAULT NULL COMMENT '审核人',
  `failure_reason` varchar(255) DEFAULT NULL COMMENT '审核不通过原因',
  `is_pay` tinyint(1) DEFAULT NULL COMMENT '是否已支付',
  `is_return_money` tinyint(1) DEFAULT NULL COMMENT '是否已回款',
  `return_time` datetime DEFAULT NULL COMMENT '回款日期',
  `app_cancel` tinyint(1) DEFAULT NULL COMMENT '申请作废状态',
  `cancel_failure_reason` varchar(255) DEFAULT NULL COMMENT '作废不通过原因',
  `advantage` tinyint(1) NULL COMMENT '优势号排期标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--排期支付申请
CREATE TABLE `wm_payment_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule` int(11) DEFAULT NULL COMMENT '排期id',
  `type` tinyint(1) DEFAULT NULL COMMENT '支付类型',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `pay_account` varchar(30) DEFAULT NULL COMMENT '付款账户',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '开户行',
  `account_holder` varchar(40) DEFAULT NULL COMMENT '开户人',
  `account_number` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `remark` varchar(255) DEFAULT NULL COMMENT '申请备注',
  `creator` int(11) NOT NULL COMMENT '创建人id',
  `creator_name` varchar(10) NOT NULL COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  `payer` int(11) DEFAULT NULL COMMENT '付款人id',
  `paytime` datetime DEFAULT NULL COMMENT '付款时间',
  `pay_remark` varchar(255) DEFAULT NULL COMMENT '付款备注',
  `oa_fapiao` tinyint(1) DEFAULT NULL COMMENT '号主是否开进项发票(-1不开票，开票税点)',
  `oa_fapiao_type` tinyint(1) DEFAULT NULL COMMENT '号主开票类型（0 专票  1普票）',
  `fapiao` int(11) DEFAULT NULL COMMENT '发票id',
  `pay_image` varchar(255) DEFAULT NULL COMMENT '支付截图存储路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--排期回款申请
CREATE TABLE `wm_oas_return` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule` int(11) DEFAULT NULL COMMENT '排期id',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '回款金额',
  `receive_account` varchar(30) DEFAULT NULL COMMENT '收款账户',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '付方开户行',
  `account_holder` varchar(40) DEFAULT NULL COMMENT '付方开户人',
  `account_number` varchar(30) DEFAULT NULL COMMENT '付方银行卡号',
  `remark` varchar(255) DEFAULT NULL COMMENT '申请备注',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  `return_time` datetime DEFAULT NULL COMMENT '回款日期',
  `return_remark` varchar(255) DEFAULT NULL COMMENT '回款备注',
  `return_image` varchar(255) DEFAULT NULL COMMENT '回款截图存储路径',
  `cust_fapiao` tinyint(1) DEFAULT NULL COMMENT '客户是否开票（-1不开票，开票税点）',
  `fapiao` int(11) DEFAULT NULL COMMENT '销项发票ID',
  `reason` varchar(255) DEFAULT NULL COMMENT '驳回原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--管理员
CREATE TABLE `bms_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL COMMENT '姓名',
  `password` char(32) DEFAULT NULL COMMENT '密码',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态 0启用  1停用',
  `department` int(11) DEFAULT NULL COMMENT '所属部门',
  `role` int(11) DEFAULT NULL COMMENT '角色',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除（0未 1删）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `mobile` char(11) DEFAULT NULL COMMENT '手机号码',
  `number` char(5) DEFAULT NULL COMMENT '工号',
  `entry_time` char(10) DEFAULT NULL COMMENT '入职时间',
  `departure_time` char(10) DEFAULT NULL COMMENT '离职时间',
  `job_type` varchar(4) DEFAULT NULL COMMENT '员工类型',
  `position_status` char(2) DEFAULT NULL COMMENT '员工状态',
  `probation_duration` varchar(10) DEFAULT NULL COMMENT '试用期时长',
  `formal_date` char(10) DEFAULT NULL COMMENT '转正日期',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `birthday` char(10) DEFAULT NULL COMMENT '出生日期',
  `gender` char(1) DEFAULT NULL COMMENT '性别',
  `nation` varchar(6) DEFAULT NULL COMMENT '民族',
  `address` varchar(50) DEFAULT NULL COMMENT '住址',
  `marriage` char(2) DEFAULT NULL COMMENT '婚姻状况',
  `huji` varchar(10) DEFAULT NULL COMMENT '户籍类型',
  `education` varchar(3) DEFAULT NULL COMMENT '学历',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '开户行',
  `account_number` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `emergency_contact` varchar(10) DEFAULT NULL COMMENT '紧急联系人',
  `emergency_phone` char(12) DEFAULT NULL COMMENT '紧急联系人电话',
  `emergency_relation` varchar(10) DEFAULT NULL COMMENT '紧急联系人关系',
  `id_card_img` varchar(100) DEFAULT NULL COMMENT '身份证正面照',
  `id_card_img2` varchar(100) DEFAULT NULL COMMENT '身份证反面照',
  `education_img` varchar(100) DEFAULT NULL COMMENT '学历证件照',
  `departure_img` varchar(100) DEFAULT NULL COMMENT '前公司离职证明照',
  `picture` varchar(100) DEFAULT NULL COMMENT '员工照片',
  `probation_salary` varchar(10) DEFAULT NULL COMMENT '试用期底薪',
  `formal_salary` varchar(10) DEFAULT NULL COMMENT '转正底薪',
  `current_salary` varchar(10) DEFAULT NULL COMMENT '当前底薪',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--部门
CREATE TABLE `bms_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `parent` int(11) DEFAULT NULL COMMENT '上级部门',
  `leader` int(11) NULL COMMENT '部门领导',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--公众号跟进记录
CREATE TABLE `wm_oa_followup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `office_account` int(11) NOT NULL COMMENT '公众号id',
  `creator` int(11) NOT NULL COMMENT '创建人id',
  `creator_name` varchar(20) NOT NULL COMMENT '创建人名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `content` varchar(255) NOT NULL COMMENT '内容描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--修改申请记录
CREATE TABLE `wm_edit_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NOT NULL COMMENT '主体类别',
  `identity` int(11) NOT NULL COMMENT '主体id',
  `content` varchar(500) NOT NULL COMMENT '内容jsonString',
  `state` tinyint(1) NOT NULL COMMENT '状态：0待审核  1审核通过  2未通过',
  `creator` int(11) NOT NULL COMMENT '创建人id',
  `creator_name` varchar(20) NOT NULL COMMENT '创建人名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `reviewer` int(11) NOT NULL COMMENT '审核人id',
  `failure_reason` varchar(255) DEFAULT NULL COMMENT '不通过原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--角色
CREATE TABLE `bms_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '名称',
  `menu_auth` varchar(1000) DEFAULT NULL COMMENT '权限集',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--文件上传事件记录
CREATE TABLE `wm_upload_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NOT NULL COMMENT '主体类别',
  `identity` int(11) NOT NULL COMMENT '主体id',
  `file_list` varchar(1000) NOT NULL COMMENT '文件存储路径集合jsonStr',
  `uploader` int(11) NOT NULL COMMENT '上传者',
  `create_time` datetime NOT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--收藏公众号
CREATE TABLE `wm_oa_collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `office_account` int(11) NOT NULL COMMENT '公众号id',
  `collector` int(11) NOT NULL COMMENT '收藏人id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--系统日志
CREATE TABLE `bms_system_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NOT NULL COMMENT '日志类型',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `manager` int(11) NOT NULL COMMENT '登陆用户id',
  `name` varchar(10) NOT NULL COMMENT '登陆用户姓名',
  `login_ip` varchar(15) NOT NULL COMMENT '登陆ip',
  `create_time` datetime NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--客户
CREATE TABLE `wm_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(30) DEFAULT NULL COMMENT '公司名称',
  `brand` varchar(20) DEFAULT NULL COMMENT '品牌',
  `industry` varchar(20) DEFAULT NULL COMMENT '行业',
  `domain` varchar(20) DEFAULT NULL COMMENT '领域',
  `linkman` varchar(20) DEFAULT NULL COMMENT '联系人名称',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `qq` varchar(20) DEFAULT NULL COMMENT 'qq',
  `wx` varchar(20) DEFAULT NULL COMMENT 'wx',
  `belong_user` int(11) DEFAULT NULL COMMENT '所属人',
  `creator` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_lock` tinyint(1) DEFAULT '0' COMMENT '是否是公海（0-否，1-是）',
  `high_sea_time` datetime DEFAULT NULL COMMENT '进入公海时间',
  `draw_time` date DEFAULT NULL COMMENT '活跃时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0表示未删除 1表示已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--客户跟进表
CREATE TABLE `wm_customer_followup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL COMMENT '关联客户id',
  `manager_id` int(11) DEFAULT NULL COMMENT '录入记录销售id',
  `record` varchar(3000) DEFAULT NULL COMMENT '跟进记录',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  `appoint_time` varchar(50) DEFAULT NULL COMMENT '下次回访时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--发票
CREATE TABLE `wm_fapiao` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bill_no` varchar(100) DEFAULT NULL COMMENT '发票号码',
  `enter_name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `make_date` date DEFAULT NULL COMMENT '开票日期',
  `verificatio_time` date DEFAULT NULL COMMENT '发票认证时间',
  `make_name` varchar(50) DEFAULT NULL COMMENT '开具公司',
  `duty_no` varchar(20) DEFAULT NULL COMMENT '税号',
  `bank_name` varchar(50) DEFAULT NULL COMMENT '开户行',
  `bank_no` varchar(30) DEFAULT NULL COMMENT '银行账号',
  `bank_address` varchar(100) DEFAULT NULL COMMENT '银行地址',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `money` double(16,2) DEFAULT NULL COMMENT '金额',
  `rates` double(4,2) DEFAULT NULL COMMENT '税率',
  `rates_money` double(16,2) DEFAULT NULL COMMENT '税额',
  `total_money` double(16,2) DEFAULT NULL COMMENT '价税合计',
  `actual_money` double(16,2) DEFAULT NULL COMMENT '实际开票金额',
  `type` char(1) NOT NULL DEFAULT '0' COMMENT '发票类型（0-普票，1-专票）',
  `is_add` char(1) DEFAULT NULL COMMENT '是否进项（0-销项，1-进项）',
  `create_userid` int(11) DEFAULT NULL COMMENT '创建用户id',
  `create_username` varchar(50) DEFAULT NULL COMMENT '创建用户名称',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `audit_userid` int(11) DEFAULT NULL COMMENT '审核人员id',
  `audit_desc` varchar(200) DEFAULT NULL COMMENT '审核意见信息',
  `sell_userid` int(11) DEFAULT NULL COMMENT '所属销售人员',
  `state` char(1) DEFAULT NULL COMMENT '状态（0-填制，1-报送，2-审核通过，3-审核不通过，4-删除）',
  `is_balance` varchar(255) NOT NULL DEFAULT '0' COMMENT '是否已结算（0-否，1-是）',
  `invoice_project` varchar(50) DEFAULT NULL COMMENT '发票项目',
  `fapiao_img` varchar(500) DEFAULT NULL COMMENT '发票图片',
  `fapiao_code` varchar(255) DEFAULT NULL COMMENT '发票代码',
  `source` tinyint(1) DEFAULT NULL COMMENT '来源：0平台   1非平台 ',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0表示未删除 1表示已删除)',
  PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--行业
CREATE TABLE `wm_industry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `industry_id` int(3) NOT NULL COMMENT '行业类别id',
  `industry_pid` int(3) DEFAULT NULL COMMENT '行业所属上级行业id',
  `industry_name` varchar(20) DEFAULT NULL COMMENT '行业名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
INSERT INTO `wm_industry` VALUES (1, 1, 0, '教育');
INSERT INTO `wm_industry` VALUES (2, 2, 0, '小说');
INSERT INTO `wm_industry` VALUES (3, 3, 0, '电商');
INSERT INTO `wm_industry` VALUES (4, 4, 0, '黑五');
INSERT INTO `wm_industry` VALUES (5, 5, 0, '保险');
INSERT INTO `wm_industry` VALUES (6, 6, 0, '财经');
INSERT INTO `wm_industry` VALUES (7, 7, 1, '全科辅导');
INSERT INTO `wm_industry` VALUES (8, 8, 1, '少儿英语');
INSERT INTO `wm_industry` VALUES (9, 9, 1, '少儿编程');
INSERT INTO `wm_industry` VALUES (10, 10, 1, '少儿钢琴');
INSERT INTO `wm_industry` VALUES (11, 11, 1, '少儿美术');
INSERT INTO `wm_industry` VALUES (12, 12, 1, '少儿语文');
INSERT INTO `wm_industry` VALUES (13, 13, 1, '成人教育');
INSERT INTO `wm_industry` VALUES (14, 14, 1, '职场教育');
INSERT INTO `wm_industry` VALUES (15, 15, 2, '男频');
INSERT INTO `wm_industry` VALUES (16, 16, 2, '女频');
INSERT INTO `wm_industry` VALUES (17, 17, 3, '平台');
INSERT INTO `wm_industry` VALUES (18, 18, 3, '土特产');
INSERT INTO `wm_industry` VALUES (19, 19, 3, '服饰');
INSERT INTO `wm_industry` VALUES (20, 20, 3, '美妆');
INSERT INTO `wm_industry` VALUES (21, 21, 3, '母婴');
INSERT INTO `wm_industry` VALUES (22, 22, 3, '食品');
INSERT INTO `wm_industry` VALUES (23, 23, 3, '数码');
INSERT INTO `wm_industry` VALUES (24, 24, 3, '电器');
INSERT INTO `wm_industry` VALUES (25, 25, 3, '汽车');
INSERT INTO `wm_industry` VALUES (26, 26, 3, '家居');
INSERT INTO `wm_industry` VALUES (27, 27, 3, '生鲜水果');
INSERT INTO `wm_industry` VALUES (28, 28, 3, '配饰');
INSERT INTO `wm_industry` VALUES (29, 29, 4, '增高');
INSERT INTO `wm_industry` VALUES (30, 30, 4, '减肥');
INSERT INTO `wm_industry` VALUES (31, 31, 4, '近视');
INSERT INTO `wm_industry` VALUES (32, 32, 4, '祛斑');
INSERT INTO `wm_industry` VALUES (33, 33, 4, '壮阳');
INSERT INTO `wm_industry` VALUES (34, 34, 4, '微商');
INSERT INTO `wm_industry` VALUES (35, 35, 4, '网赚');
INSERT INTO `wm_industry` VALUES (36, 36, 5, '少儿保险');
INSERT INTO `wm_industry` VALUES (37, 37, 5, '成人保险');
INSERT INTO `wm_industry` VALUES (38, 38, 6, '招商');
INSERT INTO `wm_industry` VALUES (39, 39, 6, '财经杂志');
INSERT INTO `wm_industry` VALUES (40, 40, 1, '数学');

--银行账户
CREATE TABLE `wm_bank_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NOT NULL COMMENT '主体类别',
  `identity` int(11) NOT NULL COMMENT '主体id',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '开户行',
  `account_holder` varchar(40) DEFAULT NULL COMMENT '开户人',
  `account_number` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--内容变更记录
CREATE TABLE `wm_content_change` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NOT NULL COMMENT '内容变更主体类别',
  `identity` int(11) NOT NULL COMMENT '主体id',
  `change_desc` varchar(1000) NOT NULL COMMENT '内容变更描述',
  `creator` int(11) NOT NULL COMMENT '变更人',
  `creator_name` varchar(10) NOT NULL COMMENT '变更人名称',
  `create_time` datetime NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--节假日
CREATE TABLE `bms_holiday` (
  `holiday` date NOT NULL COMMENT '日期',
  PRIMARY KEY (`holiday`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='节假日表';

--工作任务
CREATE TABLE `bms_work_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `executor` int(11) DEFAULT NULL COMMENT '执行者',
  `name` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `type` tinyint(1) DEFAULT NULL COMMENT '周期',
  `descrip` varchar(255) DEFAULT NULL COMMENT '描述',
  `standard` varchar(255) DEFAULT NULL COMMENT '标准',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `plan_finish_date` char(10) DEFAULT NULL COMMENT '计划完成日期',
  `actual_finish_date` char(10) DEFAULT NULL COMMENT '实际完成日期',
  `priority` tinyint(1) DEFAULT NULL COMMENT '优先级',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  `summary` varchar(255) DEFAULT NULL COMMENT '完成总结',
  `file_list` varchar(1000) DEFAULT NULL COMMENT '文件存储路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--批注
CREATE TABLE `bms_note` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creator` int(11) DEFAULT NULL COMMENT '批注人',
  `manager` int(11) DEFAULT NULL COMMENT '被批注人',
  `text` varchar(255) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--绩效模板
CREATE TABLE `bms_assess_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL COMMENT '考核内容',
  `weight` varchar(10) DEFAULT '0' COMMENT '权重',
  `standard` varchar(255) DEFAULT NULL COMMENT '考核标准',
  `type` varchar(255) DEFAULT NULL COMMENT '类型(0百分比 1加分项 2减分项)',
  `target` varchar(255) DEFAULT NULL COMMENT '目标',
  `role_id` int(11) DEFAULT NULL COMMENT '被考核人角色ID',
  `audit_role_id` int(11) DEFAULT NULL COMMENT '考核人角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `group_name` varchar(255) DEFAULT NULL COMMENT '考核组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='绩效模板表';

--绩效考核记录表
CREATE TABLE `bms_assess` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '员工姓名',
  `user_id` int(11) DEFAULT NULL COMMENT '员工Id',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门',
  `role_id` int(11) DEFAULT NULL COMMENT '角色',
  `score` decimal(10,1) DEFAULT '0.0' COMMENT '分数',
  `state` tinyint(10) DEFAULT '0' COMMENT '(0未考核  1已打分 2待确认 3已考核)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `assess_time` varchar(255) DEFAULT NULL COMMENT '考核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='考核记录表';

--考核历史表
CREATE TABLE `bms_assess_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `template_id` int(11) DEFAULT NULL COMMENT '模板ID',
  `content` varchar(255) DEFAULT NULL COMMENT '考核内容',
  `weight` varchar(10) DEFAULT NULL COMMENT '权重',
  `standard` varchar(255) DEFAULT NULL COMMENT '考核标准',
  `type` varchar(255) DEFAULT NULL COMMENT '类型(0百分比 1加分项 2减分项)',
  `target` varchar(255) DEFAULT NULL COMMENT '目标',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `audit_role_id` int(11) DEFAULT NULL COMMENT '考核人角色ID',
  `score` decimal(10,1) DEFAULT NULL COMMENT '分数',
  `gross_score` decimal(10,1) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `assess_time` varchar(255) DEFAULT NULL COMMENT '考核时间',
  `group_name` varchar(255) DEFAULT NULL COMMENT '考核组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='考核历史表';

--集采公众号排期
CREATE TABLE `jc_oa_schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `office_account` int(11) DEFAULT NULL COMMENT '公众号id',
  `customer` int(11) DEFAULT NULL COMMENT '客户id',
  `brand` varchar(20) DEFAULT NULL COMMENT '客户品牌',
  `cooperate_mode` varchar(10) DEFAULT NULL COMMENT '合作模式',
  `put_date` datetime DEFAULT NULL COMMENT '投放日期',
  `put_position` varchar(255) DEFAULT NULL COMMENT '投放位置',
  `count` int(10) DEFAULT NULL COMMENT 'cap投放数量',
  `execute_price` decimal(10,2) DEFAULT NULL COMMENT '执行价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `sell_expense` decimal(10,2) DEFAULT NULL COMMENT '销售费用',
  `channel_expense` decimal(10,2) DEFAULT NULL COMMENT '渠道费用',
  `cust_fapiao` tinyint(1) DEFAULT NULL COMMENT '客户是否开票（-1不开票，开票税点）',
  `oa_fapiao` tinyint(1) DEFAULT NULL COMMENT '号主是否含票（-1不开票，开票税点）',
  `oa_fapiao_type` tinyint(1) DEFAULT NULL COMMENT '号主含票类型（0 专票  1普票）',
  `medium` int(11) DEFAULT NULL COMMENT '媒介id',
  `medium_name` varchar(10) DEFAULT NULL COMMENT '媒介名字',
  `confirm_remark` varchar(255) DEFAULT NULL COMMENT '媒介确认备注',
  `remark` varchar(255) DEFAULT NULL COMMENT '创建备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建人id',
  `creator_name` varchar(10) NOT NULL COMMENT '创建人名称',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  `reviewer` int(11) DEFAULT NULL COMMENT '审核人',
  `failure_reason` varchar(255) DEFAULT NULL COMMENT '审核不通过原因',
  `is_pay` tinyint(1) DEFAULT NULL COMMENT '是否已支付',
  `is_return_money` tinyint(1) DEFAULT NULL COMMENT '是否已回款',
  `return_time` datetime DEFAULT NULL COMMENT '回款日期',
  `app_cancel` tinyint(1) DEFAULT NULL COMMENT '申请作废状态',
  `cancel_failure_reason` varchar(255) DEFAULT NULL COMMENT '作废不通过原因',
  `advantage` tinyint(1) NULL COMMENT '优势号排期标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--集采排期支付申请
CREATE TABLE `jc_payment_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule` int(11) DEFAULT NULL COMMENT '排期id',
  `type` tinyint(1) DEFAULT NULL COMMENT '支付类型',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `pay_account` varchar(30) DEFAULT NULL COMMENT '付款账户',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '开户行',
  `account_holder` varchar(40) DEFAULT NULL COMMENT '开户人',
  `account_number` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `remark` varchar(255) DEFAULT NULL COMMENT '申请备注',
  `creator` int(11) NOT NULL COMMENT '创建人id',
  `creator_name` varchar(10) NOT NULL COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号码',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  `payer` int(11) DEFAULT NULL COMMENT '付款人id',
  `paytime` datetime DEFAULT NULL COMMENT '付款时间',
  `pay_remark` varchar(255) DEFAULT NULL COMMENT '付款备注',
  `oa_fapiao` tinyint(1) DEFAULT NULL COMMENT '号主是否开进项发票(-1不开票，开票税点)',
  `oa_fapiao_type` tinyint(1) DEFAULT NULL COMMENT '号主开票类型（0 专票  1普票）',
  `fapiao` int(11) DEFAULT NULL COMMENT '发票id',
  `pay_image` varchar(255) DEFAULT NULL COMMENT '支付截图存储路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--集采排期回款申请
CREATE TABLE `jc_oas_return` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule` int(11) DEFAULT NULL COMMENT '排期id',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '回款金额',
  `receive_account` varchar(30) DEFAULT NULL COMMENT '收款账户',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '付方开户行',
  `account_holder` varchar(40) DEFAULT NULL COMMENT '付方开户人',
  `account_number` varchar(30) DEFAULT NULL COMMENT '付方银行卡号',
  `remark` varchar(255) DEFAULT NULL COMMENT '申请备注',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  `return_time` datetime DEFAULT NULL COMMENT '回款日期',
  `return_remark` varchar(255) DEFAULT NULL COMMENT '回款备注',
  `return_image` varchar(255) DEFAULT NULL COMMENT '回款截图存储路径',
  `cust_fapiao` tinyint(1) DEFAULT NULL COMMENT '客户是否开票（-1不开票，开票税点）',
  `fapiao` int(11) DEFAULT NULL COMMENT '销项发票ID',
  `reason` varchar(255) DEFAULT NULL COMMENT '驳回原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--集采发票
CREATE TABLE `jc_fapiao` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bill_no` varchar(100) DEFAULT NULL COMMENT '发票号码',
  `enter_name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `make_date` date DEFAULT NULL COMMENT '开票日期',
  `verificatio_time` date DEFAULT NULL COMMENT '发票认证时间',
  `make_name` varchar(50) DEFAULT NULL COMMENT '开具公司',
  `duty_no` varchar(20) DEFAULT NULL COMMENT '税号',
  `bank_name` varchar(50) DEFAULT NULL COMMENT '开户行',
  `bank_no` varchar(30) DEFAULT NULL COMMENT '银行账号',
  `bank_address` varchar(100) DEFAULT NULL COMMENT '银行地址',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `money` double(16,2) DEFAULT NULL COMMENT '金额',
  `rates` double(4,2) DEFAULT NULL COMMENT '税率',
  `rates_money` double(16,2) DEFAULT NULL COMMENT '税额',
  `total_money` double(16,2) DEFAULT NULL COMMENT '价税合计',
  `actual_money` double(16,2) DEFAULT NULL COMMENT '实际开票金额',
  `type` char(1) NOT NULL DEFAULT '0' COMMENT '发票类型（0-普票，1-专票）',
  `is_add` char(1) DEFAULT NULL COMMENT '是否进项（0-销项，1-进项）',
  `create_userid` int(11) DEFAULT NULL COMMENT '创建用户id',
  `create_username` varchar(50) DEFAULT NULL COMMENT '创建用户名称',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `audit_userid` int(11) DEFAULT NULL COMMENT '审核人员id',
  `audit_desc` varchar(200) DEFAULT NULL COMMENT '审核意见信息',
  `sell_userid` int(11) DEFAULT NULL COMMENT '所属销售人员',
  `state` char(1) DEFAULT NULL COMMENT '状态（0-填制，1-报送，2-审核通过，3-审核不通过，4-删除）',
  `is_balance` varchar(255) NOT NULL DEFAULT '0' COMMENT '是否已结算（0-否，1-是）',
  `invoice_project` varchar(50) DEFAULT NULL COMMENT '发票项目',
  `fapiao_img` varchar(500) DEFAULT NULL COMMENT '发票图片',
  `fapiao_code` varchar(255) DEFAULT NULL COMMENT '发票代码',
  `source` tinyint(1) DEFAULT NULL COMMENT '来源：0平台   1非平台 ',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0表示未删除 1表示已删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--询号
CREATE TABLE `wm_xunhao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creator` int(11) NOT NULL,
  `medium` int(11) NOT NULL,
  `office_account` int(11) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL COMMENT '品牌名及文案链接',
  `need` varchar(255) DEFAULT NULL COMMENT '销售需求',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `reply_time` datetime DEFAULT NULL COMMENT '应答时间',
  `content` varchar(255) DEFAULT NULL COMMENT '应答内容',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;