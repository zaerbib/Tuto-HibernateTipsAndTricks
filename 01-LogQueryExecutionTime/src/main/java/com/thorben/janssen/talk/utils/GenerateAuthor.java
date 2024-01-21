package com.thorben.janssen.talk.utils;

import com.thorben.janssen.talk.model.Author;
import net.datafaker.Faker;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateAuthor {

    private static final Faker faker = new Faker(new Random());

    public static Author generateAuthor() {
        Author author = new Author();
        author.setFirstName(faker.name().firstName());
        author.setLastName(faker.name().lastName());

        return author;
    }

    public static List<Author> generateAuthors(int number) {
        return IntStream.range(0, number).mapToObj(item -> generateAuthor()).collect(Collectors.toList());
    }
}
