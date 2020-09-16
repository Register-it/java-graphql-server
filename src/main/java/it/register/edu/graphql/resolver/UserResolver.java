package it.register.edu.graphql.resolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import it.register.edu.graphql.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserResolver implements GraphQLQueryResolver {

  public static final String USERNAME = "guest";
  public static final String FIRST_NAME = "Anonymous";
  public static final String LAST_NAME = "User";

  public User me() {
    return User.builder()
        .username(USERNAME)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .build();
  }

}
