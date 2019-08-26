package dao;


import model.Classes;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ClassesDAO {

    public  List<Classes> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Classes> list = new ArrayList<>();
        try {
            s.beginTransaction();
            javax.persistence.Query query = s.createQuery("from Classes");
            list =  query.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return list;
    }


    public void create(Classes classes) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(classes);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public void update(Classes classes) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.update(classes);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }
    public void delete(Classes classes) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.delete(classes);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }
    public Classes getClassByName(String name) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        Classes result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Classes> query = builder.createQuery(Classes.class);
            Root<Classes> root = query.from(Classes.class);
            query.select(root).where(builder.equal(root.get("name"), name));
            Query<Classes> q = s.createQuery(query);
             result = q.getSingleResult();

            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
        return result;
    }


}
