package com.example.ResidentMicroservice.service;

import com.example.ResidentMicroservice.model.GoogleLogin;
import com.example.ResidentMicroservice.model.Login;
import com.example.ResidentMicroservice.model.User;

public interface UserService {

    User createUser(User user);

    Login loginUser(User user);

    Login googleLogin(GoogleLogin dto);
}
