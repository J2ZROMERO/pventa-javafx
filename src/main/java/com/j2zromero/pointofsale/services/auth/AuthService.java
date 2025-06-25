package com.j2zromero.pointofsale.services.auth;

import com.j2zromero.pointofsale.models.user.User;
import com.j2zromero.pointofsale.repositories.auth.AuthRepository;
import org.checkerframework.checker.units.qual.A;

import java.sql.SQLException;

public class AuthService {
    private AuthRepository authRepository = new AuthRepository();

    public User login(String email, String password) throws SQLException {
                return authRepository.login(email,password);
    }
    }
