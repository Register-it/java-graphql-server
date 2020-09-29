package it.register.edu.graphql.resolver;


import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import it.register.edu.graphql.exception.ObjectNotFoundException;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.model.ReviewInput;
import it.register.edu.graphql.repository.RestaurantRepository;
import it.register.edu.graphql.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ReviewResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  @Autowired
  private ReviewRepository repository;
  @Autowired
  private RestaurantRepository restaurantRepository;

  public List<Review> getReviews(String restaurantId) {
    return repository.findByRestaurantId(Integer.parseInt(restaurantId));
  }

  @Transactional
  public Review createReview(ReviewInput input) {
    Restaurant restaurant = restaurantRepository
      .findById(input.getRestaurantId())
      .orElseThrow(() -> new ObjectNotFoundException("resturant non found", input.getRestaurantId()));

    Review review = Review.builder()
      .message(input.getMessage())
      .stars(input.getStars())
      .restaurant(restaurant)
      .build();
    return repository.save(review);
  }
  
}
