package dao;

import model.ClassCredit;
import org.hibernate.Session;
import utils.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ClassCreditDAO {
    public List<ClassCredit> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassCredit> list = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("from ClassCredit ");
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


    public ClassCredit findById(int id) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        ClassCredit classCredit = new ClassCredit();
        try {
            s.beginTransaction();
            classCredit = s.get(ClassCredit.class, id);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return classCredit;
    }

    public ClassCredit getIdByName(String name) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        ClassCredit result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<ClassCredit> query = builder.createQuery(ClassCredit.class);
            Root<ClassCredit> root = query.from(ClassCredit.class);
            query.select(root).where(builder.equal(root.get("name"), name));
            //query.select(root).where(builder.isNull(root.get("classPayroll")));
            org.hibernate.query.Query<ClassCredit> q = s.createQuery(query);
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


    public ClassCredit create(ClassCredit classCredit) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(classCredit);
            s.getTransaction().commit();
            return classCredit;
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            return null;
        } finally {
            s.close();
        }
    }

    public void update(ClassCredit classCredit) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.update(classCredit);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public void delete(ClassCredit classCredit) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.remove(classCredit);
            s.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

}
