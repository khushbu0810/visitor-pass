package com.example.ResidentMicroservice.service.impl;

import com.example.ResidentMicroservice.config.security.JwtUtils;
import com.example.ResidentMicroservice.exception.DuplicateUserException;
import com.example.ResidentMicroservice.model.GoogleLogin;
import com.example.ResidentMicroservice.model.Login;
import com.example.ResidentMicroservice.model.Resident;
import com.example.ResidentMicroservice.model.User;
import com.example.ResidentMicroservice.repository.ResidentRepo;
import com.example.ResidentMicroservice.repository.UserRepo;
import com.example.ResidentMicroservice.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final ResidentRepo residentRepo;
    private final AuthenticationManager authenticationManager;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final JwtUtils jwtUtils;

    private static final String GOOGLE_DEFAULT_PASSWORD = "GOOGLE_LOGIN";

    @Autowired
    public UserServiceImpl(UserRepo userRepo,
                           PasswordEncoder passwordEncoder, ResidentRepo residentRepo,
                           AuthenticationManager authenticationManager,
                           GoogleIdTokenVerifier googleIdTokenVerifier,
                           JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.residentRepo = residentRepo;
        this.authenticationManager = authenticationManager;
        this.googleIdTokenVerifier=googleIdTokenVerifier;
        this.jwtUtils=jwtUtils;
    }

    @Override
    public User createUser(User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new DuplicateUserException(user.getUsername() + " already exists");
        }
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new DuplicateUserException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(user.getRole()==null || user.getRole().isEmpty()){
            user.setRole("RESIDENT");
        }
        //creating resident profile
        user.setAccountStatus(true);
        User savedUser = userRepo.save(user);
        createResidentProfile(savedUser);
        return savedUser;
    }

    @Override
    public Login loginUser(User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            User newUser = userRepo.findByEmail(user.getEmail());

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", newUser.getUserId().toString());
            claims.put("username", newUser.getUsername());
            claims.put("role", newUser.getRole());
            return new Login(jwtUtils.generateToken(newUser.getEmail(), claims),
                    newUser.getUserId(),newUser.getRole());
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    @Override
    public Login googleLogin(GoogleLogin request) {
        try {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(request.getIdToken());
            if (idToken == null) {
                throw new BadCredentialsException("Invalid Google Token");
            }
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String username = (String) payload.get("name");

            User user = userRepo.findByEmail(email);

            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(GOOGLE_DEFAULT_PASSWORD));
                if (request.getRole() == null || request.getRole().isBlank()) {
                    user.setRole("RESIDENT");
                } else {
                    user.setRole(request.getRole());
                }
                user.setAccountStatus(true);
                user = userRepo.save(user);

                // Create resident profile automatically
                createResidentProfile(user);
            }
            Map<String, Object> claims = new HashMap<>();

            claims.put("userId", user.getUserId().toString());
            claims.put("username", user.getUsername());
            claims.put("role", user.getRole());

            String jwt = jwtUtils.generateToken(user.getEmail(), claims);

            return new Login(jwt, user.getUserId(), user.getRole());

        } catch (Exception e) {
            throw new BadCredentialsException("Google Authentication Failed");
        }

    }

    //creating profile on signup /login
    private void createResidentProfile(User user) {
        if (!"RESIDENT".equalsIgnoreCase(user.getRole())) {
            return;
        }
        if (residentRepo.findByUserUserId(user.getUserId()).isPresent()) {
            return;
        }

        Resident resident = new Resident();
        resident.setUser(user);
        resident.setFullName(user.getUsername());
        resident.setEmail(user.getEmail());
        resident.setPhoneNumber("PROFILE_PENDING");
        resident.setTower("PROFILE_PENDING");
        resident.setFlatNumber("PROFILE_PENDING");

        residentRepo.save(resident);
    }
    
}
