package it.register.edu.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import it.register.edu.graphql.model.Reply;
import it.register.edu.graphql.model.Review;
import it.register.edu.graphql.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewFieldsResolver implements GraphQLResolver<Review> {

  @Autowired
  private ReplyRepository replyRepository;

  public List<Reply> getReplies(Review review) {
    return replyRepository.findByReviewId(review.getId());
  }


}
