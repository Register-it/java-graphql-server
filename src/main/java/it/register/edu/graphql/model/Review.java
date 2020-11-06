package it.register.edu.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String message;

  private Integer stars;

  @ManyToOne(fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.PERSIST)
  @JoinColumn(name = "RESTAURANT_ID")
  private Restaurant restaurant;
}
