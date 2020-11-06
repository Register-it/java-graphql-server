package it.register.edu.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantFieldsResolver implements GraphQLResolver<Restaurant> {

  @Autowired
  private ReviewRepository reviewRepository;

  public double getStars(Restaurant restaurant) {
    final List<Review> reviews = reviewRepository.findByRestaurantId(restaurant.getId());
    return reviews
      .stream()
      .mapToDouble(Review::getStars)
      .average()
      .orElse(0);
  }

  public List<Review> getReviews(Restaurant restaurant) {
    return reviewRepository.findByRestaurantId(restaurant.getId());
  }

  public int getNumberOfReviews(Restaurant restaurant) {
    return reviewRepository.countByRestaurantId(restaurant.getId());
  }

}
