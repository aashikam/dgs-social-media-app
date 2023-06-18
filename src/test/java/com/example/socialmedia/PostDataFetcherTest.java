package com.example.socialmedia;

import com.example.socialmedia.datafetchers.PostDataFetcher;
import com.example.socialmedia.models.NewPost;
import com.example.socialmedia.models.Post;
import com.example.socialmedia.models.User;
import com.example.socialmedia.services.PostService;
import com.example.socialmedia.services.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


class PostDataFetcherTest {
    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostDataFetcher postDataFetcher;

    private User authorizedUser;
    private Post authorizedUserPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creating the authorized user
        authorizedUser = new User();
        authorizedUser.setId("12345");
        userService.saveUser(authorizedUser);

        // Creating a post associated with the authorized user
        authorizedUserPost = new Post();
        authorizedUserPost.setId(UUID.randomUUID().toString());
        authorizedUserPost.setTitle("Sample Title");
        authorizedUserPost.setContent("Sample Content");
        authorizedUserPost.setUser(authorizedUser);
        when(postService.getPostById(anyString())).thenReturn(authorizedUserPost);
    }

    @Test
    void testPostsQuery() {
        List<Post> posts = Collections.singletonList(new Post());
        when(postService.getAllPosts()).thenReturn(posts);

        List<Post> result = postDataFetcher.posts();
        assertEquals(posts.size(), result.size());
    }

    @Test
    void testCreatePostMutation() {
        NewPost newPost = new NewPost();
        newPost.setTitle("Sample Title");
        newPost.setContent("Sample Content");

        String userId = "12345";

        Post savedPost = new Post();
        savedPost.setId(UUID.randomUUID().toString());
        savedPost.setTitle(newPost.getTitle());
        savedPost.setContent(newPost.getContent());

        when(userService.getUserById(eq(userId))).thenReturn(new User());
        when(postService.savePost(any(Post.class))).thenReturn(savedPost);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("userId", userId);

        Post result = postDataFetcher.createPost(newPost, request.getHeader("userId"));

        assertEquals(newPost.getTitle(), result.getTitle());
        assertEquals(newPost.getContent(), result.getContent());
    }

    @Test
    void testDeletePostMutation_WhenUserAuthorized() {
        String postId = authorizedUserPost.getId();
        String userId = authorizedUser.getId();

        when(userService.getUserById(eq(userId))).thenReturn(new User());
        when(postService.deletePost(eq(postId))).thenReturn(authorizedUserPost);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("userId", userId);

        Post result = postDataFetcher.deletePost(postId, request.getHeader("userId"));
        assertEquals(postId, result.getId());
    }

    @Test
    void testDeletePostMutation_WhenUserNotAuthorized() {
        String postId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        when(userService.getUserById(eq(userId))).thenReturn(null);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("userId", userId);
        assertThrows(RuntimeException.class, () -> postDataFetcher.deletePost(postId, request.getHeader("userId")));
    }
}
