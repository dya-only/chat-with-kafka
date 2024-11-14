package me.dyacode.chat_with_kafka.domain.user.service;

import lombok.RequiredArgsConstructor;
import me.dyacode.chat_with_kafka.domain.user.dto.UserDto;
import me.dyacode.chat_with_kafka.domain.user.entity.User;
import me.dyacode.chat_with_kafka.domain.user.repository.UserRepository;
import me.dyacode.chat_with_kafka.global.util.Image;
import me.dyacode.chat_with_kafka.global.util.ImageUploader;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ImageUploader imageUploader;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void create(UserDto.Request userDto) throws Exception {
        String plainPassword = userDto.getPassword();
        String encryptedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        Image image = imageUploader.upload(userDto.getImage(), "user");

        userDto.setPassword(encryptedPassword);
        User user = new User(userDto, image.getStorePath());
        userRepository.save(user);
    }

    public void update(Long id, UserDto.Request userDto) throws Exception {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        String plainPassword = userDto.getPassword();
        String encryptedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        Image image = imageUploader.upload(userDto.getImage(), "user");

        userDto.setPassword(encryptedPassword);
        user.update(userDto, image.getStorePath());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}