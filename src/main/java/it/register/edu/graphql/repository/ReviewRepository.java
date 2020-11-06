package it.register.edu.graphql.repository;

import it.register.edu.graphql.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

  List<Review> findByRestaurantId(int restaurantId);

  int countByRestaurantId(int restaurantId);
}
