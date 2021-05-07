package it.register.edu.graphql.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import it.register.edu.graphql.model.Reply;
import it.register.edu.graphql.model.Review;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionResolverTest {

  @InjectMocks
  private SubscriptionResolver resolver;

  @Before
  public void setUp() {
    ReflectionTestUtils.setField(resolver, "replies", Flux.just(
        Reply.builder().id(987).review(Review.builder().id(123).build()).build(),
        Reply.builder().id(876).review(Review.builder().id(234).build()).build(),
        Reply.builder().id(765).review(Review.builder().id(123).build()).build()
    ));
  }

  @Test
  public void replyAdded() {
    Flux<Reply> result = (Flux<Reply>) resolver.replyAdded("123");
    List<Reply> list = result.collectList().block();
    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals(987, list.get(0).getId().intValue());
    assertEquals(765, list.get(1).getId().intValue());
  }
}
