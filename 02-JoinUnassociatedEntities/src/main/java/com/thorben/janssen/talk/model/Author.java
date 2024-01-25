package com.thorben.janssen.talk.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Version
	private int version;

	private String firstName;

	private String lastName;

	@Enumerated(EnumType.STRING)
	private AuthorStatus status;

	@ManyToMany(mappedBy = "authors")
	private Set<Book> books = new HashSet<>();

}