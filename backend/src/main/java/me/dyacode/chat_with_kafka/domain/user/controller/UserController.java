package me.dyacode.chat_with_kafka.domain.user.controller;

import lombok.RequiredArgsConstructor;
import me.dyacode.chat_with_kafka.domain.user.dto.UserDto;
import me.dyacode.chat_with_kafka.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
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
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDto.Request userDto) throws Exception {
        userService.create(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<HttpStatus> updateUser(@PathVariable Long id, @ModelAttribute UserDto.Request userDto) throws Exception {
        userService.update(id, userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}