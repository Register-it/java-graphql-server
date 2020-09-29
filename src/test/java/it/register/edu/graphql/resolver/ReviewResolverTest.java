package it.register.edu.graphql.resolver;

import it.register.edu.graphql.exception.ObjectNotFoundException;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.model.ReviewInput;
import it.register.edu.graphql.repository.RestaurantRepository;
import it.register.edu.graphql.repository.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReviewResolverTest {

  @Mock
  private RestaurantRepository mockRepository;
  @Mock
  private ReviewRepository mockReviewRepository;

  @InjectMocks
  private ReviewResolver resolver;

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

    Integer stars = 4;

    ReviewInput input = ReviewInput.builder()
      .restaurantId(restaurantId)
      .message("wow")
      .stars(4)
      .build();
    final Review review = resolver.createReview(input);

    ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
    verify(mockReviewRepository).save(captor.capture());

    assertEquals(stars, captor.getValue().getStars());
    assertEquals("wow", captor.getValue().getMessage());

  }
  @Test(expected = ObjectNotFoundException.class)
  public void createReview_WhenARestaurantIsNotFound() {
    int restaurantId = 1;
    when(mockRepository.findById(restaurantId)).thenReturn(Optional.empty());

    ReviewInput input = ReviewInput.builder()
      .restaurantId(restaurantId)
      .message("wow")
      .stars(4)
      .build();
    final Review review = resolver.createReview(input);

    verify(mockReviewRepository, times(0)).save(any(Review.class));

  }


}
