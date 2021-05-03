package it.register.edu.graphql.repository;

import it.register.edu.graphql.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

  List<Restaurant> findByCityLikeIgnoreCase(String city);
}
