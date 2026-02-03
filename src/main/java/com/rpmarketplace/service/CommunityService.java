package com.rpmarketplace.service;

import com.rpmarketplace.model.Comment;
import com.rpmarketplace.model.CommunityPost;
import com.rpmarketplace.model.User;
import com.rpmarketplace.repository.CommentRepository;
import com.rpmarketplace.repository.CommunityPostRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CommunityService {
    private final CommunityPostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommunityService(CommunityPostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public CommunityPost createPost(User author, String title, String content) {
        CommunityPost post = new CommunityPost();
        post.setAuthor(author);
        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post);
    }

    public List<CommunityPost> listPosts() {
        return postRepository.findAll();
    }

    public Comment addComment(User author, Long postId, String content) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public List<Comment> listComments(Long postId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));
        return commentRepository.findByPost(post);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
