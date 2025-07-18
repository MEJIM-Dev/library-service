package com.me.library_service.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "app_user_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserRole  extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_fk")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_fk")
    private Role role;

}
