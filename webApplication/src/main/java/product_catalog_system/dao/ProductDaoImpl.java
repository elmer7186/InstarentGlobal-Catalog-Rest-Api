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

import product_catalog_system.domain.CatalogDto;
import product_catalog_system.domain.ProductDto;
import product_catalog_system.services.util.exception.DAOException;

/**
 * Clase dao que gestionaran las consultas a la base de datos relacionadas con la tabla Product
 * @author Elmer
 *
 */
public class ProductDaoImpl extends HibernateDaoSupport implements ProductDao {

	/**
	 * Crea nuevo Producto ingresado por parametro
	 * @param producto objeto que contiene encapsulado información de un producto
	 */
	public void createProduct(ProductDto producto) throws DAOException{

		Session session = null;
		
		try{
			session = getSession();
			session.save(producto);
			session.flush();
		}catch(Exception e){
			DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		}finally{
			session.close();
		}

	}
	
	/**
	 * Actualiza Producto ingresado por parametro
	 * @param producto objeto que contiene encapsulado información de un producto
	 */
	public void updateProduct(ProductDto producto) throws DAOException{

		Session session = null;
		
		try{
			session = getSession();
			session.update(producto);
			session.flush();
		}catch(Exception e){
			DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		}finally{
			session.close();
		}

	}

	/**
	 * Retorna el producto de base de datos con el id ingresado por parametro
	 */
	public ProductDto getProduct(String id) throws DAOException{
		Session session = null;
		ProductDto producto = null;
		
		try{
			session = getSession();
			producto = (ProductDto)session.get(ProductDto.class, id);			
		} catch (Exception e) {
			DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}
		return producto;
	}

	/**
	 * elimina el Producto en base de datos y que ingresa como parametro
	 */
	public void delete(ProductDto producto) throws DAOException{
		Session session = null;
		Transaction tx = null;
		
		try{
			session = getSession();
			tx = session.beginTransaction();
			session.delete(producto);
			tx.commit();
			
		} catch (Exception e) {
			tx.rollback();
			DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}
	}

	/**
	 * retorna de la base de datos el Producto que pertenece al catalogo y
	 * al id del producto ingresados por parametro 
	 */
	public List<ProductDto> getProductxCatalog(CatalogDto catalogo, String id) throws DAOException{

		Session session = null;
        List<ProductDto> listaProductos = new ArrayList<ProductDto>();
       
        try{
        	
        	session = getSession();
        	
        	Query query = session.createQuery("from ProductDto where id = :id and catalog = :catalogo ");
        	query.setEntity("catalogo", catalogo);
        	
        	query.setString("id", id);
        	
        	listaProductos = query.list();
        } catch (Exception e) {
        	DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}

        return listaProductos;
		
	}
	
	/**
	 * Retorna de la base de datos todos los Productos encontrados
	 */
	public List<ProductDto> listProducts() throws DAOException{
		Session session = null;
		List<ProductDto> listaProductos = new ArrayList<ProductDto>();
		
		try{
			session = getSession();
			Criteria criteria = session.createCriteria(ProductDto.class);
			listaProductos = criteria.list();			
		} catch (Exception e) {
			DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}
		return listaProductos;
	}
	
	/**
	 * Retorna el numero de items de Producto
	 */
	public int countCatalog() throws DAOException{

		Session session = null;
        int registro = 0;
       
        try{
        	
        	session = getSession();
        	
        	Query query = session.createQuery("select count(id) from ProductDao");
        	registro = Integer.parseInt(query.list().get(0).toString());
        	
        } catch (Exception e) {
        	DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}

        return registro;
		
	}
	
	/**
	 * Retorna los elementos encontrados con referencia de nombre
	 */
	public List<ProductDto> listProductsFiltered(CatalogDto catalogo, String busqueda) throws DAOException{
		
		Session session = null;
        List<ProductDto> productos = new ArrayList<ProductDto>();
        
        try{               
            session = getSession();
            
            Query query = session.createQuery("from ProductDto where catalog = :catalogo and name like :nombre");                            
            query.setString("nombre", busqueda);
            query.setEntity("catalogo", catalogo);
            productos = query.list();
            
        } catch (Exception e) {
        	DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}
        return productos;
	}

	/**
	 * Retorna el numero de items en Producto
	 */
	public int countProducto() throws DAOException{

		Session session = null;
        int registro = 0;
       
        try{
        	
        	session = getSession();
        	
        	Query query = session.createQuery("select count(id) from ProductDto");
        	registro = Integer.parseInt(query.list().get(0).toString());
        	
        } catch (Exception e) {
        	DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}

        return registro;
		
	}
}
