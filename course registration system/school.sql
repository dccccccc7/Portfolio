/*
 Navicat Premium Data Transfer

 Source Server         : ROOT
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : localhost:3308
 Source Schema         : school

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : 65001

 Date: 24/11/2021 15:05:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for allclass
-- ----------------------------
DROP TABLE IF EXISTS `allclass`;
CREATE TABLE `allclass`  (
  `mid` int(5) NOT NULL,
  `grade` year NOT NULL,
  `classnum` int(3) NOT NULL,
  PRIMARY KEY (`mid`, `grade`, `classnum`) USING BTREE,
  CONSTRAINT `AllMid` FOREIGN KEY (`mid`) REFERENCES `major` (`mid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of allclass
-- ----------------------------
INSERT INTO `allclass` VALUES (1, 2019, 1);
INSERT INTO `allclass` VALUES (2, 2019, 1);

-- ----------------------------
-- Table structure for arrange
-- ----------------------------
DROP TABLE IF EXISTS `arrange`;
CREATE TABLE `arrange`  (
  `cid` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `weekday` int(3) NOT NULL,
  `dayorder` int(3) NOT NULL,
  `place` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `beginWeek` int(3) NULL DEFAULT NULL,
  `endWeek` int(3) NULL DEFAULT NULL,
  PRIMARY KEY (`cid`, `weekday`, `dayorder`) USING BTREE,
  CONSTRAINT `TiCid` FOREIGN KEY (`cid`) REFERENCES `course` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of arrange
-- ----------------------------
INSERT INTO `arrange` VALUES ('123', 3, 1, '5区107', 1, 17);
INSERT INTO `arrange` VALUES ('123', 4, 2, '5区107', 1, 17);

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `cid` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tid` int(11) NULL DEFAULT NULL,
  `num` int(11) NULL DEFAULT 0,
  `total` int(11) NULL DEFAULT NULL,
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `credit` decimal(5, 1) NULL DEFAULT NULL,
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `CoTid`(`tid`) USING BTREE,
  CONSTRAINT `CoTid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`tid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('123', 'web1', 1, 1, 1, 'http', 1.0);

-- ----------------------------
-- Table structure for currentchoose
-- ----------------------------
DROP TABLE IF EXISTS `currentchoose`;
CREATE TABLE `currentchoose`  (
  `sid` int(11) NOT NULL,
  `cid` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `score` int(10) NULL DEFAULT NULL,
  `present` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`sid`, `cid`) USING BTREE,
  INDEX `CuCid`(`cid`) USING BTREE,
  CONSTRAINT `CuCid` FOREIGN KEY (`cid`) REFERENCES `course` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `CuSid` FOREIGN KEY (`sid`) REFERENCES `student` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of currentchoose
-- ----------------------------

-- ----------------------------
-- Table structure for currentsemester
-- ----------------------------
DROP TABLE IF EXISTS `currentsemester`;
CREATE TABLE `currentsemester`  (
  `year` year NOT NULL,
  `semester` int(3) NOT NULL,
  PRIMARY KEY (`year`, `semester`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_520_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of currentsemester
-- ----------------------------
INSERT INTO `currentsemester` VALUES (2024, 1);

-- ----------------------------
-- Table structure for flags
-- ----------------------------
DROP TABLE IF EXISTS `flags`;
CREATE TABLE `flags`  (
  `event` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `flag` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`event`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flags
-- ----------------------------
INSERT INTO `flags` VALUES ('CHOOSING', 1);

-- ----------------------------
-- Table structure for historychoose
-- ----------------------------
DROP TABLE IF EXISTS `historychoose`;
CREATE TABLE `historychoose`  (
  `sid` int(11) NOT NULL,
  `sname` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `cid` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `year` year NOT NULL,
  `semester` int(3) NOT NULL,
  `score` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`sid`, `cid`, `year`, `semester`) USING BTREE,
  CONSTRAINT `HisSid` FOREIGN KEY (`sid`) REFERENCES `student` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of historychoose
-- ----------------------------
INSERT INTO `historychoose` VALUES (1, '张凌霄', '101', 2020, 1, 90);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '101', 2020, 2, NULL);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '102', 2020, 1, 100);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '102', 2020, 2, 45);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '102', 2023, 2, 100);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '103', 2000, 1, 98);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '103', 2020, 1, 100);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '107', 2023, 1, 60);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '108', 2023, 1, 59);
INSERT INTO `historychoose` VALUES (1, '张凌霄', '202', 2020, 1, NULL);
INSERT INTO `historychoose` VALUES (2, '卡密萨马', '101', 2023, 2, 65);
INSERT INTO `historychoose` VALUES (2, '卡密萨马', '102', 2020, 2, 90);
INSERT INTO `historychoose` VALUES (2, '卡密萨马', '107', 2023, 1, 60);
INSERT INTO `historychoose` VALUES (2, '卡密萨马', '109', 2023, 1, 100);
INSERT INTO `historychoose` VALUES (2, '卡密萨马', '110', 2023, 1, 75);
INSERT INTO `historychoose` VALUES (3, 'wulunzun', '101', 2023, 2, 70);

-- ----------------------------
-- Table structure for historycourse
-- ----------------------------
DROP TABLE IF EXISTS `historycourse`;
CREATE TABLE `historycourse`  (
  `cid` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tid` int(11) NULL DEFAULT NULL,
  `tname` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `year` year NOT NULL,
  `semester` int(3) NOT NULL,
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `credit` decimal(5, 1) NULL DEFAULT NULL,
  PRIMARY KEY (`cid`, `year`, `semester`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of historycourse
-- ----------------------------
INSERT INTO `historycourse` VALUES ('101', '面向大数据的python编程', 1, '何', 2020, 2, 'python基础与部分爬虫抓取、数据处理基础库', 1.5);
INSERT INTO `historycourse` VALUES ('101', 'web设计', 1, '何', 2023, 2, 'JavaScript，html', 2.5);
INSERT INTO `historycourse` VALUES ('102', '概率与统计', 1, '何', 2020, 2, '分为概率论和数理统计两大部分，是数据分析的基础', 3.0);
INSERT INTO `historycourse` VALUES ('102', '高等数学', 1, '何', 2023, 2, '大学必修课程', 5.0);
INSERT INTO `historycourse` VALUES ('103', 'web设计', 1, '何', 2020, 2, 'web前端技术，包括html、css，以及一些前端框架的介绍', 3.0);
INSERT INTO `historycourse` VALUES ('104', '数据库系统(双语)', 1, '何', 2020, 2, '关系型数据库的基础知识，包括sql语言、关系代数、关系型数据理论等', 5.0);
INSERT INTO `historycourse` VALUES ('107', '创业基础', 1, '何', 2023, 1, '创新创业类别', 2.0);
INSERT INTO `historycourse` VALUES ('108', '高等数学(2)', 1, '何', 2023, 1, '高等数学', 5.5);
INSERT INTO `historycourse` VALUES ('109', '大学物理(初级)', 1, '何', 2023, 1, '提供给非强需求专业的初级大学物理', 3.0);
INSERT INTO `historycourse` VALUES ('110', '大学综合英语(2)', 1, '何', 2023, 1, NULL, 4.0);

-- ----------------------------
-- Table structure for major
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major`  (
  `mid` int(5) NOT NULL AUTO_INCREMENT,
  `mname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES (1, '软件类');
INSERT INTO `major` VALUES (2, '数据科学与大数据技术');

-- ----------------------------
-- Table structure for prechoose
-- ----------------------------
DROP TABLE IF EXISTS `prechoose`;
CREATE TABLE `prechoose`  (
  `sid` int(11) NOT NULL,
  `cid` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`sid`, `cid`) USING BTREE,
  INDEX `PrCid`(`cid`) USING BTREE,
  CONSTRAINT `PrCid` FOREIGN KEY (`cid`) REFERENCES `course` (`cid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PrSid` FOREIGN KEY (`sid`) REFERENCES `student` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of prechoose
-- ----------------------------
INSERT INTO `prechoose` VALUES (1, '123');

-- ----------------------------
-- Table structure for root
-- ----------------------------
DROP TABLE IF EXISTS `root`;
CREATE TABLE `root`  (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `rname` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `birthday` date NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of root
-- ----------------------------
INSERT INTO `root` VALUES (1, '张凌霄', '2001-07-02', '13552851503', '123456', 'god@qq.com');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `sname` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `birthday` date NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `mid` int(5) NULL DEFAULT NULL,
  `grade` year NULL DEFAULT NULL,
  `classnum` int(3) NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE,
  INDEX `StuClassInfo`(`mid`, `grade`, `classnum`) USING BTREE,
  CONSTRAINT `StuClassInfo` FOREIGN KEY (`mid`, `grade`, `classnum`) REFERENCES `allclass` (`mid`, `grade`, `classnum`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `StuMaj` FOREIGN KEY (`mid`) REFERENCES `major` (`mid`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '张凌霄', '2001-07-02', '13552879503', '123456', '冰窖口', '1832189171@qq.com', 1, 2019, 1);
INSERT INTO `student` VALUES (2, '卡密萨马', '2021-09-27', '88888888', '233', 'heaven', '114514@qq.com', 1, 2019, 1);
INSERT INTO `student` VALUES (3, 'wulunzun', '2021-08-30', '16567897625', '123456', '天安门', '31563@qq.com', 1, 2019, 1);
INSERT INTO `student` VALUES (4, '何', '2021-11-01', '11136278491', '123456', '北京', '312@com', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `tid` int(11) NOT NULL AUTO_INCREMENT,
  `tname` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `birthday` date NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, '何', '1990-01-11', '18374659486', '123456', 'he@sdu.edu.com');
INSERT INTO `teacher` VALUES (2, '潘', '1974-06-11', '13649284765', '123456', 'babyPan@sdu.edu.com');

SET FOREIGN_KEY_CHECKS = 1;
