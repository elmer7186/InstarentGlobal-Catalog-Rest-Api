package product_catalog_system.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import product_catalog_system.domain.Catalog;
import product_catalog_system.domain.Product;

public class ProductDaoImpl extends HibernateDaoSupport implements ProductDao {
	
	private static Logger log=Logger.getLogger(CatalogDaoImpl.class);

	public void createUpdateProduct(Product producto) {

		Session session = null;
		
		try{
			session = getSession();
			session.saveOrUpdate(producto);
			session.flush();
		}catch(Exception e){
			System.out.println("--error en el Dao crear o actualizar Producto-- "+e);
		}finally{
			session.close();
		}

	}

	public Product getProduct(String id) {
		Session session = null;
		Product producto = null;
		
		try{
			session = getSession();
			producto = (Product)session.get(Product.class, id);			
		} catch (Exception e) {
			System.out.println("--error al intentar obtener Producto-- "+e);
			log.error(e);
		} finally{
			session.close();
		}
		return producto;
	}

	public void delete(Product producto) {
		Session session = null;
		Transaction tx = null;
		
		try{
			session = getSession();
			tx = session.beginTransaction();
			session.delete(producto);
			tx.commit();
			
		} catch (Exception e) {
			tx.rollback();
			System.out.println("--Error en el Dao eliminar producto-- "+e);
			log.error(e);
		} finally{
			session.close();
		}
	}

	public List<Product> getProductxCatalog(Catalog catalogo, String id) {

		Session session = null;
        List<Product> listaProductos = new ArrayList<Product>();
       
        try{
        	
        	session = getSession();
        	
        	Query query = session.createQuery("from Product where id = :id and catalog = :catalogo ");
        	query.setEntity("catalogo", catalogo);
        	
        	query.setString("id", id);
        	
        	listaProductos = query.list();
        } catch (Exception e) {
			System.out.println("--Error en el Dao obtener Productos x Catalogo-- "+e);
			log.error(e);
		}

        return listaProductos;
		
	}
	
	public List<Product> listProducts(){
		Session session = null;
		List<Product> listaProductos = new ArrayList<Product>();
		
		try{
			session = getSession();
			Criteria criteria = session.createCriteria(Product.class);
			listaProductos = criteria.list();			
		} catch (Exception e) {
			System.out.println("--Error en el Dao listar Productos-- "+e);
			log.error(e);
		}
		return listaProductos;
	}

}
