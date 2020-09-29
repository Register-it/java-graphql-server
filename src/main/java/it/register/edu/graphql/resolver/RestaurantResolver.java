package it.register.edu.graphql.resolver;


import graphql.kickstart.tools.GraphQLQueryResolver;
import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;

import it.register.edu.graphql.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestaurantResolver implements GraphQLQueryResolver {

  @Autowired
  private RestaurantRepository repository;

  public List<Restaurant> getRestaurants() {
    return repository.findAll();
  }

  public Optional<Restaurant> getRestaurant(String id) {
    return repository.findById(Integer.parseInt(id));
  }


}
