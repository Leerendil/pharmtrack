package org.vsu.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.vsu.userservice.dto.UserDto;
import org.vsu.userservice.dto.UserResponse;
import org.vsu.userservice.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User mapToEntity(UserDto userDto);
    UserResponse mapToResponse(User entity);
}
