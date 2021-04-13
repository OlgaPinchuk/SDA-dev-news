# Dev News

## Introduction
This assignment is all about building the backend API for a developer news site where users can create articles, comment them and post their reactions (likes, dislikes). It doesn't require a graphical user interface so it is enough to be able to make requests and get plain json text responses via curl/Postman. 

## Learning Objectives
* Understand the basic structure of a Spring application.
* Practice building, testing and consuming rest APIs.
* Learn about data modelling for real world applications.
* Learn how to interact with a relational database using an ORM tool implementing Spring JPA (Hibernate).

## Setup
Remember that you will need to configure the following dependencies in your `build.gradle`:
* Spring Web
* Spring JPA
* PostgreSQL Driver

`src/main/resources/application.properties` should also be properly configured:
```properties
spring.jpa.database=POSTGRESQL
spring.jpa.show-sql=true

spring.datasource.url=jdbc:postgresql://localhost:5431/demo
spring.datasource.username=demo_user
spring.datasource.password=demo_pass

spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create
```

## Assignment

### Articles
Article is the core entity in our project. It represents a news article with a unique **id**, **title**, **body** (article text content) and the 
**authorName**.

Example JSON response when requesting an article:

```json
{
    "id": 1,
    "title": "10 reasons to learn Spring",
    "body": "In this article I'll be listing 10 reasons why you should learn spring and use it in your next project...",
    "authorName": "John Smith"
}
```

These are the endpoints for the article API that should exist:
| HTTP Method | HTTP Path | Action |
| ------------|-----------|--------|
| `GET`    | `/articles`      | return all articles. |
| `GET`    | `/articles/{id}` | return a specific article based on the provided id.|
| `POST`   | `/articles`      | create a new article.|
| `PUT`    | `/articles/{id}` | update the given article.|
| `DELETE` | `/articles/{id}` | delete the given article.|

#### Exercise 1
Create an Article model and implement the above API.

### Comments
We want our visitors to be able to comment the different articles with a unique **id**, **body**, **authorName** (for the comment), and **article**
on which the comment was posted. Each article can have zero or more comments. 

Example JSON response when requesting a comment:

```json
{
    "id": 1,
    "body": "This article is very well written",
    "authorName": "John Smith",
    "article": {
        "id": 1,
        "title": "10 reasons to learn Spring",
        "body": "In this article I'll be listing 10 reasons why you should learn spring and use it in your next project...",
        "authorName": "John Smith"
    }
}

```
With the following endpoints:

| HTTP Method | HTTP Path | Action |
| ------------|-----------|--------|
| `GET`    | `/articles/{articleId}/comments`    | return all comments on article given by `articleId`. |
| `GET`    | `/comments?authorName={authorName}` | return all comments made by author given by `authorName`. |
| `POST`   | `/articles/{articleId}/comments`    | create a new comment on article given by `articleId`. |
| `PUT`    | `/comments/{id}`                    | update the given comment. |
| `DELETE` | `/comments/{id}`                    | delete the given comment. |

#### Exercise 2
Create a Comment model and implement the above API.

### Topics
We want to categorize our articles by topics. Each topic can be applied to zero or many articles and each article can have zero or many topics.

Example JSON response when requesting an article should now be:

```json
{
    "id": 1,
    "title": "10 reasons to learn Spring",
    "body": "In this article I'll be listing 10 reasons why you should learn spring and use it in your next project...",
    "authorName": "John Smith",
    "topics": [
        {
            "id": 1,
            "name": "backend"
        },
        {
            "id": 2,
            "name": "java"
        },
        {
            "id": 3,
            "name": "spring"
        }
    ]
}
```
Endpoints:

| HTTP Method | HTTP Path | Action |
| ------------|-----------|--------|
| `GET`    | `/topics` | return all topics. |
| `GET`    | `/articles/{articleId}/topics` | return all topics associated with article given by `articleId`. |
| `POST`   | `/articles/{articleId}/topics` | associate the topic with the article given by `articleId`. If topic does not already exist, it is created. |
| `POST`   | `/topics` | create a new topic. |
| `PUT`    | `/topics/{id}` | update the given topic. |
| `DELETE` | `/topics/{id}` | delete the given topic. |
| `DELETE` | `/articles/{articleId}/topics/{topicId}` | delete the association of a topic for the given article. The topic & article themselves remain. |
| `GET`    | `/topics/{topicId}/articles` | return all articles associated with the topic given by `topicId`. |

#### Exercise 3
Create a Topic model and implement the above API.

### Reactions

#### Exercise 4 (Bonus)
To make our application more interactive we might want to add the ability to add article and comment reactions (likes, dislikes, ...).
Go ahead and implement reactions in your application. You're free to choose how the model should look like so try to draw it out beforehand and think of what kind of relationship will the reactions have to the articles and comments respectively.
