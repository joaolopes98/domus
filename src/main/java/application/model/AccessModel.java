package application.model;

import application.controller.object.Access;
import application.controller.object.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AccessModel {
    public static boolean isLogin(String access_user, String pass_user) {

        // TODO Alteração para MD5
        /* Encripatação MD5 da senha para verificação no banco */
        try {
            MessageDigest m =  MessageDigest.getInstance("SHA-512");
            m.update(pass_user.getBytes(),0,pass_user.length());
            pass_user = new BigInteger(1,m.digest()).toString(16);

            if (pass_user.length() != 32){
                int zeros = 32 - pass_user.length();
                StringBuilder passBuilder = new StringBuilder(pass_user);
                for (int i = 0; i < zeros; i++){
                    passBuilder.insert(0, "0");
                }
                pass_user = passBuilder.toString();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

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
}
