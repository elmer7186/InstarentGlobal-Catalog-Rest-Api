package product_catalog_system.dao;

import java.util.List;

import product_catalog_system.domain.Catalog;
import product_catalog_system.domain.Product;

public interface ProductDao {
	
	public void createUpdateProduct(Product producto);
	
	public Product getProduct(String id);
	
	public void delete(Product producto);
	
	public List<Product> getProductxCatalog(Catalog catalogo, String idProduct);
	
	public List<Product> listProducts();
	
}
