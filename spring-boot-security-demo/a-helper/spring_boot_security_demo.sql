/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : spring_boot_security_demo

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 21/03/2023 15:41:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for authorize
-- ----------------------------
DROP TABLE IF EXISTS `authorize`;
CREATE TABLE `authorize`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `p_id` int UNSIGNED NOT NULL COMMENT 'parentId',
  `authorize_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称（前后端授权）',
  `authorize_name_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称中文（前端）',
  `node_type` tinyint NOT NULL COMMENT '节点类型（1：文件夹 2：页面 3：按钮）',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标地址（前端）',
  `sort` int UNSIGNED NOT NULL COMMENT '排序号',
  `link_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面对应的地址（前端）',
  `level` int UNSIGNED NOT NULL COMMENT '层次（和节点类型不一样）',
  `is_delete` int UNSIGNED NULL DEFAULT 0 COMMENT '删除（0：否 NULL：是）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `is_delete_and_authorize_name`(`is_delete` ASC, `authorize_name` ASC) USING BTREE COMMENT 'is_delete、authorize_name唯一组合索引',
  INDEX `sort`(`node_type` ASC, `sort` ASC, `level` ASC) USING BTREE COMMENT '普通组合索引排序'
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authorize
-- ----------------------------
INSERT INTO `authorize` VALUES (1, 0, 'system_setting', '系统配置', 1, NULL, 1, NULL, 1, 0);
INSERT INTO `authorize` VALUES (2, 1, 'user', '用户管理', 2, NULL, 1, NULL, 2, 0);
INSERT INTO `authorize` VALUES (3, 2, 'user:insert', '用户管理-新增', 3, NULL, 1, NULL, 3, 0);
INSERT INTO `authorize` VALUES (4, 2, 'user:select', '用户管理-查询', 3, NULL, 2, NULL, 3, 0);
INSERT INTO `authorize` VALUES (5, 2, 'user:update', '用户管理-编辑', 3, NULL, 3, NULL, 3, 0);
INSERT INTO `authorize` VALUES (6, 2, 'user:delete', '用户管理-删除', 3, NULL, 4, NULL, 3, 0);
INSERT INTO `authorize` VALUES (7, 1, 'role', '角色管理', 2, NULL, 1, NULL, 2, 0);
INSERT INTO `authorize` VALUES (8, 7, 'role:insert', '角色管理-新增', 3, NULL, 1, NULL, 3, 0);
INSERT INTO `authorize` VALUES (9, 7, 'role:select', '角色管理-查询', 3, NULL, 2, NULL, 3, 0);
INSERT INTO `authorize` VALUES (10, 7, 'role:update', '角色管理-编辑', 3, NULL, 3, NULL, 3, 0);
INSERT INTO `authorize` VALUES (11, 7, 'role:delete', '角色管理-删除', 3, NULL, 4, NULL, 3, 0);
INSERT INTO `authorize` VALUES (12, 1, 'authorize', '权限管理', 2, NULL, 1, NULL, 2, 0);
INSERT INTO `authorize` VALUES (13, 12, 'authorize:insert', '权限管理-新增', 3, NULL, 1, NULL, 3, 0);
INSERT INTO `authorize` VALUES (14, 12, 'authorize:select', '权限管理-查询', 3, NULL, 2, NULL, 3, 0);
INSERT INTO `authorize` VALUES (15, 12, 'authorize:update', '权限管理-编辑', 3, NULL, 3, NULL, 3, 0);
INSERT INTO `authorize` VALUES (16, 12, 'authorize:delete', '权限管理-删除', 3, NULL, 4, NULL, 3, 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_name_cn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称中文',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` int UNSIGNED NULL DEFAULT 0 COMMENT '删除（0：否 NULL：是）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `is_delete_and_role_name`(`is_delete` ASC, `role_name` ASC) USING BTREE COMMENT 'is_delete、role_name唯一组合索引'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'admin', '超级管理员', '2023-03-11 13:58:25', '2023-03-11 13:58:27', 0);

-- ----------------------------
-- Table structure for role_authorize
-- ----------------------------
DROP TABLE IF EXISTS `role_authorize`;
CREATE TABLE `role_authorize`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int UNSIGNED NOT NULL COMMENT '角色id',
  `authorize_id` int UNSIGNED NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色关联权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_authorize
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` char(172) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `sex` int UNSIGNED NOT NULL COMMENT '性别（0：女 1：男）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `is_ban` int UNSIGNED NOT NULL COMMENT '禁用（0：否 1：是）',
  `is_delete` int UNSIGNED NULL DEFAULT 0 COMMENT '删除（0：否 null：是）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `is_delete_and_username`(`is_delete` ASC, `username` ASC) USING BTREE COMMENT 'is_delete、username唯一组合索引'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$voVByDp713heOXHDigIUbu2mOIreU0nnwbKmokY5wYRvYRogaxGMG', '超级管理员', 0, '2023-03-10 15:42:10', '2023-03-10 15:42:13', '2023-03-10 15:42:20', 0, 0);
INSERT INTO `user` VALUES (2, 'user', '$2a$10$voVByDp713heOXHDigIUbu2mOIreU0nnwbKmokY5wYRvYRogaxGMG', '普通用户', 0, '2023-03-10 18:19:51', '2023-03-10 18:19:53', '2023-03-10 18:20:03', 0, NULL);
INSERT INTO `user` VALUES (3, 'user', '$2a$10$voVByDp713heOXHDigIUbu2mOIreU0nnwbKmokY5wYRvYRogaxGMG', '普通用户', 0, '2023-03-10 18:19:51', '2023-03-10 18:19:53', '2023-03-10 18:20:03', 0, 0);
INSERT INTO `user` VALUES (5, 'user', '$2a$10$voVByDp713heOXHDigIUbu2mOIreU0nnwbKmokY5wYRvYRogaxGMG', '普通用户', 0, '2023-03-10 18:19:51', '2023-03-10 18:19:53', '2023-03-10 18:20:03', 0, NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` int UNSIGNED NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户关联角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
