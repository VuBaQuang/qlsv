package dao;

import model.District;
import model.Province;
import model.Ward;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {
    public List<Province> getAllProvince() {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Province> list = new ArrayList<>();
        try {
            s.beginTransaction();
            list = s.createCriteria(Province.class).list();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        } finally {
            s.close();
        }
        return list;
    }

    public List<District> getDistricByProvince(Province province) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<District> list = new ArrayList<>();
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<District> query = builder.createQuery(District.class);
            Root<District> root = query.from(District.class);
            query.select(root).where(builder.equal(root.get("province"), province));
            Query<District> q = s.createQuery(query);
            list = q.getResultList();

            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
        return list;
    }

    public List<Ward> getWardByDistrict(District district) {
        Session s = HibernateUtils.getSessionFactory().openSession();
        List<Ward> list = new ArrayList<>();
        try {
            s.beginTransaction();
            CriteriaBuilder builder = s.getCriteriaBuilder();
            CriteriaQuery<Ward> query = builder.createQuery(Ward.class);
            Root<Ward> root = query.from(Ward.class);
            query.select(root).where(builder.equal(root.get("district"), district));
            Query<Ward> q = s.createQuery(query);
            list = q.getResultList();
            s.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            s.getTransaction().rollback();
        }
        return list;
    }
}
