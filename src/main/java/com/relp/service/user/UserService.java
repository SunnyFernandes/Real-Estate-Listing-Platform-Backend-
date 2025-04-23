package com.relp.service.user;

import com.relp.entity.User;
import com.relp.payload.user.ChangePasswordDto;
import com.relp.payload.user.UserDto;
import com.relp.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto findEmail(String emailId) {
        Optional<User> opEmail = userRepository.findByEmail(emailId);
        if(opEmail.isPresent()){
            return convertEntityToDto(opEmail.get());
        }
        return null;
    }


    public UserDto convertEntityToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    public User convertDtoToEntity(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    //--------------------------------------------------------
    public UserDto updateUserDetails(UserDto userDto) {
        User user = convertDtoToEntity(userDto);
        User save = userRepository.save(user);
        return convertEntityToDto(save);
    }
    //--------------------------------------------------------


    public List<UserDto> findAllUser() {
        List<User> allUser = userRepository.findAll();
        List<UserDto> collect = allUser.stream().map(u -> convertEntityToDto(u)).collect(Collectors.toList());
        return collect;
    }

    public void deleteUser(String emailId) {
        Optional<User> byEmail = userRepository.findByEmail(emailId);
        if(byEmail.isPresent()){
            userRepository.deleteById(byEmail.get().getId());
        }
    }

    public void deleteById(int id) {
        Optional<User> opId = userRepository.findById((long) id);
        if(opId.isPresent()){
            userRepository.deleteById((long)id);
        }
    }

    public UserDto updatePassword(ChangePasswordDto changePasswordDto, UserDto userDto) {
        boolean checkpw = BCrypt.checkpw(changePasswordDto.getOldPassword(), userDto.getPassword());
        if(checkpw){
            User user = convertDtoToEntity(userDto);
            String hashpw = BCrypt.hashpw(changePasswordDto.getNewPassword(), BCrypt.gensalt(10));
            user.setPassword(hashpw);
            User save = userRepository.save(user);
            return convertEntityToDto(save);
        }
        return null;
    }
}
