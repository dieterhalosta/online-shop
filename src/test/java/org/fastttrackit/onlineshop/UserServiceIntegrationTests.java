package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.domain.UserRole;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.service.UserService;
import org.fastttrackit.onlineshop.steps.UserTestSteps;
import org.fastttrackit.onlineshop.transfer.user.CreateUserRequest;
import org.fastttrackit.onlineshop.transfer.user.GetUsersRequest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;


@SpringBootTest
public class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserTestSteps userTestSteps;

    @Test
    public void createUser_whenValidRequest_thenReturnCreatedUser(){
        userTestSteps.createUser();
    }

    @Test
    public void getUser_whenExistingUser_thenReturnUser(){
        User user =  userTestSteps.createUser();

        User userResponse = userService.getUser(user.getId());

        assertThat(userResponse, notNullValue());
        assertThat(userResponse.getId(), is(user.getId()));
        assertThat(userResponse.getRole(), is(user.getRole()));
        assertThat(userResponse.getFirstName(), is(user.getFirstName()));
        assertThat(userResponse.getLastName(), is(user.getLastName()));
    }

    @Test
    void getUsers_whenOneExistingUser_thenReturnPageOfOneUser(){
        User user =  userTestSteps.createUser();

        Page<User> usersPage = userService.getUsers(new GetUsersRequest(), PageRequest.of(0, 1000));

        assertThat(usersPage, CoreMatchers.notNullValue());
        assertThat(usersPage.getTotalElements(), greaterThanOrEqualTo(1L));
        assertThat(usersPage.getContent(), contains(user));

    }

    @Test
    public void updateUser_whenExistingUser_thenReturnUpdatedUser(){
        User user =  userTestSteps.createUser();
        CreateUserRequest request = new CreateUserRequest();
        request.setRole(UserRole.ADMIN);
        request.setFirstName("Test from Test");
        request.setLastName("LastNameTest");

        User updatedUser = userService.updateUser(user.getId(), request);

        assertThat(updatedUser, CoreMatchers.notNullValue());
        assertThat(updatedUser.getId(), is(user.getId()));
        assertThat(updatedUser.getRole().name(), is(request.getRole().name()));
        assertThat(updatedUser.getFirstName(), is(request.getFirstName()));
        assertThat(updatedUser.getLastName(), is(request.getLastName()));
    }

    @Test
    void deleteProduct_whenExistingRequest_thenProductDoesNotExistAnymore(){
        User user =  userTestSteps.createUser();

        userService.deleteUser(user.getId());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getUser(user.getId()));
    }



}
