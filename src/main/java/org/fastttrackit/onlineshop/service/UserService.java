package org.fastttrackit.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.persistance.UserRepository;
import org.fastttrackit.onlineshop.transfer.user.CreateUserRequest;
import org.fastttrackit.onlineshop.transfer.user.GetUsersRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;


    @Autowired
    public UserService(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public User createUser(CreateUserRequest request){
        LOGGER.info("Creating user {}", request);

        User user = objectMapper.convertValue(request, User.class);

        return userRepository.save(user);
    }

    public User getUser(long id) {
        LOGGER.info("Retrieving product {}", id);

        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
    }

    public Page<User> getUsers(GetUsersRequest request, Pageable pageable) {
        return userRepository.findByOptionalCriteria(request.getPartialFirstName(), request.getPartialLastName(), pageable);
    }

    public User updateUser(long id, CreateUserRequest request ) {
        LOGGER.info("Updating user {}: {}", id, request);

        User user = getUser(id);

        BeanUtils.copyProperties(request, user);

        return userRepository.save(user);
    }

    public void deleteUser(long id){
        LOGGER.info("Deleting user {}", id);
        userRepository.deleteById(id);
    }
}
