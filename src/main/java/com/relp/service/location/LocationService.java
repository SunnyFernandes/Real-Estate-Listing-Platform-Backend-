package com.relp.service.location;

import com.relp.entity.Location;
import com.relp.payload.location.LocationDto;
import com.relp.repository.LocationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private LocationRepository locationRepository;
    private ModelMapper modelMapper;

    public LocationService(LocationRepository locationRepository, ModelMapper modelMapper) {
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
    }

    public LocationDto addLocation(LocationDto locationDto) {
        Location location = convertDtoToEntity(locationDto);
        Optional<Location> byZipcode = locationRepository.findByZipcode(location.getZipcode());
        if(byZipcode.isPresent()){
            return null;
        }
        Location save = locationRepository.save(location);
        return convertEntityToDto(save);
    }

    LocationDto convertEntityToDto(Location location){
        return modelMapper.map(location, LocationDto.class);
    }

    Location convertDtoToEntity(LocationDto locationDto){
        return modelMapper.map(locationDto, Location.class);
    }

    public LocationDto updateLocation(LocationDto locationDto) {
        Location location = convertDtoToEntity(locationDto);
        Optional<Location> byZipcode = locationRepository.findByZipcode(location.getZipcode());
        if(byZipcode.isPresent()){
            Location location1 = byZipcode.get();
            location1.setAddress(location.getAddress());
            location1.setCity(location.getCity());
            location1.setState(location.getState());
            location1.setAddress(location.getAddress());
            Location save = locationRepository.save(location1);
            return convertEntityToDto(save);
        }
        return null;
    }

    public LocationDto getLocationById(int id) {
        Optional<Location> byId = locationRepository.findById((long) id);
        if(byId.isPresent()){
            return convertEntityToDto(byId.get());
        }
        return null;
    }

    public List<LocationDto> getAllLocation() {
        List<Location> all = locationRepository.findAll();
        List<LocationDto> collect = all.stream().map(a -> convertEntityToDto(a)).collect(Collectors.toList());
        return collect;
    }

    public void deleteById(int id) {
        Optional<Location> byId = locationRepository.findById((long) id);
        if(byId.isPresent()){
            locationRepository.deleteById((long)id);
        }
    }

    public Location findLocation(String location) {
        Optional<Location> byId = locationRepository.findById(Long.parseLong(location));
        return byId.get();
    }
}
