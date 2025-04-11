package com.example.BeBanHang.service;


import com.example.BeBanHang.model.User;
import com.example.BeBanHang.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser (){
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String getEmail(Long userId){
        if(userId== null){
            log.info("User id có giá trị null ");
            return null;
        }
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User không tồn tại"));
        return user.getEmail();
    }


}