package com.rpmarketplace.repository;

import com.rpmarketplace.model.Comment;
import com.rpmarketplace.model.CommunityPost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(CommunityPost post);
}
