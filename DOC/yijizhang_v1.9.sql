-- MySQL Workbench Synchronization
-- Generated: 2015-11-11 17:17
-- Model: Database design for yijizhang
-- Version: 1.0
-- Project: yijizhang
-- Author: Sam,John,Joey,Jiarui

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`account_book` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `book_name` VARCHAR(128) NOT NULL COMMENT '账套名称',
  `money_code` VARCHAR(45) NULL DEFAULT NULL COMMENT '记账本位币代码',
  `money_name` VARCHAR(45) NULL DEFAULT NULL COMMENT '记账本位币名称',
  `start_time` DATE NULL DEFAULT NULL COMMENT '账套启用日期',
  `init_year` INT(11) NULL DEFAULT NULL COMMENT '启用年',
  `init_period` INT(11) NULL DEFAULT NULL COMMENT '账套启用会计期间',
  `company_name` VARCHAR(128) NULL DEFAULT NULL COMMENT '公司名称',
  `company_id` VARCHAR(45) NULL DEFAULT NULL COMMENT '公司id',
  `dict_value_id` INT(11) NULL DEFAULT NULL COMMENT '字典数据表id',
  `over_flag` INT(11) NULL DEFAULT NULL COMMENT '是否结束此次账套标示\n1  为结束    \n0  未结束',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '修改时间',
  `last_year_id` BIGINT(20) NULL DEFAULT NULL COMMENT '上一年账套id',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '账套表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`account_subject` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `subject_code` BIGINT(20) NOT NULL COMMENT '会计科目编码  \n暂定总分类：-1       小分类：0',
  `subject_name` VARCHAR(45) NOT NULL COMMENT '会计科目名称\n总分类与小分类填对应名称',
  `parent_subject_code` BIGINT(20) NOT NULL COMMENT '父会计科目id',
  `level` INT(11) NOT NULL COMMENT '暂定总分类：-1   小分类：0\n会计科目一：1    会计科目二：2',
  `tip_info` VARCHAR(20) NULL DEFAULT NULL COMMENT ' 助记码',
  `direction` INT(11) NULL DEFAULT NULL COMMENT '借方向 ：1     贷方向：2',
  `book_id` BIGINT(20) NULL DEFAULT NULL COMMENT '账套id',
  `total_debit` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '累计借方金额',
  `total_credit` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '累计贷方金额 ',
  `initial_left` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '期初余额',
  `year_occur_amount` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '本年累计损益实际发生额',
  `end_flag` INT(11) NULL DEFAULT 0 COMMENT '是否结束此次账套标示/期结账\n1  结束   0  未结束',
  `base_flag` INT(11) NULL DEFAULT 0 COMMENT '0：基础数据     1：非基础数据    ',
  `company_id` VARCHAR(45) NULL DEFAULT NULL COMMENT '公司id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_account_subject_account_book1_idx` (`book_id` ASC),
  CONSTRAINT `fk_account_subject_account_book1`
    FOREIGN KEY (`book_id`)
    REFERENCES `yjz_prod`.`account_book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '会计科目表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`dict_type` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_code` VARCHAR(45) NULL DEFAULT NULL COMMENT '字典类型code，字典数据表外键',
  `type_name` VARCHAR(45) NULL DEFAULT NULL COMMENT '字典类型名称',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '字典类型表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`dict_value` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_type_id` BIGINT(20) NULL DEFAULT NULL COMMENT '字典类型code，字典数据表外键',
  `value` VARCHAR(45) NULL DEFAULT NULL COMMENT '简称',
  `show_value` VARCHAR(45) NULL DEFAULT NULL COMMENT '显示名字',
  `is_fixed` INT(11) NULL DEFAULT NULL COMMENT '是否固定，固定则不可修改  值：1\n不固定：0',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_dict_value_dict_type1_idx` (`dict_type_id` ASC),
  CONSTRAINT `fk_dict_value_dict_type1`
    FOREIGN KEY (`dict_type_id`)
    REFERENCES `yjz_prod`.`dict_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '字典数据表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`account_subject_template` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `subject_code` BIGINT(20) NOT NULL COMMENT '会计科目编码  \n暂定总分类：-1       小分类：0',
  `subject_name` VARCHAR(128) NOT NULL COMMENT '会计科目名称\n总分类与小分类填对应名称',
  `parent_subject_code` BIGINT(20) NOT NULL COMMENT '父会计科目id',
  `level` INT(11) NOT NULL COMMENT '暂定总分类：-1   小分类：0\n会计科目一：1    会计科目二：2',
  `balance_direction` INT(11) NULL DEFAULT NULL COMMENT '借方向 ：1     贷方向：2',
  `dict_value_id` INT(11) NULL DEFAULT NULL COMMENT '字段数据id，表示科目体系id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '会计科目模板表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`voucher` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `voucher_word` VARCHAR(25) NULL DEFAULT NULL COMMENT '凭证字',
  `voucher_no` INT(11) NULL DEFAULT NULL COMMENT '凭证号  必须唯一',
  `voucher_time` TIMESTAMP NULL DEFAULT NULL COMMENT '日期',
  `bill_num` INT(11) NULL DEFAULT NULL COMMENT '附单据',
  `audit_user` VARCHAR(45) NULL DEFAULT NULL COMMENT '审核人',
  `posting_user` VARCHAR(45) NULL DEFAULT NULL COMMENT '过账人',
  `touching_user` VARCHAR(45) NULL DEFAULT NULL COMMENT '制单人',
  `carry_over` INT(1) NULL DEFAULT NULL COMMENT '1:是结账凭证',
  `period_id` BIGINT(20) NULL DEFAULT NULL COMMENT '期间id',
  `company_id` VARCHAR(45) NULL DEFAULT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `accounting_no_UNIQUE` (`voucher_no` ASC, `period_id` ASC),
  INDEX `fk_voucher_period1_idx` (`period_id` ASC),
  CONSTRAINT `fk_voucher_period1`
    FOREIGN KEY (`period_id`)
    REFERENCES `yjz_prod`.`period` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '记账凭证主表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`period` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `start_time` TIMESTAMP NULL DEFAULT NULL COMMENT '本期开始时间',
  `end_time` TIMESTAMP NULL DEFAULT NULL COMMENT '本期结束时间',
  `current_period` INT(11) NULL DEFAULT NULL COMMENT '本期数值',
  `flag` INT(11) NULL DEFAULT NULL COMMENT '是否当前期    1 表示当前期   0   表示不是当前期',
  `book_id` BIGINT(20) NULL DEFAULT NULL COMMENT '账套id',
  `company_id` VARCHAR(45) NULL DEFAULT NULL COMMENT '公司id',
  `end_flag` INT(11) NULL DEFAULT NULL COMMENT '表示此账套对应的期间是否可以修改\n1  为结束    \n0  未结束',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_period_account_book_idx` (`book_id` ASC),
  CONSTRAINT `fk_period_account_book`
    FOREIGN KEY (`book_id`)
    REFERENCES `yjz_prod`.`account_book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '期表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`voucher_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `subject_code` BIGINT(20) NULL DEFAULT NULL COMMENT '会计科目代码',
  `debit` DECIMAL(11,2) NULL DEFAULT NULL COMMENT ' 借方金额',
  `credit` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '贷方金额',
  `summary` VARCHAR(200) NULL DEFAULT NULL COMMENT '摘要',
  `voucher_id` BIGINT(20) NULL DEFAULT NULL COMMENT '记账主表id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_voucher_detail_voucher1_idx` (`voucher_id` ASC),
  CONSTRAINT `fk_voucher_detail_voucher1`
    FOREIGN KEY (`voucher_id`)
    REFERENCES `yjz_prod`.`voucher` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '记账详细表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`company_common_value` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_id` BIGINT(20) NULL DEFAULT NULL COMMENT '字典类型code，字典数据表外键',
  `value` VARCHAR(45) NULL DEFAULT NULL COMMENT '简称',
  `show_value` VARCHAR(45) NULL DEFAULT NULL COMMENT '显示名字',
  `company_id` VARCHAR(45) NULL DEFAULT NULL COMMENT '企业id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '修改时间',
  PRIMARY KEY (`id`),
  INDEX `fk_company_common_value_company_common_type1_idx` (`type_id` ASC),
  CONSTRAINT `fk_company_common_value_company_common_type1`
    FOREIGN KEY (`type_id`)
    REFERENCES `yjz_prod`.`company_common_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '企业通用配置值';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`company_common_type` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_code` VARCHAR(45) NULL DEFAULT NULL COMMENT '企业通用配置表code',
  `type_name` VARCHAR(45) NULL DEFAULT NULL COMMENT '字典类型名称',
  `company_id` VARCHAR(45) NULL DEFAULT NULL COMMENT '企业id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '企业通用配置表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`voucher_template` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `voucher_word` VARCHAR(25) NULL DEFAULT NULL COMMENT '凭证字',
  `bill_num` INT(11) NULL DEFAULT NULL COMMENT '附单据',
  `audit_user` VARCHAR(45) NULL DEFAULT NULL COMMENT '审核人',
  `posting_user` VARCHAR(45) NULL DEFAULT NULL COMMENT '过账人',
  `touching_user` VARCHAR(45) NULL DEFAULT NULL COMMENT '制单人',
  `type_name` VARCHAR(45) NULL DEFAULT NULL COMMENT '模板类别名称',
  `name` VARCHAR(45) NULL DEFAULT NULL COMMENT '模板名称',
  `company_id` VARCHAR(45) NULL DEFAULT NULL COMMENT '企业id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00" COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '记账凭证模板主表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`voucher_template_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `summary` VARCHAR(200) NULL DEFAULT NULL COMMENT '摘要',
  `subject_code` BIGINT(20) NULL DEFAULT NULL COMMENT '会计科目代码',
  `debit` DECIMAL(11,2) NULL DEFAULT NULL COMMENT ' 借方金额',
  `credit` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '贷方金额',
  `template_id` BIGINT(20) NULL DEFAULT NULL COMMENT '记账主表id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_voucher_template_detail_voucher_template1_idx` (`template_id` ASC),
  CONSTRAINT `fk_voucher_template_detail_voucher_template1`
    FOREIGN KEY (`template_id`)
    REFERENCES `yjz_prod`.`voucher_template` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '记账模板详细表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`subject_length` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT ' 主键',
  `length` INT(11) NULL DEFAULT NULL,
  `level` INT(11) NULL DEFAULT NULL COMMENT '层级',
  `book_id` BIGINT(20) NULL DEFAULT NULL COMMENT '账套id',
  `company_id` VARCHAR(45) NULL DEFAULT NULL COMMENT '企业id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_subject_length_account_book1_idx` (`book_id` ASC),
  CONSTRAINT `fk_subject_length_account_book1`
    FOREIGN KEY (`book_id`)
    REFERENCES `yjz_prod`.`account_book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '定义会计科目长度表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`users` (
  `username` VARCHAR(256) NULL DEFAULT NULL,
  `password` VARCHAR(256) NULL DEFAULT NULL,
  `enabled` TINYINT(1) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `yjz_prod`.`authorities` (
  `username` VARCHAR(256) NULL DEFAULT NULL,
  `authority` VARCHAR(256) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `yjz_prod`.`login_history` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(256) NULL DEFAULT NULL COMMENT '用户名',
  `login_ip` VARCHAR(64) NULL DEFAULT NULL COMMENT '登录IP',
  `login_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `login_times` BIGINT(20) NULL DEFAULT NULL COMMENT '登录次数',
  `account_book_id` BIGINT(20) NULL DEFAULT NULL COMMENT '当前登录所操作的账套ID',
  `login_result` TINYINT(1) NULL DEFAULT NULL COMMENT '登录是否成功 \n1：成功\n0：失败',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `idx_login_time` USING BTREE (`login_time` DESC),
  INDEX `idx_login_result` USING BTREE (`login_result` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `yjz_prod`.`subject_balance` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `subject_code` BIGINT(20) NOT NULL COMMENT '科目代码',
  `initial_debit_balance` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '期初借方金额',
  `initial_credit_balance` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '期初贷方金额',
  `period_debit_occur` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '本期借方发生额',
  `period_credit_occur` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '本期贷方发生额',
  `year_debit_occur` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '本年借方累计发生额',
  `year_credit_occur` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '本年贷方累计发生额',
  `terminal_debit_balance` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '期末借方余额',
  `terminal_credit_balance` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '期末贷方余额',
  `book_id` BIGINT(20) NOT NULL COMMENT '账套id',
  `period_id` BIGINT(20) NOT NULL,
  `profit_loss_occur_amount` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '损益类科目实际发生额',
  `profit_loss_total_occur_amount` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '损益类科目本年累计发生额',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `subject_code_UNIQUE` (`subject_code` ASC, `period_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '科目余额表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`action_log` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `action` VARCHAR(256) NULL DEFAULT NULL COMMENT '操作名称',
  `states` VARCHAR(45) NULL DEFAULT NULL COMMENT '状态',
  `description` VARCHAR(256) NULL DEFAULT NULL COMMENT '描述',
  `operator` VARCHAR(45) NULL DEFAULT NULL COMMENT '操作人',
  `address` VARCHAR(64) NULL DEFAULT NULL COMMENT ' 客户端IP',
  `os` VARCHAR(128) NULL DEFAULT NULL COMMENT '操作系统',
  `pc_name` VARCHAR(128) NULL DEFAULT NULL COMMENT '计算机名',
  `action_time` TIMESTAMP NULL DEFAULT NULL COMMENT '操作时间',
  `method_name` VARCHAR(512) NULL DEFAULT NULL COMMENT '方法名',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `yjz_prod`.`balance_sheet` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `code` BIGINT(20) NULL DEFAULT NULL,
  `parent_code` BIGINT(20) NULL DEFAULT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `year_begin_exp` VARCHAR(200) NULL DEFAULT NULL,
  `period_end_exp` VARCHAR(200) NULL DEFAULT NULL,
  `need_sum` INT(2) NULL DEFAULT NULL COMMENT '是否需要合计 0 需要 1 不需要',
  `level` INT(2) NULL DEFAULT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '资产负债';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`profit_template` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(45) NULL DEFAULT NULL COMMENT '项目',
  `month_exp` VARCHAR(200) NULL DEFAULT NULL COMMENT '本月金额',
  `last_month_exp` VARCHAR(200) NULL DEFAULT NULL COMMENT '上年金额',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '利润模板表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`cash_flow` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cash_code` VARCHAR(100) NULL DEFAULT NULL COMMENT '代码',
  `name` VARCHAR(100) NULL DEFAULT NULL COMMENT '项目',
  `static_code` VARCHAR(20) NULL DEFAULT NULL,
  `calcu_type` INT(2) UNSIGNED NULL DEFAULT NULL COMMENT '0 加 1 减',
  `from_flag` INT(2) UNSIGNED NULL DEFAULT NULL COMMENT '0 填入 1计算',
  `money` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '金额',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '现金流量基本数据表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`cash_flow_data` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cash_code` VARCHAR(100) NULL DEFAULT NULL COMMENT '代码',
  `subject_code` BIGINT(30) NULL DEFAULT NULL COMMENT '科目代码',
  `relative_subject_code` BIGINT(30) NULL DEFAULT NULL COMMENT '对方科目代码',
  `money` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '金额',
  `book_id` BIGINT(20) NULL DEFAULT NULL COMMENT '账套id',
  `period_id` BIGINT(20) NULL DEFAULT NULL COMMENT '期间id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  `voucher_id` BIGINT(20) NULL DEFAULT NULL COMMENT '记账凭证id',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '现金流量数据表';

CREATE TABLE IF NOT EXISTS `yjz_prod`.`profit_period` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(45) NULL DEFAULT NULL COMMENT '项目',
  `fix` INT(1) NULL DEFAULT NULL COMMENT '是否可修改：1:固定',
  `month_exp` VARCHAR(200) NULL DEFAULT NULL COMMENT '本期金额公式',
  `month_money` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '本月金额',
  `last_month_exp` VARCHAR(200) NULL DEFAULT NULL COMMENT '上期金额公式',
  `last_month_money` DECIMAL(11,2) NULL DEFAULT NULL COMMENT '上期金额',
  `book_id` BIGINT(20) NULL DEFAULT NULL COMMENT '账套id',
  `current_period` INT(11) NULL DEFAULT NULL COMMENT '当前期数',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` TIMESTAMP NOT NULL DEFAULT "2015-01-01 00:00:00",
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '利润表';


DELIMITER $$

USE `yjz_prod`$$
CREATE TRIGGER `account_book_BEFORE_UPDATE` BEFORE UPDATE ON `account_book` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `account_subject_BEFORE_UPDATE` BEFORE UPDATE ON `account_subject` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `dict_type_BEFORE_UPDATE` BEFORE UPDATE ON `dict_type` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `dict_value_BEFORE_UPDATE` BEFORE UPDATE ON `dict_value` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `account_subject_template_BEFORE_UPDATE` BEFORE UPDATE ON `account_subject_template` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `voucher_BEFORE_UPDATE` BEFORE UPDATE ON `voucher` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `period_BEFORE_UPDATE` BEFORE UPDATE ON `period` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `voucher_detail_BEFORE_UPDATE` BEFORE UPDATE ON `voucher_detail` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `company_common_value_BEFORE_UPDATE` BEFORE UPDATE ON `company_common_value` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `company_common_type_BEFORE_UPDATE` BEFORE UPDATE ON `company_common_type` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `voucher_template_BEFORE_UPDATE` BEFORE UPDATE ON `voucher_template` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `voucher_template_detail_BEFORE_UPDATE` BEFORE UPDATE ON `voucher_template_detail` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE TRIGGER `subject_length_BEFORE_UPDATE` BEFORE UPDATE ON `subject_length` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE DEFINER = CURRENT_USER TRIGGER `yjz_prod`.`subject_balance_BEFORE_UPDATE` BEFORE UPDATE ON `subject_balance` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END

$$

USE `yjz_prod`$$
CREATE DEFINER = CURRENT_USER TRIGGER `yjz_prod`.`balance_sheet_BEFORE_UPDATE` BEFORE UPDATE ON `balance_sheet` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE  TRIGGER `yjz_prod`.`profit_BEFORE_UPDATE` BEFORE UPDATE ON `profit_template` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE DEFINER = CURRENT_USER TRIGGER `yjz_prod`.`cash_flow_BEFORE_UPDATE` BEFORE UPDATE ON `cash_flow` FOR EACH ROW
BEGIN
   SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE DEFINER = CURRENT_USER TRIGGER `yjz_prod`.`cash_flow_data_BEFORE_UPDATE` BEFORE UPDATE ON `cash_flow_data` FOR EACH ROW
BEGIN
	SET NEW.`modify_time` = NOW();
END
$$

USE `yjz_prod`$$
CREATE DEFINER = CURRENT_USER TRIGGER `yjz_prod`.`profit_period_BEFORE_UPDATE` BEFORE UPDATE ON `profit_period` FOR EACH ROW
BEGIN
   SET NEW.`modify_time` = NOW();
END
$$


DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
