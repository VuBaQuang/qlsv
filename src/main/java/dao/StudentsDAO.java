package dao;

import model.Students;
import org.hibernate.Session;
import utils.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class StudentsDAO {


    public  List<Students> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Students> list = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("from Students ");
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



    public  Students findById(int id) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        Students student = new Students();
        try {
            s.beginTransaction();
            student = s.get(Students.class, id);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return student;
    }
    public  List<Students> getStudentNone() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Students> list =  null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Students> query = builder.createQuery(Students.class);
            Root<Students> root = query.from(Students.class);
            query.select(root).where(builder.isNull(root.get("classes")));
            org.hibernate.query.Query<Students> q = s.createQuery(query);
            list = q.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }finally {
            s.close();
        }
        return list;
    }


    public Students create(Students student) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.save(student);
            s.getTransaction().commit();
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            return null;
        }finally {
            s.close();
        }
    }

    public void update(Students student) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.update(student);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }

    public void delete(Students student) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            s.remove(student);
            s.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
    }


}
