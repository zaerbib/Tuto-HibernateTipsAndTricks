package com.thorben.janssen.talk.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDateTime;
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

	private AuthorStatus status;

	@ManyToMany(mappedBy = "authors")
	private Set<Book> books = new HashSet<>();

	@Generated(GenerationTime.ALWAYS)
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime lastUpdate;

}