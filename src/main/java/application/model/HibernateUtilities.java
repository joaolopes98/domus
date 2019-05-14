package application.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public abstract class HibernateUtilities {
    private static SessionFactory sessionFactory;
    private static Session session;

    public static boolean load(){
        if(sessionFactory == null) {
            Configuration configuration = new Configuration().configure("/hbm/hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
        }
        return !sessionFactory.isClosed();
    }

    public static Session getSession(){
        if(session != null && session.isConnected()) session.close();
        session = sessionFactory.openSession();
        return session;
    }

    public static void close() {
        sessionFactory.close();
    }
}
