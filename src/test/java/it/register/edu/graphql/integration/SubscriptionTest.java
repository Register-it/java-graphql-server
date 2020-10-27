package it.register.edu.graphql.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestSubscription;
import it.register.edu.graphql.model.Comment;
import it.register.edu.graphql.model.Review;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.UnicastProcessor;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SubscriptionTest {

  @Autowired
  private GraphQLTestSubscription graphQLTestSubscription;

  @Autowired
  private UnicastProcessor<Comment> processor;

  @Test
  public void commentAddedNoItems() {
    List<GraphQLResponse> responses = graphQLTestSubscription
        .start("graphql/subscription/commentAdded.graphql")
        .awaitAndGetNextResponses(1000, 0);

    assertEquals(0, responses.size());
  }

  @Test
  public void commentAddedOneItem() {
    Integer commentId = 111;
    String commentMessage = "OK";
    Comment comment = Comment.builder().id(commentId).message(commentMessage).review(Review.builder().id(123).build()).build();
    addCommentAfterSubscription(comment);

    List<GraphQLResponse> responses = graphQLTestSubscription
        .start("graphql/subscription/commentAdded.graphql")
        .awaitAndGetNextResponses(10000, 1);

    assertEquals(1, responses.size());
    assertTrue(responses.get(0).isOk());
    assertEquals(commentId, responses.get(0).get("$.data.commentAdded.id", Integer.class));
    assertEquals(commentMessage, responses.get(0).get("$.data.commentAdded.message"));
  }

  @Test
  public void commentAddedMultipleItems() {
    Integer commentId1 = 111;
    Integer commentId2 = 222;
    Integer commentId3 = 333;
    String commentMessage1 = "OK1";
    String commentMessage2 = "OK2";
    String commentMessage3 = "OK3";
    Comment comment1 = Comment.builder().id(commentId1).message(commentMessage1).review(Review.builder().id(123).build()).build();
    Comment comment2 = Comment.builder().id(commentId2).message(commentMessage2).review(Review.builder().id(234).build()).build();
    Comment comment3 = Comment.builder().id(commentId3).message(commentMessage3).review(Review.builder().id(123).build()).build();
    addCommentAfterSubscription(comment1);
    addCommentAfterSubscription(comment2);
    addCommentAfterSubscription(comment3);

    List<GraphQLResponse> responses = graphQLTestSubscription
        .start("graphql/subscription/commentAdded.graphql")
        .awaitAndGetNextResponses(10000, 2);

    assertEquals(2, responses.size());
    assertTrue(responses.stream().allMatch(GraphQLResponse::isOk));
    assertTrue(responses.stream()
        .map(response -> response.get("$.data.commentAdded.id", Integer.class))
        .collect(Collectors.toList())
        .containsAll(List.of(commentId1, commentId3)));
    assertTrue(responses.stream()
        .map(response -> response.get("$.data.commentAdded.message"))
        .collect(Collectors.toList())
        .containsAll(List.of(commentMessage1, commentMessage3)));
  }

  @After
  public void tearDown() {
    graphQLTestSubscription.reset();
  }

  private void addCommentAfterSubscription(Comment comment) {
    new Thread(() -> {
      while (!processor.hasDownstreams()) {
        pause(100L);
      }
      pause(3000L + comment.getId());
      processor.onNext(comment);
    }).start();
  }

  private void pause(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
