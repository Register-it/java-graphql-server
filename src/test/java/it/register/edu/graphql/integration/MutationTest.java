package it.register.edu.graphql.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import it.register.edu.graphql.model.Comment;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.repository.CommentRepository;
import it.register.edu.graphql.repository.RestaurantRepository;
import it.register.edu.graphql.repository.ReviewRepository;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MutationTest {

  @Autowired
  private GraphQLTestTemplate graphQLTestTemplate;

  @MockBean
  private RestaurantRepository mockRestaurantRepository;

  @MockBean
  private ReviewRepository mockReviewRepository;

  @MockBean
  private CommentRepository mockCommentRepository;

  @Test
  public void createReviewSuccess() throws IOException {
    Integer restaurantId = 123;
    Integer reviewId = 111;
    Restaurant restaurant = Restaurant.builder().id(123).build();
    when(mockRestaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
    when(mockReviewRepository.save(any())).then(args -> {
      Review review = args.getArgument(0);
      review.setId(reviewId);
      return review;
    });

    GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/mutation/createReview.graphql");

    assertTrue(response.isOk());
    assertEquals(reviewId, response.get("$.data.createReview.id", Integer.class));
    assertEquals("Content", response.get("$.data.createReview.message"));
    assertEquals("3", response.get("$.data.createReview.rating"));
    assertEquals(restaurantId, response.get("$.data.createReview.restaurant.id", Integer.class));
  }

  @Test
  public void createReviewRestaurantNotFound() throws IOException {
    Integer restaurantId = 123;
    when(mockRestaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

    GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/mutation/createReview.graphql");

    assertTrue(response.isOk());
    Object[] errors = response.get("$.errors", Object[].class);
    assertEquals(1, errors.length);
  }

  @Test
  public void addCommentSuccess() throws IOException {
    Integer reviewId = 123;
    Integer commentId = 111;
    Review review = Review.builder().id(reviewId).build();
    when(mockReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(mockCommentRepository.save(any())).then(args -> {
      Comment comment = args.getArgument(0);
      comment.setId(commentId);
      return comment;
    });

    GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/mutation/addComment.graphql");

    assertTrue(response.isOk());
    assertEquals(commentId, response.get("$.data.addComment.id", Integer.class));
    assertEquals("Content", response.get("$.data.addComment.message"));
    assertEquals(reviewId, response.get("$.data.addComment.review.id", Integer.class));
  }

  @Test
  public void addCommentReviewNotFound() throws IOException {
    Integer reviewId = 123;
    when(mockReviewRepository.findById(reviewId)).thenReturn(Optional.empty());

    GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/mutation/addComment.graphql");

    assertTrue(response.isOk());
    Object[] errors = response.get("$.errors", Object[].class);
    assertEquals(1, errors.length);
  }

}
