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

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
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
}
