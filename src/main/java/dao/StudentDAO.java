package dao;

import model.Student;
import org.hibernate.Session;
import utils.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {


    public static List<Student> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Student> list = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("from Student ");
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

    public static void main(String[] args) {
        System.out.println(findAll().get(0).getName());
    }

    public  Student findById(int id) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        Student student = new Student();
        try {
            s.beginTransaction();
            student = s.get(Student.class, id);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return student;
    }
    public  List<Student> getStudentNone() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Student> list =  null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Student> query = builder.createQuery(Student.class);
            Root<Student> root = query.from(Student.class);
            query.select(root).where(builder.isNull(root.get("classes")));
            org.hibernate.query.Query<Student> q = s.createQuery(query);
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


    public Student create(Student student) {
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

    public void update(Student student) {
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

    public void delete(Student student) {
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