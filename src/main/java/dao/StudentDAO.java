package dao;

import model.ClassSubject;
import model.Student;
import model.Subject;
import org.hibernate.Session;
import utils.HibernateUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentDAO {


    public List<Student> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Student> list = new ArrayList<>();
        try {
            s.beginTransaction();
            Query query = s.createQuery("from Student ");
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


    public Student findById(int id) {
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

    public List<Student> getStudentNone() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Student> list = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Student> query = builder.createQuery(Student.class);
            Root<Student> root = query.from(Student.class);
            query.select(root).where(builder.isNull(root.get("classPayroll")));
            org.hibernate.query.Query<Student> q = s.createQuery(query);
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

    public  List listStudent(Subject subject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List list = null;
        try {
            s.beginTransaction();
            String hql = "FROM Student as st WHERE id NOT in (SELECT DISTINCT re.student FROM Registersub as re join re.classSubject as cla WHERE  cla.subject=:subject)";
            org.hibernate.query.Query query = s.createQuery(hql);
            query.setParameter("subject", subject);
            list = query.list();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return list;
    }

    public static List listStudent(Date date1, Date date2) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List list = null;
        try {
            s.beginTransaction();
            String hql = "from  ClassSubject as cs where cs.startTime = :date1 ";
            org.hibernate.query.Query query = s.createQuery(hql);
            query.setParameter("date1", date1);

            list = query.list();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return list;
    }

//    public static void main(String[] args) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        try {
//            Date startDate = dateFormat.parse("01-09-2019");
//            Date endDate = dateFormat.parse("01-10-2019");
//            for (Object o : listStudent(startDate, endDate)) {
//                ClassSubject student = (ClassSubject) o;
//                System.out.println(student.getStartTime().toString());
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//    }

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
        } finally {
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
