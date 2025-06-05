package cn.throwx.octopus.server.service;

import cn.throwx.octopus.server.model.entity.UserRole;
import cn.throwx.octopus.server.repository.UserDao;
import cn.throwx.octopus.server.repository.UserRoleDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final PasswordEncoder passwordEncoder;

    public Long register(String username, String rawPassword) {
        cn.throwx.octopus.server.model.entity.User exist = userDao.selectByUsername(username);
        if (exist != null) {
            throw new RuntimeException("user exists");
        }
        cn.throwx.octopus.server.model.entity.User user = new cn.throwx.octopus.server.model.entity.User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoleId(1L);
        userDao.insertSelective(user);
        return user.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        cn.throwx.octopus.server.model.entity.User user = userDao.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        UserRole role = userRoleDao.selectByPrimaryKey(user.getRoleId());
        String roleName = role != null ? role.getRoleName() : "USER";
        return User.withUsername(user.getUsername()).password(user.getPassword()).roles(roleName).build();
    }
}
