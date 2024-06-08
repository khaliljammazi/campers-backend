package com.campers.now.services.Impl;

import com.campers.now.models.Comment;
import com.campers.now.models.Post;
import com.campers.now.models.User;
import com.campers.now.repositories.CommentRepository;
import com.campers.now.repositories.PostRepository;
import com.campers.now.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAll_ShouldReturnAllPosts() {
        // Given
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());
        when(postRepository.findAll()).thenReturn(posts);

        // When
        List<Post> result = postService.getAll(1,"title",ASC);

        // Then
        assertEquals(posts.size(), result.size());
        assertTrue(result.containsAll(posts));
    }

    @Test
    void getAll_WithPagination_ShouldReturnCorrectPage() {
        // Given
        List<Post> allPosts = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            allPosts.add(new Post());
        }
        when(postRepository.findAll()).thenReturn(allPosts);

        // When
        List<Post> result = postService.getAll(2, "title", ASC);

        // Then
        assertEquals(10, result.size());
        assertTrue(allPosts.subList(10, 20).containsAll(result));
    }

    @Test
    void getById_ExistingId_ShouldReturnPost() {
        // Given
        Integer postId = 1;
        Post post = new Post();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        Post result = postService.getById(postId);

        // Then
        assertEquals(post, result);
    }

    @Test
    void getById_NonExistingId_ShouldThrowNotFoundException() {
        // Given
        Integer postId = 1;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(HttpClientErrorException.class, () -> postService.getById(postId));
    }

    @Test
    void add_ShouldSavePostAndReturnSavedPost() {
        // Given
        Post post = new Post();
        when(postRepository.save(post)).thenReturn(post);

        // When
        Post result = postService.add(post);

        // Then
        assertNotNull(result);
        assertEquals(post, result);
    }

    @Test
    void addPost_WithValidUser_ShouldSavePostWithUser() {
        // Given
        Integer userId = 1;
        Post post = new Post();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.save(post)).thenReturn(post);

        // When
        Post result = postService.addPost(post, userId);

        // Then
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    // getPostsByUserMostComments
    @Test
    void getPostsByUserMostComments_ShouldReturnPostsSortedByCommentCount() {
        // Given
        int userId = 1;
        int post1Id = 1;
        int post2Id = 2;
        int post3Id = 3;

        // Create a user
        User user = new User();
        user.setId(userId);

        // Create posts
        Post post1 = new Post();
        post1.setId(post1Id);

        Post post2 = new Post();
        post2.setId(post2Id);

        Post post3 = new Post();
        post3.setId(post3Id);

        // Create comments
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(post1Id, user)); // User comments on post1
        comments.add(new Comment(post2Id, user)); // User comments again on post1
        comments.add(new Comment(post3Id, user)); // User comments on post2

        // Mock commentRepository and postRepository
        when(commentRepository.findByUserId(userId)).thenReturn(comments);
        when(postRepository.findById(post1Id)).thenReturn(Optional.of(post1));
        when(postRepository.findById(post2Id)).thenReturn(Optional.of(post2));
        when(postRepository.findById(post3Id)).thenReturn(Optional.of(post3));

        // When
        List<Post> result = postService.getPostsByUserMostComments(userId, 2);

        // Then
        // Assert the size of the result list
        assertEquals(2, result.size());
        // Assert the order of posts based on comment count
        assertEquals(post1, result.get(0)); // post1 should come first as it has more comments
        assertEquals(post2, result.get(1)); // post2 should come second
    }
}