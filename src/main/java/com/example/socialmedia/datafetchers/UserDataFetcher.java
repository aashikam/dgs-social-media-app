package com.example.socialmedia.datafetchers;

import com.example.socialmedia.models.User;
import com.example.socialmedia.models.NewUser;
import com.example.socialmedia.services.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.UUID;

/**
 * Handles graphql queries and mutations for users.
 */
@DgsComponent
public class UserDataFetcher {
    UserService userService;

    public UserDataFetcher(UserService userService) {
        this.userService = userService;
    }

    @DgsQuery
    public User user(@InputArgument String id) {
        return userService.getUserById(id);
    }

    @DgsQuery
    public List<User> users() {
        return userService.getAllUsers();
    }

    @DgsData(parentType = "Mutation", field = "createUser")
    public User createUser(@InputArgument("user") NewUser newUser) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(newUser.getName());
        user.setAge(newUser.getAge());
        return userService.saveUser(user);
    }

    @DgsData(parentType = "Mutation", field = "deleteUser")
    public User deleteUser(@InputArgument String id) {
        if (userService.getUserById(id) != null) {
            return userService.deleteUser(id);
        } else {
            throw new RuntimeException("User not found.");
        }
    }
}
