package dao;

import model.User;
import org.hibernate.Session;
import utils.HibernateUtils;

import javax.persistence.NoResultException;

import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<User> list = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("from User");
            list = query.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return list;
    }

    public User findByName(String user) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        User result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(builder.equal(root.get("user"), user));
            Query<User> q = s.createQuery(query);
            result = q.getSingleResult();
            s.getTransaction().commit();
        } catch (NoResultException e) {
            System.out.println("No Result !");
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
        return result;
    }

    public User findById(int id) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        User user = new User();
        try {
            s.beginTransaction();
            user = s.get(User.class, id);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return user;
    }

    public User create(User user) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(user);
            s.getTransaction().commit();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            return null;
        }
    }

    public void update(User user) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.update(user);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public void delete(User user) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.remove(user);
            s.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }


}
