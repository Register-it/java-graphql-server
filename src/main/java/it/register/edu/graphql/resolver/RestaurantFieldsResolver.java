package it.register.edu.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantFieldsResolver implements GraphQLResolver<Restaurant> {

  @Autowired
  private ReviewRepository reviewRepository;

  public Double getRating(Restaurant restaurant) {
    final List<Review> reviews = reviewRepository.findByRestaurantId(restaurant.getId());
    return reviews
      .stream()
      .mapToDouble(Review::getRating)
      .average()
      .orElse(0);
  }

  public List<Review> getReviews(Restaurant restaurant, Integer rating) {
    final int requiredRating = rating == null ? 0 : rating;
    return reviewRepository.findByRestaurantId(restaurant.getId())
      .stream()
      .filter(r -> r.getRating() >= requiredRating)
      .collect(Collectors.toList());
  }

  public int getNumberOfReviews(Restaurant restaurant) {
    return reviewRepository.countByRestaurantId(restaurant.getId());
  }

}
