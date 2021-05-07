package it.register.edu.graphql.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestSubscription;
import it.register.edu.graphql.model.Reply;
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
  private UnicastProcessor<Reply> processor;

  @Test
  public void replyAddedNoItems() {
    List<GraphQLResponse> responses = graphQLTestSubscription
        .start("graphql/subscription/replyAdded.graphql")
        .awaitAndGetNextResponses(1000, 0);

    assertEquals(0, responses.size());
  }

  @Test
  public void replyAddedOneItem() {
    Integer replyId = 111;
    String replyMessage = "OK";
    Reply reply = Reply.builder().id(replyId).message(replyMessage).review(Review.builder().id(123).build()).build();
    addReplyAfterSubscription(reply);

    List<GraphQLResponse> responses = graphQLTestSubscription
        .start("graphql/subscription/replyAdded.graphql")
        .awaitAndGetNextResponses(10000, 1);

    assertEquals(1, responses.size());
    assertTrue(responses.get(0).isOk());
    assertEquals(replyId, responses.get(0).get("$.data.replyAdded.id", Integer.class));
    assertEquals(replyMessage, responses.get(0).get("$.data.replyAdded.message"));
  }

  @Test
  public void replyAddedMultipleItems() {
    Integer replyId1 = 111;
    Integer replyId2 = 222;
    Integer replyId3 = 333;
    String replyMessage1 = "OK1";
    String replyMessage2 = "OK2";
    String replyMessage3 = "OK3";
    Reply reply1 = Reply.builder().id(replyId1).message(replyMessage1).review(Review.builder().id(123).build()).build();
    Reply reply2 = Reply.builder().id(replyId2).message(replyMessage2).review(Review.builder().id(234).build()).build();
    Reply reply3 = Reply.builder().id(replyId3).message(replyMessage3).review(Review.builder().id(123).build()).build();
    addReplyAfterSubscription(reply1);
    addReplyAfterSubscription(reply2);
    addReplyAfterSubscription(reply3);

    List<GraphQLResponse> responses = graphQLTestSubscription
        .start("graphql/subscription/replyAdded.graphql")
        .awaitAndGetNextResponses(10000, 2);

    assertEquals(2, responses.size());
    assertTrue(responses.stream().allMatch(GraphQLResponse::isOk));
    assertTrue(responses.stream()
        .map(response -> response.get("$.data.replyAdded.id", Integer.class))
        .collect(Collectors.toList())
        .containsAll(List.of(replyId1, replyId3)));
    assertTrue(responses.stream()
        .map(response -> response.get("$.data.replyAdded.message"))
        .collect(Collectors.toList())
        .containsAll(List.of(replyMessage1, replyMessage3)));
  }

  @After
  public void tearDown() {
    graphQLTestSubscription.reset();
  }

  private void addReplyAfterSubscription(Reply reply) {
    new Thread(() -> {
      while (!processor.hasDownstreams()) {
        pause(100L);
      }
      pause(3000L + reply.getId());
      processor.onNext(reply);
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
