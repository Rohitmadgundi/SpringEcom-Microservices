package com.springecommicroservcies.user.controller;

import com.springecommicroservcies.user.dto.UserRequest;
import com.springecommicroservcies.user.dto.UserResponse;
import com.springecommicroservcies.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Data
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User Added Successfully");
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> users = userService.getAllUsers();

        if(!users.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(users);
        else
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id){
        log.info("user req : {}" , id);
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest user,
                                             @PathVariable String id){
        boolean updated = userService.updateUser(user,id);
        return updated ? ResponseEntity.ok("User updated successfully")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User Deleted Successfully");
    }


}
