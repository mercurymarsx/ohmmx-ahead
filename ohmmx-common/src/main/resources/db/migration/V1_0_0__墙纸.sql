/*==============================================================*/
/* Table: WALLPAPER_TRACE                                       */
/*==============================================================*/
CREATE TABLE WALLPAPER_TRACE
(
   ID                   VARCHAR(16) NOT NULL COMMENT '主键',
   MONTH                VARCHAR(10) COMMENT '月份',
   START                INT NOT NULL COMMENT '开始',
   END                  INT NOT NULL COMMENT '结束',
   TOTAL                SMALLINT NOT NULL COMMENT '总计',
   PRIMARY KEY (ID),
   UNIQUE KEY AK_MONTH (MONTH)
);

ALTER TABLE WALLPAPER_TRACE COMMENT '墙纸跟踪';


/*==============================================================*/
/* Table: WALLPAPER_LIST                                        */
/*==============================================================*/
CREATE TABLE WALLPAPER_LIST
(
   FILENUM              VARCHAR(12) NOT NULL COMMENT '编号',
   MONTH                VARCHAR(10) COMMENT '月份',
   TYPE                 ENUM('RAW','FHD') NOT NULL COMMENT '类型',
   FILEURL              VARCHAR(100) COMMENT '地址',
   PRIMARY KEY (FILENUM, TYPE)
);

ALTER TABLE WALLPAPER_LIST COMMENT '墙纸列表';


/*==============================================================*/
/* Table: WALLPAPER_TASK                                        */
/*==============================================================*/
CREATE TABLE WALLPAPER_TASK
(
   FILENUM              VARCHAR(12) NOT NULL COMMENT '编号',
   MONTH                VARCHAR(10) COMMENT '月份',
   TYPE                 ENUM('RAW','FHD') NOT NULL COMMENT '类型',
   REASON               VARCHAR(50) COMMENT '原因',
   PRIMARY KEY (FILENUM, TYPE)
);

ALTER TABLE WALLPAPER_TASK COMMENT '墙纸任务';

