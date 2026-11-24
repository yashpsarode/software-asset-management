package com.assetmanagement.controller;

import com.assetmanagement.dto.ApiResponse;
import com.assetmanagement.dto.AuthRequest;
import com.assetmanagement.dto.AuthResponse;
import com.assetmanagement.dto.RegisterRequest;
import com.assetmanagement.model.Team;
import com.assetmanagement.model.User;
import com.assetmanagement.repository.TeamRepository;
import com.assetmanagement.repository.UserRepository;
import com.assetmanagement.security.JwtTokenProvider;
import com.assetmanagement.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
                          UserRepository userRepository, TeamRepository teamRepository,
                          PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);
        User user = userDetailsService.getFullUser(request.getUsername());
        String teamName = user.getTeam() != null ? user.getTeam().getName() : "Unassigned";

        AuthResponse authResponse = new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole(), teamName);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", authResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        Team team = null;
        if (request.getTeamId() != null) {
            team = teamRepository.findById(request.getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + request.getTeamId()));
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole(),
                team
        );

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.ok("User registered successfully", savedUser));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.getFullUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("User details retrieved", user));
    }
}
