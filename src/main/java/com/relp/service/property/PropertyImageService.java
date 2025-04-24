package com.relp.service.property;

import com.relp.entity.Property;
import com.relp.entity.PropertyImage;
import com.relp.payload.property.PropertyDto;
import com.relp.payload.property.PropertyImageDto;
import com.relp.repository.PropertyImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PropertyImageService {
    private PropertyService propertyService;
    private PropertyImageRepository propertyImageRepository;
    private ModelMapper modelMapper;

    public PropertyImageService(PropertyService propertyService, PropertyImageRepository propertyImageRepository, ModelMapper modelMapper) {
        this.propertyService = propertyService;
        this.propertyImageRepository = propertyImageRepository;
        this.modelMapper = modelMapper;
    }

    public PropertyImageDto addImageUrl(String url, int propertyId) {
        PropertyDto propertyById = propertyService.findPropertyById(propertyId);
        if(propertyById!=null){
            Property property = propertyService.convertDtoToEntity(propertyById);
            PropertyImage propertyImage = new PropertyImage();
            propertyImage.setUrl(url);
            propertyImage.setProperty(property);
            PropertyImage save = propertyImageRepository.save(propertyImage);
            return convertEntityToDto(save);
        }
        return null;
    }

    PropertyImageDto convertEntityToDto(PropertyImage propertyImage){
        return modelMapper.map(propertyImage, PropertyImageDto.class);
    }
}
