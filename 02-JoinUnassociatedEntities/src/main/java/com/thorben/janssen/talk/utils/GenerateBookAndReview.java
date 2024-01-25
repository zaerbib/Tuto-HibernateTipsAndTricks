package com.thorben.janssen.talk.utils;

import com.thorben.janssen.talk.model.Book;
import com.thorben.janssen.talk.model.Review;
import net.datafaker.Faker;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateBookAndReview {

    public static Book generateBook(int numberReviews) {
        Faker faker = new Faker();
        Book book = new Book();
        book.setTitle(faker.book().title());
        book.setVersion(faker.number().positive());
        book.setPublishingDate(faker.date().birthday());
        book.setReviews(IntStream.range(0, numberReviews)
                .mapToObj(item -> generateReview(book))
                .collect(Collectors.toList()));
        return book;
    }

    public static Review generateReview(Book book) {
        Faker faker = new Faker();
        Review review = new Review();
        review.setBook(book);
        review.setComment(faker.shakespeare().romeoAndJulietQuote());

        return review;
    }
}
