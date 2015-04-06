package product_catalog_system.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import product_catalog_system.dao.CatalogDao;
import product_catalog_system.dao.ProductDao;
import product_catalog_system.domain.Catalog;
import product_catalog_system.domain.Product;
import product_catalog_system.services.dominio.CatalogDtoService;
import product_catalog_system.services.dominio.ProductDtoService;

@Path("application")
@Component
public class ProductService {
	
	private static Logger log=Logger.getLogger(ProductService.class);
	
	@Autowired
	private ProductDao productDao;
	@Autowired
	private CatalogDao catalogDao;
	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setCatalogDao(CatalogDao catalogDao) {
		this.catalogDao = catalogDao;
	}

//	@POST
//	@Path("/catalog")
//	public void addCatalog(MultivaluedMap<String, String> parametros){
//		Catalog catalogo = new Catalog(parametros.getFirst("name"), parametros.getFirst("description"));
//		catalogDao.createCatalog(catalogo);
//	}
	
	@Produces("application/json")
	@GET
	@Path("catalog/{id}/products/{product_id}")
	public List<ProductDtoService> obtenerProductos(@PathParam("id")String id, 
			@PathParam("product_id")String idProduct){
		
		List<Product> productosEncontrados = null;
		List<ProductDtoService> listaProductos = new ArrayList<ProductDtoService>();
		
		Catalog catalogo = null;
		try {
			catalogo = catalogDao.getCatalog(id);
		} catch (Exception e) {
			System.out.println("error al invocar el metodo para obtener catalogo: "+e);
		}
		if(catalogo != null){
			try {
				productosEncontrados = productDao.getProductxCatalog(catalogo, idProduct);
			} catch (Exception e) {
				System.out.println("error al invocar el metodo para obtener Productos x catalogo: "+e);
				log.error(e);
			}
		}
		
		if(productosEncontrados != null){
			for(Product productoPasar: productosEncontrados){
				
				DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
				String fechaString = fecha.format(productoPasar.getCreatedAt());
				
				ProductDtoService producto = new ProductDtoService(productoPasar.getId(), productoPasar.getCatalog().getName(), productoPasar.getName(), productoPasar.getDescription(), productoPasar.getPrice(), productoPasar.getUom(), fechaString);
				listaProductos.add(producto);
				
			}
		}else{
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		return  listaProductos;
	}
	
	@Produces("application/json")
	@POST
	@Path("/catalog")
	@Consumes("application/json")
	public CatalogDtoService crearCatalogo(CatalogDtoService catalogo){
		String maximoId = null;
		String nuevoId = null;
		try {
			maximoId = catalogDao.maxCatalog();
		} catch (Exception e) {
			System.out.println("error al intentar consultar catalogo");
			log.error(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		/**
		 * Creando nuevo Id para asignar al catalogo
		 */
		if(maximoId != null){
			int numeroId = Integer.parseInt(maximoId.substring(3, maximoId.length()-1));
			nuevoId = "CAT"+Integer.toString(numeroId+1);
		}
		Catalog catalogoCrear = new Catalog(nuevoId, catalogo.getName(), catalogo.getDescription());
		
		try {
			catalogDao.createCatalog(catalogoCrear);
		} catch (Exception e) {
			System.out.println("Error al invocar metodo crear Catalogo");
			log.error(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		/**
		 * Empaquetamos Catalogo creado en un documento xml para responder al servicio
		 */
		CatalogDtoService catalogoResponse = new CatalogDtoService(catalogoCrear.getId(), catalogoCrear.getName(), catalogoCrear.getDescription());
		
		return catalogoResponse;
	}
	
	@Produces("application/json")
	@PUT
	@Path("/catalog/{id_Catalog}/products/{id_Product}")
	@Consumes("application/json")
	public void crearProducto(ProductDtoService producto, @PathParam("id_Catalog")String idCatalog, @PathParam("id_Product")String id){
		/**
		 * Consultamos id de catalogo ingresado
		 */
		Catalog catalogoConsultar = null;
		if(idCatalog != null && (!"".equals(idCatalog))){
			try {
				catalogoConsultar = catalogDao.getCatalog(idCatalog);
			} catch (Exception e) {
				System.out.println("error al intentar consultar un catalogo: "+e);
				log.error(e);
			}
		}else{
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		/**
		 * Verificamos si el catalogo retornado es nulo, entonces no se puede guardar con dicho catalogo
		 */
		if(catalogoConsultar == null){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		String idProduct = null;
		if(id != null){
			idProduct = id;
		}else if(!"".equals(producto.getId()) && (producto.getId() != null)){
			idProduct = producto.getId();
		}
		
		/**
		 * llamamos al metodo de verificación de id y nos retorna el nuevo id
		 */
		String nuevoIdProducto = verificarIdProducto(idProduct);
	}

	private String verificarIdProducto(String id){
		
		List<Product> listaProductos = null;
		try {
			listaProductos = productDao.listProducts();
		} catch (Exception e) {
			System.out.println("error al intentar consultar un catalogo: "+e);
			log.error(e);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		if(listaProductos != null){
			int contador = 0;
			boolean claveRepetida = true;
			while(claveRepetida){
				claveRepetida = false;
				for(Product producto: listaProductos){
					if(id == null || (producto.getId().equals(id))){
						claveRepetida = false;
						contador = contador+1;
						id = "PRO"+Integer.toString(contador);
					}
				}
			}
		}
		return id;
	}
	
}
