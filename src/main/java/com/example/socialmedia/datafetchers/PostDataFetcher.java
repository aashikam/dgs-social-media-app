package com.example.socialmedia.datafetchers;

import com.example.socialmedia.models.NewPost;
import com.example.socialmedia.models.Post;
import com.example.socialmedia.services.PostService;
import com.example.socialmedia.services.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsSubscription;
import com.netflix.graphql.dgs.InputArgument;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

/**
 * Handles graphql queries and mutations for posts.
 */
@DgsComponent
public class PostDataFetcher {
    PostService postService;
    UserService userService;

    public PostDataFetcher(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @DgsQuery
    public List<Post> posts() {
        return postService.getAllPosts();
    }

    @DgsData(parentType = "Mutation", field = "createPost")
    public Post createPost(@InputArgument("post") NewPost newPost, @RequestHeader("Authorization") String userId) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        post.setTitle(newPost.getTitle());
        post.setContent(newPost.getContent());
        if (userService.getUserById(userId) != null) {
            post.setUser(userService.getUserById(userId));
            return postService.savePost(post);
        } else {
            throw new RuntimeException("User not authorized.");
        }
    }

    @DgsData(parentType = "Mutation", field = "deletePost")
    public Post deletePost(@InputArgument String id, @RequestHeader("Authorization") String userId) {
        if (userService.getUserById(userId) != null) {
            return postService.deletePost(id);
        } else {
            throw new RuntimeException("User not authorized.");
        }
    }

    @DgsSubscription
    public Publisher<Post> newPosts() {
        return postService.getPostsPublisher();
    }
}
