package it.register.edu.graphql.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.register.edu.graphql.exception.ObjectNotFoundException;
import it.register.edu.graphql.model.Comment;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.model.ReviewInput;
import it.register.edu.graphql.repository.CommentRepository;
import it.register.edu.graphql.repository.RestaurantRepository;
import it.register.edu.graphql.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.UnicastProcessor;

@RunWith(MockitoJUnitRunner.class)
public class ReviewResolverTest {

  @Mock
  private RestaurantRepository mockRepository;

  @Mock
  private ReviewRepository mockReviewRepository;

  @Mock
  private CommentRepository mockCommentRepository;

  private UnicastProcessor<Comment> processor = UnicastProcessor.create();

  @Captor
  private ArgumentCaptor<Review> reviewCaptor;

  @Captor
  private ArgumentCaptor<Comment> commentCaptor;

  @InjectMocks
  private ReviewResolver resolver;

  @Before
  public void setUp() {
    ReflectionTestUtils.setField(resolver, "processor", processor);
  }

  @Test
  public void getReviews() {
    List<Review> expected = List.of(new Review(), new Review());
    int restaurantId = 1;
    when(mockReviewRepository.findByRestaurantId(restaurantId)).thenReturn(expected);

    List<Review> result = resolver.getReviews(String.valueOf(restaurantId));

    assertEquals(expected, result);
  }

  @Test
  public void createReview() {
    int restaurantId = 1;
    when(mockRepository.findById(restaurantId)).thenReturn(Optional.of(new Restaurant()));

    Integer rating = 4;

    ReviewInput input = ReviewInput.builder()
      .restaurantId(restaurantId)
      .message("wow")
      .rating(4)
      .build();
    final Review review = resolver.createReview(input);

    verify(mockReviewRepository).save(reviewCaptor.capture());

    assertEquals(rating, reviewCaptor.getValue().getRating());
    assertEquals("wow", reviewCaptor.getValue().getMessage());
  }

  @Test(expected = ObjectNotFoundException.class)
  public void createReview_WhenARestaurantIsNotFound() {
    int restaurantId = 1;
    when(mockRepository.findById(restaurantId)).thenReturn(Optional.empty());

    ReviewInput input = ReviewInput.builder()
        .restaurantId(restaurantId)
        .message("wow")
        .rating(4)
        .build();
    final Review review = resolver.createReview(input);

    verify(mockReviewRepository, times(0)).save(any(Review.class));
  }

  @Test
  public void addComment() {
    int reviewId = 123;
    Review review = new Review();
    String message = "This is a comment";

    when(mockReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(mockCommentRepository.save(any())).then(args -> args.getArgument(0));

    List<Comment> comments = new ArrayList<>();
    processor.subscribe(comments::add);

    Comment result = resolver.addComment(String.valueOf(reviewId), message);

    assertNotNull(comments);
    assertEquals(1, comments.size());
    assertEquals(result, comments.get(0));

    verify(mockCommentRepository).save(commentCaptor.capture());
    assertEquals(message, commentCaptor.getValue().getMessage());
    assertEquals(review, commentCaptor.getValue().getReview());
    assertEquals(result, commentCaptor.getValue());
  }

  @Test(expected = ObjectNotFoundException.class)
  public void addComment_reviewNotFound() {
    int reviewId = 123;
    String message = "This is a comment";

    when(mockReviewRepository.findById(reviewId)).thenReturn(Optional.empty());

    resolver.addComment(String.valueOf(reviewId), message);
  }
}
