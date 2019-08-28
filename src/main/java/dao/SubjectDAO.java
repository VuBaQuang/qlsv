package dao;

import model.Subject;
import org.hibernate.Session;
import utils.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public List<Subject> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Subject> list = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("from Subject");
            list = query.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
        return list;
    }

    public Subject getByName(String name) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        Subject result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
            Root<Subject> root = query.from(Subject.class);
            query.select(root).where(builder.equal(root.get("name"), name));
            //query.select(root).where(builder.isNull(root.get("classPayroll")));
            org.hibernate.query.Query<Subject> q = s.createQuery(query);
            result = q.getSingleResult();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return result;
    }

    public void create(Subject subject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(subject);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
    }

    public void update(Subject subject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.update(subject);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
    }

    public void delete(Subject subject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.delete(subject);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
    }
}