/*==============================================================*/
/* Table: STEAM_APP                                             */
/*==============================================================*/
CREATE TABLE STEAM_APP
(
   APPID                INT NOT NULL COMMENT '应用号',
   TYPE                 ENUM('GAME','CONFIG','APPLICATION','MUSIC','VIDEO') COMMENT '类型',
   NAME                 VARCHAR(255) COMMENT '名称',
   FREE                 BOOL COMMENT '免费',
   ORI_PRICE            INT COMMENT '原价',
   BUY_PRICE            INT COMMENT '购入价格',
   BUY_TYPE             ENUM('FREE','GIFT','DOLLAR','RMB','RUB','BUNDLE') COMMENT '购入方式',
   BUNDLE_ID            VARCHAR(64) COMMENT '捆绑包ID',
   CATEGORY             VARCHAR(64) COMMENT '分类',
   STAGE                ENUM('TOBUY','BUYING','BUYED','COLLECT') COMMENT '卡牌购买阶段',
   CARD                 BOOL COMMENT '有卡',
   FINISHED             BOOL COMMENT '玩完',
   ACHIEVEMENTS         SMALLINT COMMENT '总成就数',
   DONE_ACHIEVE         SMALLINT COMMENT '完成成就数',
   RELEASE_DATE         DATE COMMENT '发售日期',
   BUY_DATE             DATE COMMENT '购入日期',
   PLAYED_MINITES       INT COMMENT '运行时长',
   BADGE_TIME           DATETIME COMMENT '领徽章时间',
   BADGE_LEVEL          SMALLINT COMMENT '徽章等级',
   BACKGROUND           TINYINT COMMENT '背景数',
   EMOTION              TINYINT COMMENT '表情数',
   DESCRIPTION          TEXT COMMENT '描述',
   PRIMARY KEY (APPID)
);

ALTER TABLE STEAM_APP COMMENT 'STEAM应用';


/*==============================================================*/
/* Table: STEAM_CATEGORY                                        */
/*==============================================================*/
CREATE TABLE STEAM_CATEGORY
(
   CODE                 VARCHAR(16) NOT NULL COMMENT '主键',
   SERIAL_NO            VARCHAR(8) COMMENT '显示序号',
   NAME                 VARCHAR(16) COMMENT '名称',
   SHOW_NAME            VARCHAR(32) COMMENT '显示名称=序号+名称',
   PRIMARY KEY (CODE)
);

ALTER TABLE STEAM_CATEGORY COMMENT 'STEAM应用分类';


/*==============================================================*/
/* Table: STEAM_DLC                                             */
/*==============================================================*/
CREATE TABLE STEAM_DLC
(
   APPID                INT NOT NULL COMMENT '应用号',
   NAME                 VARCHAR(255) COMMENT '名称',
   FREE                 BOOL COMMENT '免费',
   ORI_PRICE            INT COMMENT '原价',
   BUY_PRICE            INT COMMENT '购入价格',
   BUY_TYPE             ENUM('FREE','GIFT','DOLLAR','RMB','RUB','BUNDLE') COMMENT '购入方式',
   BUNDLE_ID            VARCHAR(64) COMMENT '捆绑包ID',
   RELEASE_DATE         DATE COMMENT '发售日期',
   BUY_DATE             DATE COMMENT '购入日期',
   PARENT_ID            INT NOT NULL COMMENT '父应用ID',
   PRIMARY KEY (APPID)
);

ALTER TABLE STEAM_DLC COMMENT 'STEAMDLC';


/*==============================================================*/
/* Table: STEAM_BUNDLE                                          */
/*==============================================================*/
CREATE TABLE STEAM_BUNDLE
(
   BUNDLEID             VARCHAR(64) NOT NULL COMMENT '捆绑包号',
   NAME                 VARCHAR(255) COMMENT '名称',
   CORPORATION          VARCHAR(16) COMMENT '公司',
   PACK_ID              VARCHAR(64) COMMENT '编号',
   CURRENCY             ENUM('FREE','GIFT','DOLLAR','RMB','RUB','BUNDLE') COMMENT '货币',
   PRICE                INT COMMENT '价格',
   PRIMARY KEY (BUNDLEID)
);

ALTER TABLE STEAM_BUNDLE COMMENT 'STEAM捆绑包';


/*==============================================================*/
/* Table: STEAM_TRADE                                           */
/*==============================================================*/
CREATE TABLE STEAM_TRADE
(
   ID                   VARCHAR(16) NOT NULL COMMENT '主键',
   APPID                INT NOT NULL COMMENT '应用号',
   TRADE_FLAG           ENUM('BUY','SELL') COMMENT '买卖标识',
   TRADE_TYPE           ENUM('CARD','PACK','FOIL','BACKGROUND','EMOTION') COMMENT '交易类型',
   TRADE_NAME           VARCHAR(255) COMMENT '交易名称',
   ONSHELF_TIME         DATETIME COMMENT '上架时间',
   QUANTITY             TINYINT COMMENT '数量',
   PRIMARY KEY (ID)
);

ALTER TABLE STEAM_TRADE COMMENT 'STEAM交易单';


/*==============================================================*/
/* Table: STEAM_WISHLIST                                        */
/*==============================================================*/
CREATE TABLE STEAM_WISHLIST
(
   APPID                INT NOT NULL COMMENT '应用号',
   TYPE                 ENUM('GAME','DLC','SUB','BUNDLE','MUSIC','VIDEO') COMMENT '类型',
   SALE_PRICE           INT COMMENT '原价',
   CURRENT_PRICE        INT COMMENT '当前价格',
   LOWEST_PRICE         INT COMMENT '最低价格',
   OFF_RATE             TINYINT COMMENT '折扣率',
   SALE_DATE            DATE COMMENT '销售日期',
   NAME                 VARCHAR(255) COMMENT '名称',
   TRANS_NAME           VARCHAR(255) COMMENT '译名',
   LINK                 VARCHAR(255) COMMENT '合辑链接',
   PRIMARY KEY (APPID)
);

ALTER TABLE STEAM_WISHLIST COMMENT 'STEAM愿望单';


/*==============================================================*/
/* Table: STEAM_WORKSET                                         */
/*==============================================================*/
CREATE TABLE STEAM_WORKSET
(
   ID                   INT NOT NULL COMMENT '主键',
   GOODS_NAME           VARCHAR(255) COMMENT '货品名称',
   APP_NAME             VARCHAR(255) COMMENT '应用名称',
   TRADER               VARCHAR(255) COMMENT '交易对象',
   PRIMARY KEY (ID)
);

ALTER TABLE STEAM_WORKSET COMMENT 'STEAM工作集';


/*==============================================================*/
/* Table: STEAM_HANDLER_DLC                                     */
/*==============================================================*/
CREATE TABLE STEAM_HANDLER_DLC
(
   APPID                INT NOT NULL COMMENT '应用号',
   HAS_DLC              BOOL COMMENT '是否有DLC',
   WANT                 BOOL COMMENT '是否需求',
   TOTAL                SMALLINT COMMENT 'DLC总数量',
   OWNED                SMALLINT COMMENT 'DLC拥有数',
   ALIVE                SMALLINT COMMENT 'DLC存活数',
   PRIMARY KEY (APPID)
);

ALTER TABLE STEAM_HANDLER_DLC COMMENT 'STEAM整理DLC';


/*==============================================================*/
/* Table: STEAM_HANDLER_IMAGE                                   */
/*==============================================================*/
CREATE TABLE STEAM_HANDLER_IMAGE
(
   APPID                INT NOT NULL COMMENT '应用号',
   SCANED               BOOL COMMENT '是否已扫',
   HAND_DATE            DATE COMMENT '扫描日期',
   PRIMARY KEY (APPID)
);

ALTER TABLE STEAM_HANDLER_IMAGE COMMENT 'STEAM整理图像';


/*==============================================================*/
/* Table: STEAM_HANDLER_NEW                                     */
/*==============================================================*/
CREATE TABLE STEAM_HANDLER_NEW
(
   APPID                INT NOT NULL COMMENT '应用号',
   TYPE                 VARCHAR(16) COMMENT '类型',
   ANALYSED             BOOL COMMENT '已解析',
   PRIMARY KEY (APPID)
);

ALTER TABLE STEAM_HANDLER_NEW COMMENT 'STEAM整理新APP';


/*==============================================================*/
/* Table: STEAM_THIRD_TOOLS_CARD                                */
/*==============================================================*/
CREATE TABLE STEAM_THIRD_TOOLS_CARD
(
   GAME                 VARCHAR(255) COMMENT '名称',
   OWNED                TINYINT COMMENT '拥有',
   UNIQEE               TINYINT COMMENT '唯一',
   CARDS                TINYINT COMMENT '卡牌数',
   BADGE_LVL            TINYINT COMMENT '徽章等级',
   SET_PRICE            VARCHAR(255) COMMENT '整套卡牌价格',
   PRICE_DIFF           VARCHAR(255) COMMENT '价格差',
   CARD_AVG             VARCHAR(255) COMMENT '卡牌均价',
   BOOSTER_AVG          VARCHAR(255) COMMENT '补充包均价',
   BOOSTER_PERCENT      VARCHAR(255) COMMENT '补充包涨跌',
   EMOTE_AVG            VARCHAR(255) COMMENT '表情均价',
   BG_AVG               VARCHAR(255) COMMENT '背景均价',
   AVG_QTY              VARCHAR(255) COMMENT '平均在售数量',
   DISCOUNT             VARCHAR(255) COMMENT '折扣',
   ADDED                DATE COMMENT '添加日期',
   APPID                INT NOT NULL COMMENT '应用号',
   PRIMARY KEY (APPID)
);

ALTER TABLE STEAM_THIRD_TOOLS_CARD COMMENT 'STEAM第三方工具卡牌';
