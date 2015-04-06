package product_catalog_system.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import product_catalog_system.domain.Catalog;
import product_catalog_system.domain.Product;

public class CatalogDaoImpl extends HibernateDaoSupport implements CatalogDao {

	private static Logger log=Logger.getLogger(CatalogDaoImpl.class);
	
	public void createCatalog(Catalog catalogo) {
		Session session = null;
		
		try{
			session = getSession();
			session.save(catalogo);
			session.flush();
		}catch(Exception e){
			System.out.println("--error en el Dao crear Catalogo-- "+e);
			log.error(e);
		}finally{
			session.close();
		}
	}

	public Catalog getCatalog(String id){
		Session session = null;
		Catalog catalogo = null;
		
		try{
			session = getSession();
			catalogo = (Catalog)session.get(Catalog.class, id);			
		} catch (Exception e) {
			System.out.println("--error al intentar obtener Catalogo-- "+e);
			log.error(e);
		}
		return catalogo;
	}
	
	public String maxCatalog() {

		Session session = null;
        String registro = null;
       
        try{
        	
        	session = getSession();
        	
        	Query query = session.createQuery("select max(id) from Catalog");
        	registro = (String)query.list().get(0);
        	
        } catch (Exception e) {
			System.out.println("--Error en el Dao contar Catalogos-- "+e);
			log.error(e);
		}

        return registro;
		
	}
}
