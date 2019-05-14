package application.model;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericModel {
    static boolean create(Model model){
        boolean result = true;
        Session session = HibernateUtilities.getSession();
        try {
            boolean active = session.getTransaction().isActive();
            if(!active) {
                session.beginTransaction();
                session.save(model);
                session.getTransaction().commit();
            } else {
                session.save(model);
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            result = false;
        } finally {
            session.close();
        }
        return result;
    }

    static Object get(String queryHQL){
        Object object;
        Session session = HibernateUtilities.getSession();
        try{
            Query query = session.createQuery(queryHQL);
            object = query.uniqueResult();
        } catch (Exception e){
            e.printStackTrace();
            object = null;
        }
        return object;
    }

    static List getAll(String queryHQL, int quantity){
        List objects = new ArrayList<>();
        Session session = HibernateUtilities.getSession();
        try{
            Query query = session.createQuery(queryHQL);
            query.setMaxResults(quantity);
            objects = query.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        }

        return objects;
    }

    static List getAll(String queryHQL){
        List objects = new ArrayList<>();
        Session session = HibernateUtilities.getSession();
        try{
            Query query = session.createQuery(queryHQL);
            objects = query.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        }
        return objects;
    }

    static Object getLast(String queryHQL){
        Object object;
        Session session = HibernateUtilities.getSession();
        try{
            Query query = session.createQuery(queryHQL);
            query.setMaxResults(1);
            object = query.uniqueResult();
        } catch (Exception e){
            e.printStackTrace();
            object = null;
        }
        return object;
    }

    static boolean update(Model model) {
        boolean result = true;
        Session session = HibernateUtilities.getSession();

        try {
            boolean active = session.getTransaction().isActive();
            if(!active) {
                session.beginTransaction();
                session.update(model);
                session.getTransaction().commit();
            } else {
                session.update(model);
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            result = false;
        } finally {
            session.close();
        }
        return result;
    }
}
