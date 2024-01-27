package com.thorben.janssen.talk.utils;

import com.thorben.janssen.talk.model.Author;
import com.thorben.janssen.talk.model.AuthorStatus;
import com.thorben.janssen.talk.model.Book;
import com.thorben.janssen.talk.model.Review;
import net.datafaker.Faker;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateBookAndReview {

    public final static Faker faker = new Faker();

    public static Book generateBook(int numberReviews, int authors) {
        Book book = new Book();
        book.setTitle(faker.book().title());
        book.setVersion(faker.number().positive());
        book.setPublishingDate(faker.date().birthday());
        book.setReviews(IntStream.range(0, numberReviews)
                .mapToObj(item -> generateReview(book))
                .collect(Collectors.toList()));
        book.setAuthors(IntStream.range(0, authors)
                .mapToObj(item -> generateAuthor(book))
                .collect(Collectors.toSet()));
        return book;
    }

    public static Review generateReview(Book book) {
        Review review = new Review();
        review.setBook(book);
        review.setComment(faker.shakespeare().romeoAndJulietQuote());

        return review;
    }

    public static Author generateAuthor(Book book) {
        Author author = new Author();
        author.setFirstName(faker.name().firstName());
        author.setLastName(faker.name().lastName());
        author.setStatus(faker.options().option(AuthorStatus.class));
        author.setVersion(faker.number().numberBetween(1, 100));
        author.getBooks().add(book);

        return author;
    }
}
