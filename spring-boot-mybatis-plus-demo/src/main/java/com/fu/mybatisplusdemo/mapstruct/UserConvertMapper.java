package com.fu.mybatisplusdemo.mapstruct;

import com.fu.mybatisplusdemo.dto.UserDTO;
import com.fu.mybatisplusdemo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 注意
 * 和 MyBatis/Plus 同时使用时， mapstruct 导包为 import org.mapstruct.Mapper;别倒错了包。
 */
@Mapper
public interface UserConvertMapper {
    //定义单例
    UserConvertMapper INSTANCE = Mappers.getMapper(UserConvertMapper.class);

    List<UserDTO> userListToUserDTOList(List<User> userList);

    UserDTO userToUserDTO(User user);

}
