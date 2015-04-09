package product_catalog_system.dao;

import java.util.List;

import product_catalog_system.domain.CatalogDto;
import product_catalog_system.domain.ProductDto;
import product_catalog_system.services.util.exception.DAOException;

public interface ProductDao {
	
	public void createProduct(ProductDto producto) throws DAOException;
	
	public void updateProduct(ProductDto producto) throws DAOException;
	
	public ProductDto getProduct(String id) throws DAOException;
	
	public void delete(ProductDto producto) throws DAOException;
	
	public List<ProductDto> getProductxCatalog(CatalogDto catalogo, String idProduct) throws DAOException;
	
	public List<ProductDto> listProducts() throws DAOException;
	
	public List<ProductDto> listProductsFiltered(CatalogDto catalogo, String busqueda) throws DAOException;
	
	public int countProducto() throws DAOException;
	
}
