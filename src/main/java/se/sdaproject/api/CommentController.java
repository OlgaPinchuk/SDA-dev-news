package se.sdaproject.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sdaproject.api.exception.ResourceNotFoundException;
import se.sdaproject.model.Article;
import se.sdaproject.model.Comment;
import se.sdaproject.repository.ArticleRepository;
import se.sdaproject.repository.CommentRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {

    CommentRepository commentRepository;
    ArticleRepository articleRepository;
    @Autowired
    public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    //create a new comment on article given by articleId.
    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable Long articleId, @RequestBody Comment comment) {
        Article owner = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        comment.setOwner(owner);
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    //return all comments on article given by articleId.
    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<Comment>> listCommentsByArticleId(@PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        List<Comment> comments = article.getComments();
        return ResponseEntity.ok(comments);
    }

    //return all comments made by author given by authorName.
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> listCommentsByAuthorName(@RequestParam(value ="authorName", required = true) String authorName) {
        List<Comment> authorComments = commentRepository.findByAuthorName(authorName);
        return ResponseEntity.ok(authorComments);
    }


    //update the given comment.
    @PutMapping ("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment updatedComment) {
        commentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedComment.setId(id);
        commentRepository.save(updatedComment);
        return ResponseEntity.ok(updatedComment);
    }

    //delete the given comment
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        commentRepository.delete(comment);
        return ResponseEntity.ok(comment);
    }
}
