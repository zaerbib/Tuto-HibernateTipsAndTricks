package com.thorben.janssen.talk;

import com.thorben.janssen.talk.model.Author;
import com.thorben.janssen.talk.utils.GenerateAuthor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class TestUUIDPrimaryKey {

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
    public void testUUIDPrimaryKeyV4() {
        log.info("... testUUIDPrimaryKeyV4 ...");

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        GenerateAuthor.generateAuthors(10_000)
                        .forEach(em::persist);

        log.info("Persist new Authors entity.");

        Author a = (Author) em.createNativeQuery("select * from author limit 1", Author.class).getSingleResult();

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();

        UUID uuid = a.getId();

        a = em.find(Author.class, uuid);
        Assert.assertEquals(uuid, a.getId());

        em.getTransaction().commit();
        em.close();
    }
}
