package product_catalog_system.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import product_catalog_system.domain.CatalogDto;
import product_catalog_system.domain.ProductDto;
import product_catalog_system.services.util.exception.DAOException;

/**
 * Clase dao que gestionaran las consultas a la base de datos relacionadas con la tabla Catalog
 * @author Elmer
 *
 */
public class CatalogDaoImpl extends HibernateDaoSupport implements CatalogDao {
	
	/**
	 * Metodo que pide a Hibernate la creación de un Catalogo con la
	 * información que ingresa como parametro
	 */
	public void createCatalog(CatalogDto catalogo) throws DAOException{
		Session session = null;
		
		try{
			session = getSession();
			session.save(catalogo);
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
	 * Pide un catalogo especifico con id ingresado como parametro
	 */
	public CatalogDto getCatalog(String id) throws DAOException{
		Session session = null;
		CatalogDto catalogo = null;
		
		try{
			session = getSession();
			catalogo = (CatalogDto)session.get(CatalogDto.class, id);			
		} catch (Exception e) {
			DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}
		return catalogo;
	}
	
	/**
	 * Retorna el numero de items en Catalogo
	 */
	public int countCatalog() throws DAOException{

		Session session = null;
        int registro = 0;
       
        try{
        	
        	session = getSession();
        	
        	Query query = session.createQuery("select count(id) from CatalogDto");
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
