package com.example.fragrance.model;

import com.example.fragrance.ENUM.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING) // Đảm bảo enum được lưu dưới dạng chuỗi trong database
    @Column(nullable = false)
    private Role role; // CUSTOMER, ADMIN

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
}
