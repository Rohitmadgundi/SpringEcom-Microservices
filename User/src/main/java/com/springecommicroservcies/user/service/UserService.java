package com.springecommicroservcies.user.service;


import com.springecommicroservcies.user.dto.AddressDTO;
import com.springecommicroservcies.user.dto.UserRequest;
import com.springecommicroservcies.user.dto.UserResponse;
import com.springecommicroservcies.user.model.Address;
import com.springecommicroservcies.user.model.User;
import com.springecommicroservcies.user.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class UserService {

    private final UserRepository userRepository;

    public void addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromUserRequest(userRequest,user);
        userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public Optional<UserResponse> getUser(String id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponse);
    }

    public boolean updateUser(UserRequest userRequest,String id) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromUserRequest(userRequest,existingUser);
                    userRepository.save(existingUser);
                    return true;
                })
                .orElse(false);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public void updateUserFromUserRequest(UserRequest userRequest,User user){
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null){
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            user.setAddress(address);

        }

    }
    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }
        return response;
    }

}
