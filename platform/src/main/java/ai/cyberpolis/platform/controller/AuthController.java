package ai.cyberpolis.platform.controller;

import ai.cyberpolis.platform.entity.User;
import ai.cyberpolis.platform.model.AuthenticationRequest;
import ai.cyberpolis.platform.model.RegisterRequest;
import ai.cyberpolis.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class
AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        return authService.signup(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        System.out.println(authenticationRequest);
        return authService.login(authenticationRequest);
    }

    @GetMapping("/getInfo")
    public ResponseEntity getInfo(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }
}
