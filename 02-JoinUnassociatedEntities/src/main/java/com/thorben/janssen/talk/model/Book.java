package com.thorben.janssen.talk.model;

import java.util.*;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    private int version;

    private String title;

    @Temporal(TemporalType.DATE)
    private Date publishingDate;

    @OneToMany(mappedBy = "book",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Review> reviews;

    public void addReview(Review review) {
        this.reviews.add(review);
        review.setBook(this);
    }

    public void removeReview(Review review) {
        review.setBook(null);
        this.reviews.remove(review);
    }

    public void removeReviews() {
        Iterator<Review> iterator = this.reviews.iterator();
        while(iterator.hasNext()) {
            Review review = iterator.next();
            review.setBook(null);
            iterator.remove();
        }
    }
}