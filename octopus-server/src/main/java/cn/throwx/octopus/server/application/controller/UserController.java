package cn.throwx.octopus.server.application.controller;

import cn.throwx.octopus.server.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterRequest request) {
        Long id = userService.register(request.getUsername(), request.getPassword());
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return map;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @PostMapping("/login")
    public Map<String, Object> login() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Please authenticate using HTTP Basic");
        return map;
    }
}
