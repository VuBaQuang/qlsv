package dao;


import model.ClassPayroll;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {

    public  List<ClassPayroll> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassPayroll> list = new ArrayList<>();
        try {
            s.beginTransaction();
            javax.persistence.Query query = s.createQuery("from ClassPayroll");
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


    public int create(ClassPayroll aClass) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(aClass);
            s.getTransaction().commit();
        } catch (ConstraintViolationException e){
            return 2;
        }
        catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            return 1;
        } finally {
            s.close();
        }
        return 0;
    }

    public ClassPayroll findById(int id) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        ClassPayroll aClass = null;
        try {
            s.beginTransaction();
            aClass = s.get(ClassPayroll.class, id);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return aClass;
    }


    public void update(ClassPayroll aClass) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.update(aClass);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public void delete(ClassPayroll aClass) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.delete(aClass);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public ClassPayroll getClassByName(String name) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        ClassPayroll result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<ClassPayroll> query = builder.createQuery(ClassPayroll.class);
            Root<ClassPayroll> root = query.from(ClassPayroll.class);
            query.select(root).where(builder.equal(root.get("name"), name));
            Query<ClassPayroll> q = s.createQuery(query);
            result = q.getSingleResult();

            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
        return result;
    }


}
