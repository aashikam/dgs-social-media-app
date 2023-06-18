package com.example.socialmedia;

import com.example.socialmedia.datafetchers.UserDataFetcher;
import com.example.socialmedia.models.NewUser;
import com.example.socialmedia.models.User;
import com.example.socialmedia.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserDataFetcherTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserDataFetcher userDataFetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserQuery() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(userId);
        when(userService.getUserById(eq(userId))).thenReturn(user);

        // Act
        User result = userDataFetcher.user(userId);

        // Assert
        assertEquals(userId, result.getId());
    }

    @Test
    void testUsersQuery() {
        // Arrange
        List<User> users = Collections.singletonList(new User());
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        List<User> result = userDataFetcher.users();

        // Assert
        assertEquals(users.size(), result.size());
    }

    @Test
    void testCreateUserMutation() {
        // Arrange
        NewUser newUser = new NewUser();
        newUser.setName("John Doe");
        newUser.setAge(25);

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID().toString());
        savedUser.setName(newUser.getName());
        savedUser.setAge(newUser.getAge());

        when(userService.saveUser(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userDataFetcher.createUser(newUser);

        // Assert
        assertEquals(newUser.getName(), result.getName());
        assertEquals(newUser.getAge(), result.getAge());
    }

    @Test
    void testDeleteUserMutation() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        User deletedUser = new User();
        deletedUser.setId(userId);

        when(userService.deleteUser(eq(userId))).thenReturn(deletedUser);

        // Act
        User result = userDataFetcher.deleteUser(userId);

        // Assert
        assertEquals(userId, result.getId());
    }


}




