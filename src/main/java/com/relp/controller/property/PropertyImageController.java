package com.relp.controller.property;

import com.relp.payload.property.PropertyDto;
import com.relp.payload.property.PropertyImageDto;
import com.relp.payload.user.UserDto;
import com.relp.service.AWS.BucketService;
import com.relp.service.auth.JWTService;
import com.relp.service.property.PropertyImageService;
import com.relp.service.property.PropertyService;
import com.relp.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/relp/upload-image")
public class PropertyImageController {

    private PropertyService propertyService;
    private JWTService jwtService;
    private UserService userService;
    private BucketService bucketService;
    private PropertyImageService propertyImageService;
    public PropertyImageController(PropertyService propertyService, JWTService jwtService, UserService userService, BucketService bucketService, PropertyImageService propertyImageService) {
        this.propertyService = propertyService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.bucketService = bucketService;
        this.propertyImageService = propertyImageService;
    }

    @PostMapping(path = "/upload/file/{bucketName}/property/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PropertyImageDto>> uploadImage(
            @RequestHeader("Authorization") String token,
            @PathVariable String bucketName,
            @PathVariable int propertyId,
            @RequestParam("files")List<MultipartFile> files
    )throws IOException {
        String jwtToken = token.substring(8, token.length() - 1);
        String emailId = jwtService.getEmailId(jwtToken);
        UserDto email = userService.findEmail(emailId);
        List<PropertyImageDto> savePhotos = null;
        if(email!= null){
            PropertyDto propertyById = propertyService.findPropertyById(propertyId);
            if(propertyById == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            savePhotos = new ArrayList<>();

            for(MultipartFile file : files){
                String url = bucketService.uploadfile(bucketName, propertyById, file);
                PropertyImageDto propertyImageDto = propertyImageService.addImageUrl(url, propertyId);
                savePhotos.add(propertyImageDto);
            }
        }
        return new ResponseEntity<>(savePhotos,HttpStatus.ACCEPTED);
    }
}
