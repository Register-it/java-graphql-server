package it.register.edu.graphql.config;

import it.register.edu.graphql.model.Reply;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Configuration
public class SubscriptionConfiguration {

  @Bean
  public UnicastProcessor<Reply> replyPublisher() {
    return UnicastProcessor.create();
  }

  @Bean
  public Flux<Reply> replies(UnicastProcessor<Reply> publisher) {
    return publisher.publish().autoConnect(0);
  }
}
