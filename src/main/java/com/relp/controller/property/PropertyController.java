package com.relp.controller.property;

import com.relp.payload.property.PropertyDto;
import com.relp.payload.user.UserDto;
import com.relp.service.auth.JWTService;
import com.relp.service.property.PropertyService;
import com.relp.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relp/property")
public class PropertyController {

    private PropertyService propertyService;
    private JWTService jwtService;
    private UserService userService;

    public PropertyController(PropertyService propertyService, JWTService jwtService, UserService userService) {
        this.propertyService = propertyService;
        this.jwtService = jwtService;
        this.userService = userService;
    }
    
    @PostMapping("/add-property")
    public ResponseEntity<PropertyDto> addProperty(
            @RequestHeader("Authorization") String token,
            @RequestBody PropertyDto propertyDto
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        PropertyDto property = null;
        if(email != null){
            property = propertyService.findProperty(propertyDto,email);
            if(property == null){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(property,HttpStatus.CREATED);
    }

    @GetMapping("/get-property-by-id")
    public ResponseEntity<PropertyDto> getPropertyById(
            @RequestParam int id
    ){
        PropertyDto propertyById = propertyService.findPropertyById(id);
        if(propertyById!=null){
            return new ResponseEntity<>(propertyById,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-all-property")
    public ResponseEntity<List<PropertyDto>> getAllProperty(){
        List<PropertyDto> allProperty = propertyService.findAllProperty();
        if(allProperty!=null){
            return new ResponseEntity<>(allProperty,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update-property")
    public ResponseEntity<PropertyDto> updateProperty(
            @RequestHeader("Authorization") String token,
            @RequestBody PropertyDto propertyDto
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email!=null) {
            PropertyDto propertyDto1 = propertyService.updateProperty(propertyDto);
            if (propertyDto1 != null) {
                return new ResponseEntity<>(propertyDto1, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/delete-property-by-id")
    public ResponseEntity<?> deletePropertyById(
            @RequestHeader("Authorization") String token,
            @RequestParam int id
    ){
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        if(email!=null){
            propertyService.deletePropertyById(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search-filter-property")
    public ResponseEntity<List<PropertyDto>> searchFilterProperty(
            @RequestParam String param
    ){
        List<PropertyDto> propertyDtos = propertyService.propertySearchFilter(param);
        if(propertyDtos!=null){
            return new ResponseEntity<>(propertyDtos,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
