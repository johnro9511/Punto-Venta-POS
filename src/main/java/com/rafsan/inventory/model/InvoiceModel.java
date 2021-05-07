package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.InvoiceDao;
import com.rafsan.inventory.entity.Invoice;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Query;
import org.hibernate.Session;

public class InvoiceModel implements InvoiceDao {

    private static Session session;

    @Override
    public ObservableList<Invoice> getInvoices() {

        ObservableList<Invoice> list = FXCollections.observableArrayList();

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Invoice> products = session.createQuery("from Invoice").list();
        session.beginTransaction().commit();
        products.stream().forEach(list::add);

        return list;
    }
    
    // @Override
    /* CONSULTA PARA OBTENER DATOS DE REPORTE POR RANGO DE FECHAS */
    public ObservableList<Invoice> getInvoicesDate(String fec1, String fec2) throws ParseException{
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateex = df1.parse(fec1);
        
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date dateey = df2.parse(fec2);
        
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Invoice where date(datetime) between :name1 and :name2");
        query.setParameter("name1", dateex);
        query.setParameter("name2", dateey);
        System.out.println("esta es la query:: "+query);
        List<Invoice> products= query.list();
        session.beginTransaction().commit();
        products.stream().forEach(list::add);

        return list;
    }// getSaleDate

    @Override
    public Invoice getInvoice(String id) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Invoice invoice = session.get(Invoice.class, id);
        session.getTransaction().commit();

        return invoice;
    }

    @Override
    public void saveInvoice(Invoice invoice) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(invoice);
        session.getTransaction().commit();
    }

    @Override
    public void deleteCategory(Invoice invoice) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Invoice i = session.get(Invoice.class, invoice.getId());
        session.delete(i);
        session.getTransaction().commit();
    }

}
