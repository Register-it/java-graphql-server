package it.register.edu.graphql.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import it.register.edu.graphql.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserResolverTest {

  @InjectMocks
  private UserResolver resolver;

  @Test
  public void me() {
    User result = resolver.me();
    assertNotNull(result);
    assertEquals(UserResolver.USERNAME, result.getUsername());
    assertEquals(UserResolver.FIRST_NAME, result.getFirstName());
    assertEquals(UserResolver.LAST_NAME, result.getLastName());
  }
}
