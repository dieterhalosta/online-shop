package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.domain.UserRole;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.service.UserService;
import org.fastttrackit.onlineshop.steps.UserTestSteps;
import org.fastttrackit.onlineshop.transfer.user.CreateUserRequest;
import org.fastttrackit.onlineshop.transfer.user.GetUsersRequest;
import org.fastttrackit.onlineshop.transfer.user.UserResponse;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;



@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
    void createUser_whenMissingMandatoryProperties_thenThrowException(){
        CreateUserRequest request = new CreateUserRequest();

        try {
            userService.createUser(request);
        } catch (Exception e) {
            assertThat("Unexpected exception thrown.", e instanceof ConstraintViolationException);
        }
    }

    @Test
    public void getUser_whenExistingUser_thenReturnUser(){
        UserResponse user =  userTestSteps.createUser();

        UserResponse response = userService.getUserResponse(user.getId());

        assertThat(response, notNullValue());
        assertThat(response.getId(), is(user.getId()));
        assertThat(response.getRole(), is(user.getRole()));
        assertThat(response.getFirstName(), is(user.getFirstName()));
        assertThat(response.getLastName(), is(user.getLastName()));
    }

    @Test
    void getUser_whenNonExistingUser_thenThrowResourcesNotFoundException(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getUserResponse(0));
    }

    @Test
    void getUsers_whenOneExistingUser_thenReturnPageOfOneUser(){
        UserResponse user =  userTestSteps.createUser();

        Page<UserResponse> usersPage = userService.getUsers(new GetUsersRequest(), PageRequest.of(0, 1000));

        assertThat(usersPage, CoreMatchers.notNullValue());
        assertThat(usersPage.getTotalElements(), greaterThanOrEqualTo(1L));
        assertThat(usersPage.getContent().get(0).getId(), is(user.getId()));
        assertThat(usersPage.getContent().get(0).getRole(), is(user.getRole()));
        assertThat(usersPage.getContent().get(0).getFirstName(), is(user.getFirstName()));
        assertThat(usersPage.getContent().get(0).getLastName(), is(user.getLastName()));

    }

    @Test
    public void updateUser_whenExistingUser_thenReturnUpdatedUser(){
        UserResponse user =  userTestSteps.createUser();
        CreateUserRequest request = new CreateUserRequest();
        request.setRole(UserRole.ADMIN);
        request.setFirstName("Test from Test");
        request.setLastName("LastNameTest");

        UserResponse updatedUser = userService.updateUser(user.getId(), request);

        assertThat(updatedUser, CoreMatchers.notNullValue());
        assertThat(updatedUser.getId(), is(user.getId()));
        assertThat(updatedUser.getRole(), is(request.getRole().name()));
        assertThat(updatedUser.getFirstName(), is(request.getFirstName()));
        assertThat(updatedUser.getLastName(), is(request.getLastName()));
    }

    @Test
    void deleteProduct_whenExistingRequest_thenProductDoesNotExistAnymore(){
        UserResponse user =  userTestSteps.createUser();

        userService.deleteUser(user.getId());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getUser(user.getId()));
    }



}
