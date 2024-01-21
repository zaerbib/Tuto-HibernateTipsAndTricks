package com.thorben.janssen.talk.utils;

import com.thorben.janssen.talk.model.Author;
import com.thorben.janssen.talk.model.AuthorStatus;
import net.datafaker.Faker;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateAuthor {


    public static Author generateAuthor() {
        Faker faker = new Faker(new Random());

        Author author = new Author();
        author.setFirstName(faker.name().firstName());
        author.setLastName(faker.name().lastName());
        author.setStatus(faker.options().option(AuthorStatus.class));
        return author;
    }

    public static List<Author> generateAuthors(int number) {
        return IntStream.range(0, number).mapToObj(item -> generateAuthor()).collect(Collectors.toList());
    }
}
