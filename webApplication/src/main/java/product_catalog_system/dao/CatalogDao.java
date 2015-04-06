package product_catalog_system.dao;

import product_catalog_system.domain.Catalog;

public interface CatalogDao {

	public void createCatalog(Catalog catalogo);
	
	public Catalog getCatalog(String id);
	
	public String maxCatalog();
	
}
