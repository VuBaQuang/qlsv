package dao;

import model.ClassCredit;
import model.ClassSubject;
import model.Student;
import model.Subject;
import org.hibernate.Session;
import utils.HibernateUtils;

import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClaSubDAO {


    public static List<ClassSubject> findAll() {
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
            if (subject != null) {
                query.select(root).where(builder.equal(root.get("subject"), subject));
            } else {
                query.select(root).where();
            }
            //query.select(root).where(builder.isNull(root.get("classPayroll")));

            Query<ClassSubject> q = s.createQuery(query);
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


    public static ClassSubject findById(int id) {
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


    public List<ClassSubject> getByClass(ClassCredit classCredit) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassSubject> list = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<ClassSubject> query = builder.createQuery(ClassSubject.class);
            Root<ClassSubject> root = query.from(ClassSubject.class);
            query.select(root).where(builder.equal(root.get("classCredit"), classCredit));
            //query.select(root).where(builder.isNull(root.get("classPayroll")));

            Query<ClassSubject> q = s.createQuery(query);
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

    public List<ClassSubject> getBySub(Subject subject) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassSubject> list = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<ClassSubject> query = builder.createQuery(ClassSubject.class);
            Root<ClassSubject> root = query.from(ClassSubject.class);
            query.select(root).where(builder.equal(root.get("subject"), subject));
            //query.select(root).where(builder.isNull(root.get("classPayroll")));
            Query<ClassSubject> q = s.createQuery(query);
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

    public ClassSubject getBySubFtClass(Subject subject, ClassCredit classCredit) {


        Session s = HibernateUtils.getSessionFactory().openSession();
        ClassSubject result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<ClassSubject> query = builder.createQuery(ClassSubject.class);
            Root<ClassSubject> root = query.from(ClassSubject.class);
            query.select(root).where(
                    builder.and(
                            builder.equal(root.get("subject"), subject),
                            builder.equal(root.get("classCredit"), classCredit)
                    )
            );
            Query<ClassSubject> q = s.createQuery(query);
            result = q.getSingleResult();
            s.getTransaction().commit();
        } catch (NoResultException e) {
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
            return result;
        } finally {
            s.close();
        }
        return result;
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

    public List<ClassSubject> findBySubCla(Subject subject, ClassCredit classCredit) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassSubject> result = null;
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<ClassSubject> query = builder.createQuery(ClassSubject.class);
            Root<ClassSubject> root = query.from(ClassSubject.class);
            if (subject != null && classCredit != null) {
                query.select(root).where(
                        builder.and(
                                builder.equal(root.get("subject"), subject),
                                builder.equal(root.get("classCredit"), classCredit)
                        )
                );
            } else if (classCredit != null) {
                query.select(root).where(builder.equal(root.get("classCredit"), classCredit));
            } else if (subject != null) {
                query.select(root).where(builder.equal(root.get("subject"), subject));
            } else {
                query.select(root).where();
            }
            //query.select(root).where(builder.isNull(root.get("classPayroll")));
            Query<ClassSubject> q = s.createQuery(query);
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


    public List<ClassSubject> findRangeDay(ClassSubject classSubject, Student student) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassSubject> claSub = null;
        try {
            s.beginTransaction();
            String hql = "SELECT DISTINCT cs " +
                    "FROM ClassSubject as cs," +
                    "     Subject as su " +
                    "WHERE (su = cs.subject)" +
                    "     and (:start_time >= cs.endTime or :end_time <= cs.startTime)" +
                    "      and (cs.day = :day)  ";
            org.hibernate.query.Query query = s.createQuery(hql);
            query.setParameter("start_time", classSubject.getStartTime());
            query.setParameter("end_time", classSubject.getEndTime());
            query.setParameter("day", classSubject.getDay());
            claSub = query.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return claSub;
    }

    public  List<ClassSubject> findRangeTime(ClassSubject classSubject, Student student) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<ClassSubject> claSub = null;
        try {
            s.beginTransaction();
            String hql = "SELECT DISTINCT cs " +
                    "FROM ClassSubject as cs," +
                    "     Subject as su," +
                    "     Student as st," +
                    "     Registersub as re " +
                    "WHERE (su = cs.subject)" +
                    "  and (st = re.student)" +
                    "  and re.classSubject = cs" +
                    "  and (:start_time >= cs.todStart or :end_time <= cs.todEnd)" +
                    "  and cs.day = :day" +
                    "  and st = :student";
            org.hibernate.query.Query query = s.createQuery(hql);
            query.setParameter("start_time", classSubject.getTodStart());
            query.setParameter("end_time", classSubject.getTodEnd());
            query.setParameter("day", classSubject.getDay());
            query.setParameter("student",student);
            claSub = query.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return claSub;
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
