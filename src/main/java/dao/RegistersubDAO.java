package dao;

import model.ClassCredit;
import model.ClassSubject;
import model.Registersub;
import model.Student;
import org.hibernate.Session;
import utils.HibernateUtils;

import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class RegistersubDAO {
    public List<Registersub> findAll() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Registersub> list = new ArrayList<>();
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

    public List<Registersub> findByStudent(Student student) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Registersub> result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Registersub> query = builder.createQuery(Registersub.class);
            Root<Registersub> root = query.from(Registersub.class);
            query.select(root).where(builder.equal(root.get("student"), student));
            //query.select(root).where(builder.isNull(root.get("classPayroll")));
            Query<Registersub> q = s.createQuery(query);
            result = q.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return result;
    }


    public List<Registersub> findByClass(ClassSubject classSubject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Registersub> result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Registersub> query = builder.createQuery(Registersub.class);
            Root<Registersub> root = query.from(Registersub.class);
            query.select(root).where(builder.equal(root.get("classSubject"), classSubject));

            //query.select(root).where(builder.isNull(root.get("classPayroll")));
            Query<Registersub> q = s.createQuery(query);

            result = q.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return result;
    }

    public  List<Registersub> findByClassStu(ClassSubject classSubject, Student student) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Registersub> result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Registersub> query = builder.createQuery(Registersub.class);
            Root<Registersub> root = query.from(Registersub.class);
            if (classSubject != null && student != null) {
                query.select(root).where(
                        builder.and(
                                builder.equal(root.get("classSubject"), classSubject),
                                builder.equal(root.get("student"), student)
                        )
                );
            } else if (classSubject != null) {
                query.select(root).where(builder.equal(root.get("classSubject"), classSubject));
            } else if (student != null) {
                query.select(root).where(builder.equal(root.get("student"), student));
            }else {
                query.select(root).where();
            }
            //query.select(root).where(builder.isNull(root.get("classPayroll")));
            Query<Registersub> q = s.createQuery(query);
            result = q.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return result;
    }



    public Registersub findById(int id) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        Registersub registersub = new Registersub();
        try {
            s.beginTransaction();
            registersub = s.get(Registersub.class, id);
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return registersub;
    }


    public Registersub create(Registersub student) {
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

    public void update(Registersub student) {
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

    public void delete(Registersub student) {
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
