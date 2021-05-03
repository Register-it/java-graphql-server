package it.register.edu.graphql.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import it.register.edu.graphql.model.Restaurant;
import it.register.edu.graphql.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantResolverTest {

  @Mock
  private RestaurantRepository mockRepository;

  @InjectMocks
  private RestaurantResolver resolver;

  @Test
  public void getRestaurants() {
    List<Restaurant> expected = List.of(new Restaurant(), new Restaurant());
    when(mockRepository.findAll()).thenReturn(expected);

    List<Restaurant> result = resolver.getRestaurants(null);

    assertEquals(expected, result);
  }

  @Test
  public void getRestaurantFound() {
    Restaurant expected = new Restaurant();
    when(mockRepository.findById(123)).thenReturn(Optional.of(expected));

    Optional<Restaurant> result = resolver.getRestaurant("123");

    assertTrue(result.isPresent());
    assertEquals(expected, result.get());
  }

  @Test
  public void getRestaurantNotFound() {
    when(mockRepository.findById(123)).thenReturn(Optional.empty());

    Optional<Restaurant> result = resolver.getRestaurant("123");

    assertFalse(result.isPresent());
  }

}
