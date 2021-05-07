package it.register.edu.graphql.resolver;


import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import it.register.edu.graphql.exception.ObjectNotFoundException;
import it.register.edu.graphql.model.Reply;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.model.ReviewInput;
import it.register.edu.graphql.repository.ReplyRepository;
import it.register.edu.graphql.repository.RestaurantRepository;
import it.register.edu.graphql.repository.ReviewRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.UnicastProcessor;

@Component
public class ReviewResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Autowired
  private ReplyRepository replyRepository;

  @Autowired
  private UnicastProcessor<Reply> processor;

  public List<Review> getReviews(String restaurantId) {
    return reviewRepository.findByRestaurantId(Integer.parseInt(restaurantId));
  }

  @Transactional
  public Review createReview(ReviewInput input) {
    Restaurant restaurant = restaurantRepository
      .findById(input.getRestaurantId())
      .orElseThrow(() -> new ObjectNotFoundException("resturant non found", input.getRestaurantId()));

    Review review = Review.builder()
      .message(input.getMessage())
      .rating(input.getRating())
      .restaurant(restaurant)
      .build();
    return reviewRepository.save(review);
  }

  @Transactional
  public Reply addReply(String reviewId, String message) {

    Review review = reviewRepository
        .findById(Integer.parseInt(reviewId))
        .orElseThrow(() -> new ObjectNotFoundException("review non found", reviewId));

    Reply reply = Reply.builder()
        .message(message)
        .review(review)
        .build();

    Reply saved = replyRepository.save(reply);

    processor.onNext(saved);

    return saved;
  }

}
