package se.sdaproject;


import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long>{
    boolean existsByName(String name);
}