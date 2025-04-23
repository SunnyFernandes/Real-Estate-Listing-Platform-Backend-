package com.relp.controller.location;

import com.relp.payload.location.LocationDto;
import com.relp.payload.user.UserDto;
import com.relp.repository.user.UserRepository;
import com.relp.service.auth.JWTService;
import com.relp.service.location.LocationService;
import com.relp.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.SimpleTimeZone;

@RestController
@RequestMapping("/api/relp/location")
public class LocationController {

    private JWTService jwtService;
    private UserService userService;
    private LocationService locationService;
    public LocationController(JWTService jwtService, UserService userService, LocationService locationService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.locationService = locationService;
    }

    @PostMapping("/add-location")
    public ResponseEntity<LocationDto> addLocation(
            @RequestHeader("Authorization") String token,
            @RequestBody LocationDto locationDto
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email!=null){
            LocationDto locationDto1 = locationService.addLocation(locationDto);
            if(locationDto1 != null){
                return new ResponseEntity<>(locationDto1, HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update-location")
    public ResponseEntity<LocationDto> updateLocationbyId(
            @RequestHeader("Authorization") String token,
            @RequestBody LocationDto locationDto
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email!=null) {
            LocationDto locationDto1 = locationService.updateLocation(locationDto);
            if (locationDto1 != null) {
                return new ResponseEntity<>(locationDto1, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/get-location-by-id")
    public ResponseEntity<LocationDto> getLocationById(
            @RequestHeader("Authorization") String token,
            @RequestParam int id
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email!=null){
            LocationDto locationById = locationService.getLocationById(id);
            if(locationById!=null){
                return new ResponseEntity<>(locationById,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-all-location")
    public ResponseEntity<List<LocationDto>> getAllLocation(
            @RequestHeader("Authorization") String token
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email!=null){
            List<LocationDto> allLocation = locationService.getAllLocation();
            return new ResponseEntity<>(allLocation,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete-location-by-id")
    public ResponseEntity<?> deleteLocationById(
            @RequestHeader("Authorization") String token,
            @RequestParam int id
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email!=null){
            locationService.deleteById(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
