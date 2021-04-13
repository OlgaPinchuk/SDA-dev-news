package se.sdaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class TopicController {
    ArticleRepository articleRepository;
    TopicRepository topicRepository;

    @Autowired
    public TopicController(TopicRepository topicRepository, ArticleRepository articleRepository) {
        this.topicRepository = topicRepository;
        this.articleRepository = articleRepository;
    }

    //create a new topic.
    @PostMapping("/topics")
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(topic);
    }

    //return all topics.
    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> listAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        return ResponseEntity.ok(topics);
    }

    //return all topics associated with article given by articleId.
    @GetMapping("/articles/{articleId}/topics")
    public ResponseEntity<List<Topic>> listTopicsByArticle(@PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        List<Topic> topics = article.getTopics();
        return ResponseEntity.ok(topics);
    }

    //return all articles associated with the topic given by topicId.
    @GetMapping("/topics/{topicId}/articles")
    public ResponseEntity<List<Article>> listArticlesByTopic(@PathVariable Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        List<Article> articles = topic.getArticles();
        return ResponseEntity.ok(articles);
    }

    //associate the topic with the article given by articleId. If topic does not already exist, it is created.
    @PostMapping("/articles/{articleId}/topics")
    public ResponseEntity<Article> addTopicToArticle(@RequestBody Topic topic, @PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        List<Topic> articleTopics = article.getTopics();
        List<Topic> allTopics = topicRepository.findAll();

        boolean isTopicExist = topicRepository.existsByName(topic.getName());

        if (!isTopicExist) {
            topicRepository.save(topic);
        } else {
            for (Topic item : allTopics) {
                if (item.getName().equals(topic.getName())) {
                    topic = item;
                }
            }
        }

        articleTopics.add(topic);
        articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    //update the given topic.
    @PutMapping("/topics/{id}")
    public ResponseEntity<Topic> updateTopicById(@PathVariable Long id, @Valid @RequestBody Topic updatedTopic) {
        topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedTopic.setId(id);
        topicRepository.save(updatedTopic);
        return ResponseEntity.ok(updatedTopic);
    }

    //delete the given topic.
    @DeleteMapping("/topics/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long id) {
        Topic topic = topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        topicRepository.delete(topic);
    }

    //delete the association of a topic for the given article. The topic & article themselves remain.
    @DeleteMapping("/articles/{articleId}/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssociation(@PathVariable Long topicId, @PathVariable Long articleId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        if (article.getTopics().contains(topic)) {
            article.getTopics().remove(topic);
            articleRepository.save(article);
        } else{
            throw new ResourceNotFoundException();
        }
    }


}
