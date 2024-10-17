package me.dyacode.chat_with_kafka.domain.user.controller;

import me.dyacode.chat_with_kafka.domain.user.dto.UserDto;
import me.dyacode.chat_with_kafka.domain.user.entity.User;
import me.dyacode.chat_with_kafka.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseBody
    public List<UserDto.Response> getAllUsers() {
        return userService.getAllUsers().stream().map(UserDto.Response::new).toList();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UserDto.Response getUserById(@PathVariable Long id) {
        return userService.getUserById(id).map(UserDto.Response::new).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    @ResponseBody
    public UserDto.Response createUser(@RequestBody UserDto.Request userDto) {
        User user = userService.createUser(userDto);
        return new UserDto.Response(user);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public UserDto.Response updateUser(@PathVariable Long id, @RequestBody UserDto.Request userDto) {
        User user = userService.updateUser(id, userDto);
        return new UserDto.Response(user);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}