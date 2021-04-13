package se.sdaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sdaproject.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorName(String authorName);
}
