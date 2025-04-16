SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `sex` int NOT NULL COMMENT '性别（0：女 1：男）',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `is_delete` int NOT NULL COMMENT '逻辑删除（0：否 1是）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE COMMENT '用户名唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Meta39', 1, '元', '2023-03-07 11:04:22', '2023-03-07 11:04:25', 0);

-- ----------------------------
-- Table structure for entity_a
-- ----------------------------
DROP TABLE IF EXISTS `entity_a`;
CREATE TABLE `entity_a`  (
                             `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of entity_a
-- ----------------------------
INSERT INTO `entity_a` VALUES (1, 'Meta');

-- ----------------------------
-- Table structure for entity_b
-- ----------------------------
DROP TABLE IF EXISTS `entity_b`;
CREATE TABLE `entity_b`  (
                             `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                             `a_id` int UNSIGNED NOT NULL,
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `a_id`(`a_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of entity_b
-- ----------------------------
INSERT INTO `entity_b` VALUES (1, 1, 'Metab');

-- ----------------------------
-- Table structure for entity_c
-- ----------------------------
DROP TABLE IF EXISTS `entity_c`;
CREATE TABLE `entity_c`  (
                             `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                             `b_id` int UNSIGNED NOT NULL,
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `b_id`(`b_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of entity_c
-- ----------------------------
INSERT INTO `entity_c` VALUES (1, 1, 'Metac');

-- ----------------------------
-- Table structure for entity_d
-- ----------------------------
DROP TABLE IF EXISTS `entity_d`;
CREATE TABLE `entity_d`  (
                             `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                             `c_id` int UNSIGNED NOT NULL,
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `c_id`(`c_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of entity_d
-- ----------------------------
INSERT INTO `entity_d` VALUES (1, 1, 'Metad');

-- ----------------------------
-- Table structure for entity_e
-- ----------------------------
DROP TABLE IF EXISTS `entity_e`;
CREATE TABLE `entity_e`  (
                             `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
                             `d_id` int UNSIGNED NOT NULL,
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `d_id`(`d_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of entity_e
-- ----------------------------
INSERT INTO `entity_e` VALUES (1, 1, 'Metae');

SET FOREIGN_KEY_CHECKS = 1;
