package com.example.BeBanHang.controller;


import com.example.BeBanHang.model.User;
import com.example.BeBanHang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("getAll")
    public ResponseEntity<List<User>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> userOptional = userService.getUserByEmail(user.getEmail());

        if (userOptional.isPresent()) {
               User userFromDb = userOptional.get();
            if (userFromDb.getPassword().equals(user.getPassword())) {
                return ResponseEntity.ok(userFromDb);
            } else {
                return ResponseEntity.badRequest().body("Sai mật khẩu!");
            }
        }
        return ResponseEntity.badRequest().body("Email không tồn tại!");
    }

    @GetMapping("/getEmail/{userId}")
    public ResponseEntity<String > getEmailById (@PathVariable("userId") Long userId){
        return new ResponseEntity<>(userService.getEmail(userId), HttpStatus.OK);
    }

}