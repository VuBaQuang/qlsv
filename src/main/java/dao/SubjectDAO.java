package dao;

import model.Subject;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import utils.HibernateUtils;

import org.hibernate.query.Query;

import javax.persistence.NoResultException;
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
        } finally {
            s.close();
        }
        return list;
    }
    public Subject findById(int id) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        Subject aClass = null;
        try {
            s.beginTransaction();
            aClass = s.get(Subject.class, id);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return aClass;
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
            Query<Subject> q = s.createQuery(query);
            result = q.getSingleResult();
            s.getTransaction().commit();
        }catch (NoResultException e){
            System.out.println("No result !");
        }
        catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return result;
    }

    public int create(Subject subject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(subject);
            s.getTransaction().commit();
            return 0;
        }
        catch (ConstraintViolationException e){
            return 1;
        }
        catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            return 2;
        } finally {
            s.close();
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
        } finally {
            s.close();
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
        } finally {
            s.close();
        }
    }
}