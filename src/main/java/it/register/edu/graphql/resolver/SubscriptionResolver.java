package it.register.edu.graphql.resolver;


import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import it.register.edu.graphql.model.Comment;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class SubscriptionResolver implements GraphQLSubscriptionResolver {

  @Autowired
  private Flux<Comment> comments;

  public Publisher<Comment> commentAdded(String reviewId) {
    return comments.filter(comment -> comment.getReview().getId().equals(Integer.valueOf(reviewId)));
  }
}
