-- broadcasting_dev.QRTZ_CALENDARS definition

CREATE TABLE `QRTZ_CALENDARS`
(
    `SCHED_NAME`    varchar(120) NOT NULL,
    `CALENDAR_NAME` varchar(190) NOT NULL,
    `CALENDAR`      blob         NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.QRTZ_FIRED_TRIGGERS definition

CREATE TABLE `QRTZ_FIRED_TRIGGERS`
(
    `SCHED_NAME`        varchar(120) NOT NULL,
    `ENTRY_ID`          varchar(95)  NOT NULL,
    `TRIGGER_NAME`      varchar(190) NOT NULL,
    `TRIGGER_GROUP`     varchar(190) NOT NULL,
    `INSTANCE_NAME`     varchar(190) NOT NULL,
    `FIRED_TIME`        bigint(13)   NOT NULL,
    `SCHED_TIME`        bigint(13)   NOT NULL,
    `PRIORITY`          int(11)      NOT NULL,
    `STATE`             varchar(16)  NOT NULL,
    `JOB_NAME`          varchar(190) DEFAULT NULL,
    `JOB_GROUP`         varchar(190) DEFAULT NULL,
    `IS_NONCONCURRENT`  varchar(1)   DEFAULT NULL,
    `REQUESTS_RECOVERY` varchar(1)   DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
    KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
    KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
    KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
    KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.QRTZ_JOB_DETAILS definition

CREATE TABLE `QRTZ_JOB_DETAILS`
(
    `SCHED_NAME`        varchar(120) NOT NULL,
    `JOB_NAME`          varchar(190) NOT NULL,
    `JOB_GROUP`         varchar(190) NOT NULL,
    `DESCRIPTION`       varchar(250) DEFAULT NULL,
    `JOB_CLASS_NAME`    varchar(250) NOT NULL,
    `IS_DURABLE`        varchar(1)   NOT NULL,
    `IS_NONCONCURRENT`  varchar(1)   NOT NULL,
    `IS_UPDATE_DATA`    varchar(1)   NOT NULL,
    `REQUESTS_RECOVERY` varchar(1)   NOT NULL,
    `JOB_DATA`          blob         DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
    KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.QRTZ_LOCKS definition

CREATE TABLE `QRTZ_LOCKS`
(
    `SCHED_NAME` varchar(120) NOT NULL,
    `LOCK_NAME`  varchar(40)  NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.QRTZ_PAUSED_TRIGGER_GRPS definition

CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`
(
    `SCHED_NAME`    varchar(120) NOT NULL,
    `TRIGGER_GROUP` varchar(190) NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.QRTZ_SCHEDULER_STATE definition

CREATE TABLE `QRTZ_SCHEDULER_STATE`
(
    `SCHED_NAME`        varchar(120) NOT NULL,
    `INSTANCE_NAME`     varchar(190) NOT NULL,
    `LAST_CHECKIN_TIME` bigint(13)   NOT NULL,
    `CHECKIN_INTERVAL`  bigint(13)   NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.tbl_campaign definition

CREATE TABLE `tbl_campaign`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `name`         varchar(128) CHARACTER SET utf8mb4       DEFAULT NULL,
    `description`  varchar(255) CHARACTER SET utf8mb4       DEFAULT NULL,
    `start_time`   datetime                                 DEFAULT NULL,
    `end_time`     datetime                                 DEFAULT NULL,
    `priority`     varchar(8) COLLATE utf8mb4_vietnamese_ci DEFAULT NULL,
    `repeated`     tinyint(4)                               DEFAULT NULL,
    `created_by`   int(11)                                  DEFAULT NULL,
    `created_date` datetime                                 DEFAULT NULL,
    `updated_by`   int(11)                                  DEFAULT NULL,
    `updated_date` datetime                                 DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_vietnamese_ci;


-- broadcasting_dev.tbl_content definition

CREATE TABLE `tbl_content`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `name`         varchar(45)  DEFAULT NULL,
    `type`         varchar(16)  DEFAULT NULL,
    `uri`          varchar(128) DEFAULT NULL,
    `size`         double       DEFAULT NULL,
    `created_by`   int(11)      DEFAULT NULL,
    `created_date` datetime     DEFAULT NULL,
    `updated_by`   int(11)      DEFAULT NULL,
    `updated_date` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.tbl_device definition

CREATE TABLE `tbl_device`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `name`         varchar(45)  DEFAULT NULL,
    `imei`         varchar(45)  DEFAULT NULL,
    `description`  varchar(255) DEFAULT NULL,
    `location`     point        DEFAULT NULL,
    `created_by`   int(11)      DEFAULT NULL,
    `created_date` datetime     DEFAULT NULL,
    `updated_by`   int(11)      DEFAULT NULL,
    `updated_date` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.tbl_role definition

CREATE TABLE `tbl_role`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `name`         varchar(45) DEFAULT NULL,
    `type`         varchar(12) DEFAULT NULL,
    `created_by`   int(11)     DEFAULT NULL,
    `created_date` datetime    DEFAULT NULL,
    `updated_by`   int(11)     DEFAULT NULL,
    `updated_date` datetime    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 985626
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.tbl_user definition

CREATE TABLE `tbl_user`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `name`         varchar(255) DEFAULT NULL,
    `username`     varchar(32)  DEFAULT NULL,
    `password`     varchar(255) DEFAULT NULL,
    `phone`        varchar(16)  DEFAULT NULL,
    `email`        varchar(64)  DEFAULT NULL,
    `birthday`     date         DEFAULT NULL,
    `gender`       varchar(8)   DEFAULT NULL,
    `avatar`       varchar(255) DEFAULT NULL,
    `role_id`      int(11)      DEFAULT NULL,
    `created_by`   int(11)      DEFAULT NULL,
    `created_date` datetime     DEFAULT NULL,
    `updated_by`   int(11)      DEFAULT NULL,
    `updated_date` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 569548
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.QRTZ_TRIGGERS definition

CREATE TABLE `QRTZ_TRIGGERS`
(
    `SCHED_NAME`     varchar(120) NOT NULL,
    `TRIGGER_NAME`   varchar(190) NOT NULL,
    `TRIGGER_GROUP`  varchar(190) NOT NULL,
    `JOB_NAME`       varchar(190) NOT NULL,
    `JOB_GROUP`      varchar(190) NOT NULL,
    `DESCRIPTION`    varchar(250) DEFAULT NULL,
    `NEXT_FIRE_TIME` bigint(13)   DEFAULT NULL,
    `PREV_FIRE_TIME` bigint(13)   DEFAULT NULL,
    `PRIORITY`       int(11)      DEFAULT NULL,
    `TRIGGER_STATE`  varchar(16)  NOT NULL,
    `TRIGGER_TYPE`   varchar(8)   NOT NULL,
    `START_TIME`     bigint(13)   NOT NULL,
    `END_TIME`       bigint(13)   DEFAULT NULL,
    `CALENDAR_NAME`  varchar(190) DEFAULT NULL,
    `MISFIRE_INSTR`  smallint(2)  DEFAULT NULL,
    `JOB_DATA`       blob         DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    KEY `IDX_QRTZ_T_J` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
    KEY `IDX_QRTZ_T_C` (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
    KEY `IDX_QRTZ_T_G` (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
    KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
    KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
    KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
    KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
    KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
    KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
    KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
    KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`,
                                         `TRIGGER_STATE`) USING BTREE,
    CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.tbl_campaign_content definition

CREATE TABLE `tbl_campaign_content`
(
    `campaign_id` int(11) NOT NULL,
    `content_id`  int(11) NOT NULL,
    KEY `fk_cid_idx` (`campaign_id`),
    KEY `fk_content_idx` (`content_id`),
    CONSTRAINT `fk_campaign` FOREIGN KEY (`campaign_id`) REFERENCES `tbl_campaign` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_content` FOREIGN KEY (`content_id`) REFERENCES `tbl_content` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.tbl_campaign_device definition

CREATE TABLE `tbl_campaign_device`
(
    `campaign_id` int(11) NOT NULL,
    `device_id`   int(11) NOT NULL,
    KEY `fk_campaign_idx` (`campaign_id`),
    KEY `fk_device_idx` (`device_id`),
    KEY `fk_cp_idx` (`campaign_id`),
    KEY `fk_de_idx` (`device_id`),
    CONSTRAINT `fk_cp` FOREIGN KEY (`campaign_id`) REFERENCES `tbl_campaign` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_de` FOREIGN KEY (`device_id`) REFERENCES `tbl_device` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.tbl_chat_message definition

CREATE TABLE `tbl_chat_message`
(
    `id`        int(11)     NOT NULL AUTO_INCREMENT,
    `chat_id`   varchar(64)   DEFAULT NULL,
    `sender`    int(11)       DEFAULT NULL,
    `recipient` int(11)       DEFAULT NULL,
    `content`   varchar(2000) DEFAULT NULL,
    `send_date` datetime      DEFAULT NULL,
    `status`    varchar(16) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_chat_msg_idx` (`sender`),
    KEY `fk_chat_msg_recip_idx` (`recipient`),
    CONSTRAINT `fk_chat_msg_recip` FOREIGN KEY (`recipient`) REFERENCES `tbl_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_chat_msg_send` FOREIGN KEY (`sender`) REFERENCES `tbl_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 21
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.tbl_chat_room definition

CREATE TABLE `tbl_chat_room`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `chat_id`   varchar(64) DEFAULT NULL,
    `sender`    int(11)     DEFAULT NULL,
    `recipient` int(11)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_room_send_idx` (`sender`),
    KEY `fk_room_recip_idx` (`recipient`),
    CONSTRAINT `fk_room_recip` FOREIGN KEY (`recipient`) REFERENCES `tbl_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `fk_room_send` FOREIGN KEY (`sender`) REFERENCES `tbl_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.tbl_history definition

CREATE TABLE `tbl_history`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `uid`        int(11)     DEFAULT NULL,
    `ip_address` varchar(32) DEFAULT NULL,
    `login_time` datetime    DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_user_idx` (`uid`),
    CONSTRAINT `fk_user` FOREIGN KEY (`uid`) REFERENCES `tbl_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- broadcasting_dev.QRTZ_BLOB_TRIGGERS definition

CREATE TABLE `QRTZ_BLOB_TRIGGERS`
(
    `SCHED_NAME`    varchar(120) NOT NULL,
    `TRIGGER_NAME`  varchar(190) NOT NULL,
    `TRIGGER_GROUP` varchar(190) NOT NULL,
    `BLOB_DATA`     blob DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    KEY `SCHED_NAME` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.QRTZ_CRON_TRIGGERS definition

CREATE TABLE `QRTZ_CRON_TRIGGERS`
(
    `SCHED_NAME`      varchar(120) NOT NULL,
    `TRIGGER_NAME`    varchar(190) NOT NULL,
    `TRIGGER_GROUP`   varchar(190) NOT NULL,
    `CRON_EXPRESSION` varchar(120) NOT NULL,
    `TIME_ZONE_ID`    varchar(80) DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.QRTZ_SIMPLE_TRIGGERS definition

CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`
(
    `SCHED_NAME`      varchar(120) NOT NULL,
    `TRIGGER_NAME`    varchar(190) NOT NULL,
    `TRIGGER_GROUP`   varchar(190) NOT NULL,
    `REPEAT_COUNT`    bigint(7)    NOT NULL,
    `REPEAT_INTERVAL` bigint(12)   NOT NULL,
    `TIMES_TRIGGERED` bigint(10)   NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;


-- broadcasting_dev.QRTZ_SIMPROP_TRIGGERS definition

CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`
(
    `SCHED_NAME`    varchar(120) NOT NULL,
    `TRIGGER_NAME`  varchar(190) NOT NULL,
    `TRIGGER_GROUP` varchar(190) NOT NULL,
    `STR_PROP_1`    varchar(512)   DEFAULT NULL,
    `STR_PROP_2`    varchar(512)   DEFAULT NULL,
    `STR_PROP_3`    varchar(512)   DEFAULT NULL,
    `INT_PROP_1`    int(11)        DEFAULT NULL,
    `INT_PROP_2`    int(11)        DEFAULT NULL,
    `LONG_PROP_1`   bigint(20)     DEFAULT NULL,
    `LONG_PROP_2`   bigint(20)     DEFAULT NULL,
    `DEC_PROP_1`    decimal(13, 4) DEFAULT NULL,
    `DEC_PROP_2`    decimal(13, 4) DEFAULT NULL,
    `BOOL_PROP_1`   varchar(1)     DEFAULT NULL,
    `BOOL_PROP_2`   varchar(1)     DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;
