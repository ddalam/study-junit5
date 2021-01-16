package com.study.junit5.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser() {
        User user = new User();
        return userRepository.save(user);
    }
}
