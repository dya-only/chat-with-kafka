package me.dyacode.chat_with_kafka.domain.user.service;

import me.dyacode.chat_with_kafka.domain.user.dto.UserDto;
import me.dyacode.chat_with_kafka.domain.user.entity.User;
import me.dyacode.chat_with_kafka.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(UserDto.Request userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User(userDto, null);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserDto.Request userDetails) {
        return userRepository.findById(id).map(user -> {
            user.update(userDetails, null);
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}