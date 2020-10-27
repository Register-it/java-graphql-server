package it.register.edu.graphql.config;

import it.register.edu.graphql.model.Comment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Configuration
public class SubscriptionConfiguration {

  @Bean
  public UnicastProcessor<Comment> commentPublisher() {
    return UnicastProcessor.create();
  }

  @Bean
  public Flux<Comment> comments(UnicastProcessor<Comment> publisher) {
    return publisher.publish().autoConnect(0);
  }
}
