package cn.throwx.octopus.server.repository.mapper;

import cn.throwx.octopus.server.model.entity.UserRole;
import java.util.List;

public interface UserRoleMapper {
    UserRole selectByPrimaryKey(Long id);

    List<UserRole> selectAll();
}
