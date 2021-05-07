package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.PurchaseDao;
import com.rafsan.inventory.entity.Purchase;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Query;
import org.hibernate.Session;

public class PurchaseModel implements PurchaseDao {

    private static Session session;
    
    @Override
    public ObservableList<Purchase> getPurchases() {
        
        ObservableList<Purchase> list = FXCollections.observableArrayList();

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Purchase> products = session.createQuery("from Purchase").list();
        session.beginTransaction().commit();
        products.stream().forEach(list::add);

        return list;
    }
       
    // @Override
    /* CONSULTA PARA OBTENER DATOS DE REPORTE POR RANGO DE FECHAS */
    public ObservableList<Purchase> getPurchaseDate(String fec1, String fec2) throws ParseException{
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateex = df1.parse(fec1);
        
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateey = df2.parse(fec2);
        
        ObservableList<Purchase> list = FXCollections.observableArrayList();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Purchase where date(datetime) between :name1 and :name2");
        query.setParameter("name1", dateex);
        query.setParameter("name2", dateey);
        System.out.println("esta es la query:: "+query);
        List<Purchase> products= query.list();
        session.beginTransaction().commit();
        products.stream().forEach(list::add);

        return list;
    }// getPurchaseDate

    @Override
    public Purchase getPurchase(long id) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Purchase purchase = session.get(Purchase.class, id);
        session.getTransaction().commit();

        return purchase;
    }

    @Override
    public void savePurchase(Purchase purchase) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(purchase);
        session.getTransaction().commit();
    }

    @Override
    public void updatePurchase(Purchase purchase) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Purchase p = session.get(Purchase.class, purchase.getId());
        p.setProduct(purchase.getProduct());
        p.setSupplier(purchase.getSupplier());
        p.setQuantity(purchase.getQuantity());
        p.setDate(purchase.getDate());
        session.getTransaction().commit();
    }

    @Override
    public void deletePurchase(Purchase purchase) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Purchase p = session.get(Purchase.class, purchase.getId());
        session.delete(p);
        session.getTransaction().commit();
    }
    
}
