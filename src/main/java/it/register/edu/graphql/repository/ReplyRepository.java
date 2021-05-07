package it.register.edu.graphql.repository;

import it.register.edu.graphql.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

  List<Reply> findByReviewId(Integer id);
}
