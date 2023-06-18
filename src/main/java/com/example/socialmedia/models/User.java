package com.example.socialmedia.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;


    @Column(name = "name", nullable = false)
    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(name = "age", nullable = false)
    private Integer age;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", posts=" + getAllPostsToString() +
                '}';
    }

    public String getAllPostsToString() {
        if (posts == null || posts.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Post post : posts) {
            sb.append(post.toString());
            sb.append(System.lineSeparator()); // Add line separator for readability
        }
        return sb.toString();
    }

    // getters and setters (omitted for brevity)
}


