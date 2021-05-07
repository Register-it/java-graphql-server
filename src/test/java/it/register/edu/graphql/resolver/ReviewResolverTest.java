package it.register.edu.graphql.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.register.edu.graphql.exception.ObjectNotFoundException;
import it.register.edu.graphql.model.Reply;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.model.ReviewInput;
import it.register.edu.graphql.repository.ReplyRepository;
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
  private ReplyRepository mockReplyRepository;

  private UnicastProcessor<Reply> processor = UnicastProcessor.create();

  @Captor
  private ArgumentCaptor<Review> reviewCaptor;

  @Captor
  private ArgumentCaptor<Reply> replyCaptor;

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
  public void addReply() {
    int reviewId = 123;
    Review review = new Review();
    String message = "This is a reply";

    when(mockReviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(mockReplyRepository.save(any())).then(args -> args.getArgument(0));

    List<Reply> replies = new ArrayList<>();
    processor.subscribe(replies::add);

    Reply result = resolver.addReply(String.valueOf(reviewId), message);

    assertNotNull(replies);
    assertEquals(1, replies.size());
    assertEquals(result, replies.get(0));

    verify(mockReplyRepository).save(replyCaptor.capture());
    assertEquals(message, replyCaptor.getValue().getMessage());
    assertEquals(review, replyCaptor.getValue().getReview());
    assertEquals(result, replyCaptor.getValue());
  }

  @Test(expected = ObjectNotFoundException.class)
  public void addReply_reviewNotFound() {
    int reviewId = 123;
    String message = "This is a reply";

    when(mockReviewRepository.findById(reviewId)).thenReturn(Optional.empty());

    resolver.addReply(String.valueOf(reviewId), message);
  }
}
