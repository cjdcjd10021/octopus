package cn.throwx.octopus.server.repository.mapper;

import cn.throwx.octopus.server.model.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int insertSelective(User user);

    User selectByPrimaryKey(Long id);

    User selectByUsername(@Param("username") String username);
}
