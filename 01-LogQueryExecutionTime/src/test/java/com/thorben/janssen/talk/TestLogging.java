package com.thorben.janssen.talk;

import com.thorben.janssen.talk.model.Author;
import com.thorben.janssen.talk.utils.GenerateAuthor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLogging {

	Logger log = LogManager.getLogger(this.getClass().getName());

    private EntityManagerFactory emf;

    @Before
    public void init() {
        emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    @After
    public void close() {
        emf.close();
    }

    @Test
    public void selectAuthors() {
        log.info("... selectAuthors ...");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        GenerateAuthor.generateAuthors(10)
                .forEach(em::persist);

        TypedQuery<Author> q = em.createQuery("SELECT a FROM Author a WHERE a.id = :id", Author.class);
        q.setParameter("id", 1L);
        Author a = q.getSingleResult();

        a.setFirstName("Changed");
        
        em.getTransaction().commit();
        em.close();
    }
}
