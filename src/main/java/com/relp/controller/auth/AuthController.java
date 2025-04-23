package com.relp.controller.auth;

import com.relp.payload.user.JWTTokenDto;
import com.relp.payload.user.LoginDto;
import com.relp.payload.user.UserDto;
import com.relp.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relp/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/add-buyer")
    public ResponseEntity<UserDto> addUser(
            @RequestBody UserDto userDto
    ){
        UserDto email = authService.findEmail(userDto);
        if(email==null){
            UserDto mobile = authService.findMobile(userDto.getMobile());
            if(mobile==null){
                UserDto userAdded = authService.addUser(userDto);
                return new ResponseEntity<>(userAdded,HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/add-admin")
    public ResponseEntity<UserDto> addAdmin(
            @RequestBody UserDto userDto
    ){
        UserDto email = authService.findEmail(userDto);
        if(email==null){
            UserDto mobile = authService.findMobile(userDto.getMobile());
            if(mobile==null){
                UserDto userAdded = authService.addAdmin(userDto);
                return new ResponseEntity<>(userAdded,HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/add-seller")
    public ResponseEntity<UserDto> addSeller(
            @RequestBody UserDto userDto
    ){
        UserDto email = authService.findEmail(userDto);
        if(email==null){
            UserDto mobile = authService.findMobile(userDto.getMobile());
            if(mobile==null){
                UserDto userAdded = authService.addSeller(userDto);
                return new ResponseEntity<>(userAdded,HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto loginDto
    ) {
        String token = authService.verifyUser(loginDto);
        if(token != null){
            JWTTokenDto tokenDto = new JWTTokenDto();
            tokenDto.setToken(token);
            tokenDto.setType("JWT");
            return new ResponseEntity<>(tokenDto,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
