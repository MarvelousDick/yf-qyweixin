/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Version : 50627
 Source Host           : localhost
 Source Database       : qyweixin

 Target Server Version : 50627
 File Encoding         : utf-8

 Date: 07/15/2017 17:29:59 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `qywx_authorize`
-- ----------------------------
DROP TABLE IF EXISTS `qywx_authorize`;
CREATE TABLE `qywx_authorize` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `agent_id` varchar(255) NOT NULL COMMENT '应用编号',
  `domain` varchar(255) NOT NULL COMMENT '授权域名',
  `state` varchar(255) NOT NULL DEFAULT '1' COMMENT '重定向参数',
  `snsapi_base` int(11) NOT NULL DEFAULT '0' COMMENT '授权作用域:1=snsapi_base,0=snsapi_userinfo ',
  `subscribe_url` varchar(255) NOT NULL COMMENT '关注url',
  `redirect_uri` varchar(255) NOT NULL COMMENT '回调地址',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态:0=不可以,1=可用',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Records of `qywx_authorize`
-- ----------------------------
BEGIN;
INSERT INTO `qywx_authorize` VALUES ('1', '16', 'http://qy.javen.1mfy.cn', '123', '0', '', 'http://qy.javen.1mfy.cn/qyoauth/custom', '1', '2017-07-08 16:43:17', '2017-07-08 16:43:20'), ('2', '3', 'http://qy.javen.1mfy.cn', '123456', '0', '', 'http://qy.javen.1mfy.cn/qyoauth/custom', '1', '2017-07-08 19:20:21', '2017-07-08 19:20:24'), ('3', '1000002', 'http://qy.javen.1mfy.cn', '1', '0', '', 'http://qy.javen.1mfy.cn/qyoauth/custom', '1', '0000-00-00 00:00:00', '0000-00-00 00:00:00');
COMMIT;

-- ----------------------------
--  Table structure for `qywx_config`
-- ----------------------------
DROP TABLE IF EXISTS `qywx_config`;
CREATE TABLE `qywx_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `token` varchar(255) NOT NULL DEFAULT 'Javen' COMMENT 'Token',
  `agent_id` varchar(11) NOT NULL COMMENT '应用编号',
  `corp_id` varchar(255) NOT NULL COMMENT '集团编号',
  `secret` varchar(255) NOT NULL COMMENT '私密',
  `encodingaeskey` varchar(255) NOT NULL DEFAULT '1' COMMENT 'EncodingAESKey',
  `rmid` varchar(255) NOT NULL COMMENT '随机数',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态:1=可用,2=禁用',
  `update_time` datetime NOT NULL COMMENT '创建时间',
  `create_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Records of `qywx_config`
-- ----------------------------
BEGIN;
INSERT INTO `qywx_config` VALUES ('1', 'Javen', '16', 'wxdbc631b5210be89f', 'IvgLYvR6qror_6Ai7S1HWWQKTP0hgaVORTsxf4B5AZ4', '0qWuCqiF5y75VKTxr9DQC4uH8k2S8ei8g7RjVW2aTay', '123456', '1', '2017-07-08 11:48:44', '2017-07-08 11:48:47'), ('2', 'Javen', '3', 'wx4463c2c14c8d0761', 'DDsh0LMj7KnQqi49ZweMnslejGPfHAiWSh9jSMCNCW0', 'uonk83Ysb8OKvoN5cumgXgsykuC5ixjEoMJBWDSNloo', '789', '1', '2017-07-08 19:41:49', '2017-07-08 19:41:51'), ('4', 'Javen', '1000002', 'wx4463c2c14c8d0761', 'uX0gYUB1ukBOxQRi52JYztSQhjDa1u7DJBicSF_egN4', '6NSmcvIkTNCyhA1kql7VcuLR25Xk6ioin87NUB2lFsn', '100', '1', '0000-00-00 00:00:00', '0000-00-00 00:00:00');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
