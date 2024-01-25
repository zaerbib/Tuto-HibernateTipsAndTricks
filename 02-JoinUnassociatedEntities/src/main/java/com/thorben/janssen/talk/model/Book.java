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
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_author",
            joinColumns = { @JoinColumn(name = "fk_book") },
            inverseJoinColumns = { @JoinColumn(name = "fk_author") })
    private Set<Author> authors = new HashSet<>();

    public void addReview(Review review) {
        this.reviews.add(review);
        review.setBook(this);
    }

    public void removeReview(Review review) {
        review.setBook(null);
        this.reviews.remove(review);
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
    }

    public void removeReviews() {
        Iterator<Review> iterator = this.reviews.iterator();
        while(iterator.hasNext()) {
            Review review = iterator.next();
            review.setBook(null);
            iterator.remove();
        }
    }

    public void removeAuthors() {
        Iterator<Author> iterator = this.authors.iterator();
        while(iterator.hasNext()) {
            Author author = iterator.next();
            author.getBooks().remove(this);
            iterator.remove();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}