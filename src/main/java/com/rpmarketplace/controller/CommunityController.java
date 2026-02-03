package com.rpmarketplace.controller;

import com.rpmarketplace.model.Comment;
import com.rpmarketplace.model.CommunityPost;
import com.rpmarketplace.model.User;
import com.rpmarketplace.service.CommunityService;
import com.rpmarketplace.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/community")
public class CommunityController {
    private final CommunityService communityService;
    private final UserService userService;

    public CommunityController(CommunityService communityService, UserService userService) {
        this.communityService = communityService;
        this.userService = userService;
    }

    @GetMapping("/public/posts")
    public List<CommunityPost> listPosts() {
        return communityService.listPosts();
    }

    @PostMapping("/posts")
    public CommunityPost createPost(@Valid @RequestBody PostRequest request, Principal principal) {
        User author = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return communityService.createPost(author, request.title(), request.content());
    }

    @GetMapping("/public/posts/{postId}/comments")
    public List<Comment> listComments(@PathVariable Long postId) {
        return communityService.listComments(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    public Comment addComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest request, Principal principal) {
        User author = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return communityService.addComment(author, postId, request.content());
    }

    public record PostRequest(@NotBlank String title, @NotBlank String content) {
    }

    public record CommentRequest(@NotBlank String content) {
    }
}
