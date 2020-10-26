package org.fastttrackit.onlineshop.service;

import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.persistance.UserRepository;
import org.fastttrackit.onlineshop.transfer.user.CreateUserRequest;
import org.fastttrackit.onlineshop.transfer.user.GetUsersRequest;
import org.fastttrackit.onlineshop.transfer.user.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;



    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(CreateUserRequest request){
        LOGGER.info("Creating user {}", request);

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole().name());

        User savedUser = userRepository.save(user);

        return mapUserResponse(savedUser);
    }

    public UserResponse getUserResponse(long id){
        LOGGER.info("Retrieving user {}", id);

        User user = getUser(id);
        return mapUserResponse(user);
    }

    public User getUser(long id) {
        LOGGER.info("Retrieving user {}", id);

        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
    }

    @Transactional
    public Page<UserResponse> getUsers(GetUsersRequest request, Pageable pageable) {
        Page<User> page = userRepository.findByOptionalCriteria(request.getPartialFirstName(), request.getPartialLastName(), pageable);

        List<UserResponse> userDtos = new ArrayList<>();

        for (User user : page.getContent()){
            UserResponse userResponse = mapUserResponse(user);
            userDtos.add(userResponse);
        }

        return new PageImpl<>(userDtos, pageable, page.getTotalElements());
    }

    public UserResponse updateUser(long id, CreateUserRequest request ) {
        LOGGER.info("Updating user {}: {}", id, request);

        User user = getUser(id);
        user.setRole(request.getRole().name());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User updatedUser = userRepository.save(user);


        return mapUserResponse(updatedUser);
    }

    public void deleteUser(long id){
        LOGGER.info("Deleting user {}", id);
        userRepository.deleteById(id);
    }

    private UserResponse mapUserResponse (User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setRole(user.getRole());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());

        return userResponse;
    }

}
