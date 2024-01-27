package com.thorben.janssen.talk;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.thorben.janssen.talk.model.Book;
import com.thorben.janssen.talk.utils.GenerateBookAndReview;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class TestJoinUnassociatedEntities {

    private static final int CHUNK_SIZE = 1000;
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
    public void joinUnassociated() {
        log.info("... joinUnassociated ...");
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        ExecutorService executorSimple = Executors.newFixedThreadPool(4);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        List<Book> toUpdate = Collections.synchronizedList(new ArrayList<>());
        Stopwatch watch = Stopwatch.createStarted();
        List<Void> tmp = IntStream.range(0, 10000).mapToObj(item -> CompletableFuture.runAsync(() -> {
            toUpdate.add(GenerateBookAndReview
                    .generateBook(ThreadLocalRandom.current().nextInt(1, 5),
                            ThreadLocalRandom.current().nextInt(1, 5)));
        }, executorSimple).join()).toList();
        watch.stop();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        List<Void> list = Lists.partition(toUpdate, CHUNK_SIZE).stream().map(item -> CompletableFuture.runAsync(() -> saveAllFlush(em, item), executor).whenCompleteAsync((t, ex) -> {
            if (ex != null) {
                failed.incrementAndGet();
            }
            success.incrementAndGet();
        }).join()).toList();

        /*Query q = em.createQuery("SELECT b.title, count(r.id) FROM Book b INNER JOIN Review r ON r.book.id = b.id GROUP BY b.title").setFirstResult(0).setMaxResults(5);
        Object[] r = (Object[]) q.getResultList().toArray();
        log.info(r[0] + " received " + r[1] + " reviews.");*/

        em.getTransaction().commit();
        em.close();

        log.info("Failed : " + failed.get());
        log.info("Success : " + success.get());
        log.info("Elapsed time for creating list in ms : " + watch.elapsed(TimeUnit.MILLISECONDS));
    }

    private void saveAllFlush(EntityManager em, List<Book> books) {
        books.forEach(em::persist);
        em.flush();
    }
}
