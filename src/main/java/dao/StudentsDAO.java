package dao;

import model.Students;
import org.hibernate.Session;
import utils.HibernateUtils;

import javax.persistence.Query;
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



    public Students findById(int id) {
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

//    public static void main(String[] args) {
//        Iterator<ClassesStudents> iterator = findById(2).getClassesStudentses().iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next().getClasses().getName());
//        }

    //    }
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
