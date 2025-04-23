package com.relp.controller.user;

import com.relp.payload.user.ChangePasswordDto;
import com.relp.payload.user.UserDto;
import com.relp.service.auth.JWTService;
import com.relp.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relp/user")
public class UserController {

    private JWTService jwtService;
    private UserService userService;


    public UserController(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

//    Create a DTO FOR THIS LIKE BE SPECFIC CHANGE PASSWORD
    @PutMapping("/update-user")
    public ResponseEntity<UserDto> updateUser(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangePasswordDto changePasswordDto
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email != null ){
            UserDto updatedUser = userService.updatePassword(changePasswordDto,email);
            return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<UserDto> getByEmail(
            @RequestHeader("Authorization") String token
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email!=null){
            return new ResponseEntity<>(email,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> getAllUser(
            @RequestHeader("Authorization") String token
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email != null){
            List<UserDto> allUser = userService.findAllUser();
            if(allUser!=null){
                return new ResponseEntity<>(allUser,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(
            @RequestHeader("Authorization") String token
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email != null){
            userService.deleteUser(emailId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete-user-by-id")
    public ResponseEntity<?> deleteUserById(
            @RequestHeader("Authorization") String token,
            @RequestParam int id
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email != null){
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
