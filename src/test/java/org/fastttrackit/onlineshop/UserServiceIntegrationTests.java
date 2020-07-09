package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.domain.Product;
import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.domain.UserRole;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.service.UserService;
import org.fastttrackit.onlineshop.transfer.GetProductsRequest;
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

    @Test
    public void createUser_whenValidRequest_thenReturnCreatedUser(){
        createUser();
    }

    @Test
    public void getUser_whenExistingUser_thenReturnUser(){
        User user = createUser();

        User userResponse = userService.getUser(user.getId());

        assertThat(userResponse, notNullValue());
        assertThat(userResponse.getId(), is(user.getId()));
        assertThat(userResponse.getRole(), is(user.getRole()));
        assertThat(userResponse.getFirstName(), is(user.getFirstName()));
        assertThat(userResponse.getLastName(), is(user.getLastName()));
    }

    @Test
    void getUsers_whenOneExistingUser_thenReturnPageOfOneUser(){
        User user = createUser();

        Page<User> usersPage = userService.getUsers(new GetUsersRequest(), PageRequest.of(0, 1000));

        assertThat(usersPage, CoreMatchers.notNullValue());
        assertThat(usersPage.getTotalElements(), greaterThanOrEqualTo(1L));
        assertThat(usersPage.getContent(), contains(user));

    }

    @Test
    public void updateUser_whenExistingUser_thenReturnUpdatedUser(){
        User user = createUser();
        CreateUserRequest request = new CreateUserRequest();
        request.setRole(UserRole.CUSTOMER);
        request.setFirstName("Test from Test");
        request.setLastName("LastNameTest");

        User updatedUser = userService.updateUser(user.getId(), request);

        assertThat(updatedUser, CoreMatchers.notNullValue());
        assertThat(updatedUser.getId(), is(user.getId()));
        assertThat(updatedUser.getRole(), is(request.getRole().name()));
        assertThat(updatedUser.getFirstName(), is(request.getFirstName()));
        assertThat(updatedUser.getLastName(), is(request.getLastName()));
    }

    @Test
    void deleteProduct_whenExistingRequest_thenProductDoesNotExistAnymore(){
        User user = createUser();

        userService.deleteUser(user.getId());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getUser(user.getId()));
    }

    private User createUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setRole(UserRole.CUSTOMER);
        request.setFirstName("Test First Name");
        request.setLastName("Test Last Name");

        User user = userService.createUser(request);

        assertThat(user, notNullValue());
        assertThat(user.getId(), greaterThan(0L));
        assertThat(user.getRole(), is(request.getRole().name()));
        assertThat(user.getFirstName(), is(request.getFirstName()));
        assertThat(user.getLastName(), is(request.getLastName()));

        return user;
    }

}
