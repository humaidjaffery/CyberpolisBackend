package ai.cyberpolis.platform.service;

import ai.cyberpolis.platform.entity.User;
import ai.cyberpolis.platform.model.AuthenticationRequest;
import ai.cyberpolis.platform.model.RegisterRequest;
import ai.cyberpolis.platform.repository.UserRepository;
import ai.cyberpolis.platform.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtService;
    private AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public ResponseEntity<String> signup(RegisterRequest registerRequest){
        System.out.println(registerRequest);
        if(registerRequest.getEmail() == null || registerRequest.getDisplayName() == null || registerRequest.getPassword() == null){
            return ResponseEntity.badRequest().body("Username, email, or password cannot be null");
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())){
            return ResponseEntity.badRequest().body("Account with that email already exists");
        }

        if(userRepository.existsByDisplayName(registerRequest.getDisplayName())) {
            return ResponseEntity.badRequest().body("Account with that username already exists");
        }

        User u = User.builder()
                .displayName(registerRequest.getDisplayName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .currency(10000)
                .build();
        User user = userRepository.save(u);

        return ResponseEntity.ok(jwtService.generateJwtTokenFromEmail(user));
    }

    public ResponseEntity<String> login(AuthenticationRequest authenticationRequest) {
        Authentication authentication;
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Bad Credentials", HttpStatus.NOT_FOUND);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(jwtService.generateJwtTokenFromEmail(userDetails)) ;
    }

    public void subtractTokens(User user, Integer cost){
        user.setCurrency(Math.max(0, user.getCurrency() - cost));
        userRepository.save(user);
    }

    public int getTokens(String userEmail) throws Exception {
        return userRepository.findByEmail(userEmail).orElseThrow(Exception::new).getCurrency();
    }

    public int addTokens(User user, int tokens) throws Exception {
        user.setCurrency(user.getCurrency() + tokens);
        userRepository.save(user);
        return user.getCurrency();
    }
}
