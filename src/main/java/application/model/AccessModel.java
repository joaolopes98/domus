package application.model;

import application.controller.object.Access;
import application.controller.object.User;
import application.view.auxiliary.Formatter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public abstract class AccessModel {
    public static boolean isLogin(String access_user, String pass_user) {

        pass_user = Formatter.formatSHA512(pass_user);

        String queryLogin = "FROM Access WHERE user = :access_user  AND password = :pass_user AND status = TRUE ";

        try (Session session = HibernateUtilities.getSession()) {
            Query query = session.createQuery(queryLogin);
            query.setParameter("access_user", access_user);
            query.setParameter("pass_user", pass_user);
            Access user = (Access) query.getSingleResult();

            if (user != null) {
                User.setUser(user);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean create(Access access){
        return GenericModel.create(access);
    }

    public static List<Access> getAll(String search){
        return GenericModel.getAll("FROM Access " + search);
    }

    public static boolean update(Access access) {
        return GenericModel.update(access);
    }
}
