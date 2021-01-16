package com.study.junit5.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다"));
    }

    public User createUser() {
        User user = new User();
        return userRepository.save(user);
    }
}
