package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.SaleDao;
import com.rafsan.inventory.entity.Sale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class SalesModel implements SaleDao {

    private static Session session;

    @Override
    public ObservableList<Sale> getSales() {

        ObservableList<Sale> list = FXCollections.observableArrayList();

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Sale> products = session.createQuery("from Sale").list();
        session.beginTransaction().commit();
        products.stream().forEach(list::add);

        return list;
    }

    // @Override
    /* CONSULTA PARA OBTENER DATOS DE REPORTE POR RANGO DE FECHAS */
    public ObservableList<Sale> getSaleDate(String fec1, String fec2) throws ParseException{
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateex = df1.parse(fec1);
        
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateey = df2.parse(fec2);
        
        ObservableList<Sale> list = FXCollections.observableArrayList();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Sale where date(datetime) between :name1 and :name2");
        query.setParameter("name1", dateex);
        query.setParameter("name2", dateey);
        System.out.println("esta es la query:: "+query);
        List<Sale> products= query.list();
        session.beginTransaction().commit();
        products.stream().forEach(list::add);

        return list;
    }// getSaleDate
    
    @Override
    public ObservableList<Sale> getSaleByProductId(long id) {

        ObservableList<Sale> list = FXCollections.observableArrayList();

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        List<Sale> products = (List<Sale>) session.createCriteria(Sale.class)
                .add(Restrictions.eq("product.id", id)).list();

        session.beginTransaction().commit();
        products.stream().forEach(list::add);

        return list;
    }

    @Override
    public Sale getSale(long id) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Sale sale = session.get(Sale.class, id);
        session.getTransaction().commit();

        return sale;
    }

    @Override
    public void saveSale(Sale sale) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(sale);
        session.getTransaction().commit();
    }

    @Override
    public void updateSale(Sale sale) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Sale s = session.get(Sale.class, sale.getId());
        s.setProduct(sale.getProduct());
        s.setQuantity(sale.getQuantity());
        s.setPrice(sale.getPrice());
        s.setTotal(sale.getTotal());
        s.setDate(sale.getDate());
        session.getTransaction().commit();
    }

    @Override
    public void deleteSale(Sale sale) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Sale s = session.get(Sale.class, sale.getId());
        session.delete(s);
        session.getTransaction().commit();
    }

}// salesModel
