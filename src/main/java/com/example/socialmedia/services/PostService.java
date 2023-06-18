package com.example.socialmedia.services;

import com.example.socialmedia.models.Post;
import com.example.socialmedia.repositories.PostRepository;
import jakarta.annotation.PostConstruct;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;

@Service
public class PostService {
    private FluxSink<Post> reviewsStream;
    private ConnectableFlux<Post> reviewsPublisher;
    private final PostRepository postRepository;

    @PostConstruct
    public void init() {
        Flux<Post> publisher = Flux.create(emitter -> {
            reviewsStream = emitter;
        });

        reviewsPublisher = publisher.publish();
        reviewsPublisher.connect();
    }

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post savePost(Post post) {
        Post savedPost = postRepository.save(post);
        reviewsStream.next(savedPost);
        return savedPost;
    }

    public Post getPostById(String id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post deletePost(String id) {
        if (postRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Post not found.");
        }
        Post post = postRepository.findById(id).orElse(null);
        postRepository.deleteById(id);
        return post;
    }

    public Publisher<Post> getPostsPublisher() {
        return reviewsPublisher;
    }
}
