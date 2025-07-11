package com.github.bladeehl.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import lombok.val;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseHelper {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static void shutdown() {
        sessionFactory.close();
    }

    public static void doInTransaction(final Consumer<Session> action) {
        try (val session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        }
    }

    public static <T> T returnInTransaction(final Function<Session, T> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        }
    }
}
