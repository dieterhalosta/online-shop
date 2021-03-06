package org.fastttrackit.onlineshop.steps;

import org.fastttrackit.onlineshop.domain.User;
import org.fastttrackit.onlineshop.domain.UserRole;
import org.fastttrackit.onlineshop.service.UserService;
import org.fastttrackit.onlineshop.transfer.user.CreateUserRequest;
import org.fastttrackit.onlineshop.transfer.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;


@Component
public class UserTestSteps {

    @Autowired
    private UserService userService;

    public UserResponse createUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setRole(UserRole.CUSTOMER);
        request.setFirstName("Test First Name");
        request.setLastName("Test Last Name");

        UserResponse user = userService.createUser(request);

        assertThat(user, notNullValue());
        assertThat(user.getId(), greaterThan(0L));
        assertThat(user.getRole(), is(request.getRole().name()));
        assertThat(user.getFirstName(), is(request.getFirstName()));
        assertThat(user.getLastName(), is(request.getLastName()));

        return user;
    }
}
