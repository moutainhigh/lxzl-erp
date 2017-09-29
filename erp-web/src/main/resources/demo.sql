/*
Navicat MySQL Data Transfer

Source Server         : 10.2.50.36 dev
Source Server Version : 50622
Source Host           : 10.2.50.36:3306
Source Database       : demo

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2016-10-26 16:18:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `log`
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `execute_times` int(10) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_user` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log
-- ----------------------------
INSERT INTO `log` VALUES ('1', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('2', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('3', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('4', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('5', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('6', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('7', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('8', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('9', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('10', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('11', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('12', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('13', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('14', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('15', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('16', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('17', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('18', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('19', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('20', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:37:23');
INSERT INTO `log` VALUES ('21', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('22', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('23', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('24', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('25', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('26', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('27', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('28', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('29', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('30', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('31', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('32', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('33', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('34', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('35', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('36', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('37', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('38', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('39', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('40', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:38:29');
INSERT INTO `log` VALUES ('41', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('42', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('43', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('44', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('45', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('46', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('47', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('48', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('49', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('50', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('51', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('52', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('53', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('54', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('55', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('56', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('57', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('58', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('59', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('60', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:39:35');
INSERT INTO `log` VALUES ('61', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('62', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('63', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('64', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('65', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('66', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('67', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('68', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('69', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('70', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('71', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('72', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('73', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('74', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('75', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('76', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('77', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('78', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('79', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('80', '测试', '1', '3', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:40:40');
INSERT INTO `log` VALUES ('81', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('82', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('83', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('84', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('85', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('86', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('87', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('88', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('89', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('90', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('91', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('92', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('93', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('94', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('95', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('96', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('97', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('98', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('99', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('100', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:28:29');
INSERT INTO `log` VALUES ('101', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('102', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('103', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('104', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('105', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('106', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('107', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('108', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('109', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('110', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-10-11 17:32:04');
INSERT INTO `log` VALUES ('111', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('112', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('113', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('114', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('115', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('116', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('117', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('118', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('119', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('120', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('121', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('122', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('123', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('124', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('125', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('126', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('127', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('128', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('129', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('130', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('131', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('132', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('133', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('134', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('135', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('136', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('137', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('138', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('139', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('140', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('141', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('142', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('143', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('144', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('145', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('146', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('147', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('148', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('149', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('150', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('151', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('152', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('153', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('154', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('155', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('156', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('157', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('158', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('159', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('160', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('161', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('162', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('163', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('164', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('165', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('166', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('167', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('168', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('169', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('170', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('171', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('172', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('173', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('174', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('175', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('176', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('177', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('178', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('179', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('180', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('181', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('182', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('183', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('184', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('185', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('186', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('187', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('188', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('189', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('190', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('191', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('192', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('193', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('194', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('195', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('196', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('197', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('198', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('199', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');
INSERT INTO `log` VALUES ('200', '测试', '0', '0', 'SYS', '2015-09-09 14:02:15', 'SYS', '2016-08-30 10:00:49');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `nick` varchar(32) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `birthday` varchar(32) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `tel` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `last_login_ip` varchar(32) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_user` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_user` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `user_1`
-- ----------------------------
DROP TABLE IF EXISTS `user_1`;
CREATE TABLE `user_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `nick` varchar(32) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `birthday` varchar(32) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `tel` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `last_login_ip` varchar(32) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_user` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_user` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `user_2`
-- ----------------------------
DROP TABLE IF EXISTS `user_2`;
CREATE TABLE `user_2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `nick` varchar(32) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `birthday` varchar(32) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `tel` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `last_login_ip` varchar(32) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_user` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_user` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;