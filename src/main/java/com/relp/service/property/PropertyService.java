package com.relp.service.property;

import com.relp.entity.Location;
import com.relp.entity.Property;
import com.relp.payload.property.PropertyDto;
import com.relp.payload.user.UserDto;
import com.relp.repository.PropertyRepository;
import com.relp.service.location.LocationService;
import com.relp.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    private PropertyRepository propertyRepository;
    private ModelMapper modelMapper;
    private UserService userService;
    private LocationService locationService;

    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, UserService userService, LocationService locationService) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.locationService = locationService;
    }

    public PropertyDto findProperty(PropertyDto propertyDto, UserDto email) {
        propertyDto.setUser(userService.convertDtoToEntity(email));
        Property property = convertDtoToEntity(propertyDto);
        property.setLocation(locationService.findLocation(propertyDto.getLocation()));
        Optional<Property> opTitle = propertyRepository.findByTitleAndUser(property.getTitle(),property.getUser().getId());
        if(opTitle.isPresent()){
            return null;
        }
        Property save = propertyRepository.save(property);
        return convertEntityToDto(save);

    }

    Property convertDtoToEntity(PropertyDto propertyDto){
        return modelMapper.map(propertyDto,Property.class);
    }
    PropertyDto convertEntityToDto(Property property){
        return modelMapper.map(property, PropertyDto.class);
    }

    public PropertyDto findPropertyById(int id) {
        Optional<Property> opId = propertyRepository.findById((long) id);
        if(opId.isPresent()){
            return convertEntityToDto(opId.get());
        }
        return null;
    }

    public List<PropertyDto> findAllProperty() {
        List<Property> allProperty = propertyRepository.findAll();
        List<PropertyDto> collect = allProperty.stream().map(p -> convertEntityToDto(p)).collect(Collectors.toList());
        return collect;
    }

    public PropertyDto updateProperty(PropertyDto propertyDto) {
        Property property = convertDtoToEntity(propertyDto);
        Location location = locationService.findLocation(propertyDto.getLocation());
        Optional<Property> opProperty = propertyRepository.findByTitle(property.getTitle());
        if(opProperty.isPresent()) {
            Property property1 = opProperty.get();
            property1.setProperty_type(property.getProperty_type());
            property1.setBathroom(property.getBathroom());
            property1.setBedroom(property.getBedroom());
            property1.setDescription(property.getDescription());
            property1.setLocation(location);
            property1.setSize(property.getSize());
            property1.setPrice(property.getPrice());
            Property save = propertyRepository.save(property1);
            return convertEntityToDto(save);
        }
        return null;
    }

    public void deletePropertyById(int id) {
        Optional<Property> byId = propertyRepository.findById((long) id);
        if(byId.isPresent()){
            propertyRepository.deleteById((long) id);
        }
    }

    public List<PropertyDto> propertySearchFilter(String param) {
        Optional<List<Property>> opPropertyList = propertyRepository.findByCityOrStateOrZipcode(param);
        if(opPropertyList.isPresent()) {
            List<Property> properties = opPropertyList.get();
            List<PropertyDto> collect = properties.stream().map(pl -> convertEntityToDto(pl)).collect(Collectors.toList());
            return collect;
        }
        return null;
    }
}
