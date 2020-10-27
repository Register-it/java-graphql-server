package it.register.edu.graphql.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import it.register.edu.graphql.model.Comment;
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
    ReflectionTestUtils.setField(resolver, "comments", Flux.just(
        Comment.builder().id(987).review(Review.builder().id(123).build()).build(),
        Comment.builder().id(876).review(Review.builder().id(234).build()).build(),
        Comment.builder().id(765).review(Review.builder().id(123).build()).build()
    ));
  }

  @Test
  public void commentAdded() {
    Flux<Comment> result = (Flux<Comment>) resolver.commentAdded("123");
    List<Comment> list = result.collectList().block();
    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals(987, list.get(0).getId().intValue());
    assertEquals(765, list.get(1).getId().intValue());
  }
}
