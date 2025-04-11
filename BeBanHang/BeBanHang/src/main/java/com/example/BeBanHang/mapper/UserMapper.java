package com.example.BeBanHang.mapper;

import com.example.BeBanHang.model.User;
import com.example.BeBanHang.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User INSTANCE = Mappers.getMapper(User.class);

    UserResponse userToUserResponse (User user);
}
