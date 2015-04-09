package product_catalog_system.dao;

import product_catalog_system.domain.CatalogDto;
import product_catalog_system.services.util.exception.DAOException;

public interface CatalogDao {

	public void createCatalog(CatalogDto catalogo) throws DAOException;
	
	public CatalogDto getCatalog(String id) throws DAOException;
	
	public int countCatalog() throws DAOException;
	
}
