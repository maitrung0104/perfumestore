package com.example.fragrance.controller;

import com.example.fragrance.dto.LoginRequest;
import com.example.fragrance.dto.MessageResponse;
import com.example.fragrance.dto.SignupRequest;
import com.example.fragrance.model.User;
import com.example.fragrance.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        // Kiểm tra xem username đã tồn tại chưa
        if (userService.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username đã được sử dụng!"));
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu có khớp không
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Mật khẩu và xác nhận mật khẩu không khớp!"));
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(signupRequest.getPassword());

        userService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("Đăng ký thành công!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpSession session) {
        if (userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            // Lưu thông tin đăng nhập vào session
            session.setAttribute("username", loginRequest.getUsername());
            return ResponseEntity.ok(new MessageResponse("Đăng nhập thành công!"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username hoặc mật khẩu không đúng!"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công!"));
    }
}