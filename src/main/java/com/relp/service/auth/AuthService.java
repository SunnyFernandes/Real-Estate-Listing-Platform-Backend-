package com.relp.service.auth;

import com.relp.entity.User;
import com.relp.payload.user.LoginDto;
import com.relp.payload.user.UserDto;
import com.relp.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private JWTService jwtService;

    public AuthService(UserRepository userRepository, ModelMapper modelMapper, JWTService jwtService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
    }

    public UserDto findEmail(UserDto userDto) {
        Optional<User> opEmail = userRepository.findByEmail(userDto.getEmail());
        if(opEmail.isPresent()){
            return convertEntityToDto(opEmail.get());
        }
        return null;
    }

    UserDto convertEntityToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    User convertDtoToEntity(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    public UserDto findMobile(String mobile) {
        Optional<User> opMobile = userRepository.findByMobile(mobile);
        if(opMobile.isPresent()){
            return convertEntityToDto(opMobile.get());
        }
        return null;
    }

    public UserDto addUser(UserDto userDto) {
        String hashpw = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
        userDto.setPassword(hashpw);
        userDto.setRole("ROLE_BUYER");
        User user = convertDtoToEntity(userDto);
        User save = userRepository.save(user);
        return convertEntityToDto(save);

    }

    public UserDto addAdmin(UserDto userDto) {
        String hashpw = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
        userDto.setPassword(hashpw);
        userDto.setRole("ROLE_ADMIN");
        User user = convertDtoToEntity(userDto);
        User save = userRepository.save(user);
        return convertEntityToDto(save);
    }

    public UserDto addSeller(UserDto userDto) {
        String hashpw = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(10));
        userDto.setPassword(hashpw);
        userDto.setRole("ROLE_SELLER");
        User user = convertDtoToEntity(userDto);
        User save = userRepository.save(user);
        return convertEntityToDto(save);
    }

    public String verifyUser(LoginDto loginDto) {
        Optional<User> opEmail = userRepository.findByEmail(loginDto.getEmail());
        if(opEmail.isPresent()){
            boolean checkpw = BCrypt.checkpw(loginDto.getPassword(), opEmail.get().getPassword());
            if(checkpw==true){
                return jwtService.generateToken(opEmail.get().getEmail());
            }
        }
        return null;
    }
}
