package it.register.edu.graphql.resolver;


import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import it.register.edu.graphql.model.Reply;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class SubscriptionResolver implements GraphQLSubscriptionResolver {

  @Autowired
  private Flux<Reply> replies;

  public Publisher<Reply> replyAdded(String reviewId) {
    return replies.filter(reply -> reply.getReview().getId().equals(Integer.valueOf(reviewId)));
  }
}
