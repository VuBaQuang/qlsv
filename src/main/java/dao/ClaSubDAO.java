package dao;

import model.ClassCredit;
import model.ClassSubject;
import model.Subject;
import org.hibernate.Session;
import utils.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ClaSubDAO {


    public List<ClassSubject> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassSubject> list = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("from ClassSubject ");
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

    public List<ClassSubject> findBySub(Subject subject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassSubject> list = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<ClassSubject> query = builder.createQuery(ClassSubject.class);
            Root<ClassSubject> root = query.from(ClassSubject.class);
            query.select(root).where(builder.equal(root.get("subject"), subject));
            //query.select(root).where(builder.isNull(root.get("classPayroll")));

            org.hibernate.query.Query<ClassSubject> q = s.createQuery(query);
            list = q.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return list;
    }


    public ClassSubject findById(int id) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        ClassSubject classSubject = new ClassSubject();
        try {
            s.beginTransaction();
            classSubject = s.get(ClassSubject.class, id);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return classSubject;
    }

    public List<ClassSubject> getSubjectByClass(ClassCredit classCredit) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassSubject> list = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<ClassSubject> query = builder.createQuery(ClassSubject.class);
            Root<ClassSubject> root = query.from(ClassSubject.class);
            query.select(root).where(builder.equal(root.get("classCredit"), classCredit));
            //query.select(root).where(builder.isNull(root.get("classPayroll")));

            org.hibernate.query.Query<ClassSubject> q = s.createQuery(query);
            list = q.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return list;
    }


    public ClassSubject create(ClassSubject classSubject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(classSubject);
            s.getTransaction().commit();
            return classSubject;
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            return null;
        } finally {
            s.close();
        }
    }

    public void update(ClassSubject classSubject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.update(classSubject);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public void delete(ClassSubject classSubject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.remove(classSubject);
            s.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }


}
