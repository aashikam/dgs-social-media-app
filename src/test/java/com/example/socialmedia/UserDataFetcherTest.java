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
        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(userId);
        when(userService.getUserById(eq(userId))).thenReturn(user);

        User result = userDataFetcher.user(userId);
        assertEquals(userId, result.getId());
    }

    @Test
    void testUsersQuery() {
        List<User> users = Collections.singletonList(new User());
        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = userDataFetcher.users();
        assertEquals(users.size(), result.size());
    }

    @Test
    void testCreateUserMutation() {
        NewUser newUser = new NewUser();
        newUser.setName("John Doe");
        newUser.setAge(25);

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID().toString());
        savedUser.setName(newUser.getName());
        savedUser.setAge(newUser.getAge());

        when(userService.saveUser(any(User.class))).thenReturn(savedUser);
        User result = userDataFetcher.createUser(newUser);

        assertEquals(newUser.getName(), result.getName());
        assertEquals(newUser.getAge(), result.getAge());
    }
}
