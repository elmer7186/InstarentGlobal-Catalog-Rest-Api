package product_catalog_system.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import product_catalog_system.dao.AttachmentsDao;
import product_catalog_system.dao.CatalogDao;
import product_catalog_system.dao.ProductDao;
import product_catalog_system.domain.AttachmentsDto;
import product_catalog_system.domain.CatalogDto;
import product_catalog_system.domain.ProductDto;
import product_catalog_system.services.util.CatalogJson;
import product_catalog_system.services.util.ProductJson;
import product_catalog_system.services.util.exception.DAOException;

/**
 * Clase de servicios web para la aplicación
 * @author Elmer
 *
 */
@Path("application")
@Component
public class ProductService {
	
	/**
	 * Variable Logger de incidencias
	 */
	private static Logger log=Logger.getLogger(ProductService.class);
	
	/**
	 * Variable inyectadas en la clase con Spring
	 */
	@Autowired
	private ProductDao productDao;
	@Autowired
	private CatalogDao catalogDao;
	@Autowired
	private AttachmentsDao attachmentsDao;
	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setCatalogDao(CatalogDao catalogDao) {
		this.catalogDao = catalogDao;
	}

	public void setAttachmentsDao(AttachmentsDao attachmentsDao) {
		this.attachmentsDao = attachmentsDao;
	}

	/**
	 * Servicio Get que retorna el producto con id Catalogo y id Producto asociado
	 * @param id identificación de Catalogo a asociar con el Producto
	 * @param idProduct identificacion de Producto a encontrar
	 * @return lista Json de producto encontrado con Catalogo y id del Producto que coincide con el pedido
	 */
	@Produces("application/json")
	@GET
	@Path("catalog/{id}/products/{product_id}")
	public Response obtenerProductos(@PathParam("id")String id, 
			@PathParam("product_id")String idProduct){
		List<ProductDto> productosEncontrados = null;
		CatalogDto catalogo = null;
		try {
			catalogo = catalogDao.getCatalog(id);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.NOT_FOUND).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.NOT_FOUND).build();
		}
		if(catalogo != null){
			try {
				productosEncontrados = productDao.getProductxCatalog(catalogo, idProduct);
			} catch(DAOException expDao){
				log.error(expDao.getMsjTecnico());
				System.out.println(expDao.getOrigen());
				return Response.status(Status.NOT_FOUND).build();
			} catch(Exception e){
				log.error(e);
				System.out.println(e);
				return Response.status(Status.NOT_FOUND).build();
			}
		}else{
			return Response.status(Status.NOT_FOUND).build();
		}
		
		/**
		 * Verifica que si hayan productos encontrados
		 */
		String productoResponse = "";
		if(productosEncontrados != null){
			ProductDto productoEncontrado = null;
			if(productosEncontrados.size()>0){
				productoEncontrado = productosEncontrados.get(0);
			}else{
				return Response.status(Status.NOT_FOUND).build();
			}
			ProductJson productoJson = new ProductJson();
			List<String> listaAttachments = null;
			try{
				listaAttachments = obtenerAttachmets(productoEncontrado);
			} catch(Exception e){
				log.error(e);
				System.out.println(e);
				return Response.status(Status.NOT_FOUND).build();
			}
			productoResponse = productoJson.convertirProductoToJson(productoEncontrado, listaAttachments);
			System.out.println(productoResponse);
		}else{
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Response.Status.OK).entity(productoResponse).build();
	}
	
	@Produces("application/json")
	@POST
	@Path("/catalog")
	public Response crearCatalogo(JSONObject objeto){
		/**
		 * estracción del datos desde el archivo json recibido
		 */
		String nombre = "";
		String descripcion = "";
		try {
			if(objeto.has("name"))
			nombre = objeto.getString("name");
			if(objeto.has("description"))
			descripcion = objeto.getString("description");
		} catch (JSONException e) {
			System.out.println("error al intentar extraer datos del archivo json de catalogo");
			log.error(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		String nuevoId = generarIdCatalogo();
		
		CatalogDto catalogoCrear = new CatalogDto(nuevoId, nombre, descripcion);
		try {
			catalogDao.createCatalog(catalogoCrear);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.BAD_REQUEST).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/**
		 * Empaquetamos Catalogo creado en un documento xml para responder al servicio
		 */
		CatalogJson catalogoJson = new CatalogJson();
		String catalogoResponse = catalogoJson.convertirCatalogoToJson(catalogoCrear);
		
		return Response.status(Response.Status.CREATED).entity(catalogoResponse).build();
	}
	
	/**
	 * El metodo guarda un nuevo producto
	 * @param objeto documento en formato Json con contenido del  request
	 * @param idCatalog Variable que llega por post para identificar el catalogo a elegir
	 * @return Response con estado de guardado y Producto guardao
	 */
	@Produces("application/json")
	@PUT
	@Path("/catalog/{id_Catalog}/products")
	public Response crearProducto(JSONObject objeto, @PathParam("id_Catalog")String idCatalog){
		/**
		 * estracción del datos desde el archivo json recibido
		 */
		String nombre = "";
		String descripcion = "";
		int precio = 0;
		String uom = "";
		String attachments = "";
		try {
			if(objeto.has("name"))
				nombre = objeto.getString("name");
			if(objeto.has("description"))
				descripcion = objeto.getString("description");
			if(objeto.has("price"))
				precio = Integer.parseInt(objeto.getString("price"));
			if(objeto.has("uom"))
				uom = objeto.getString("uom");
			if(objeto.has("attachments"))
				attachments = objeto.getString("attachments");
		} catch (JSONException e) {
			System.out.println("error al intentar extraer datos del archivo json de catalogo: "+e);
			log.error(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/**
		 * verificamos variable uom
		 */
		if(!verificarUom(uom)){
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/**
		 * Verificacion si el catalogo existe
		 */
		CatalogDto catalogo = null;
		try {
			catalogo = catalogDao.getCatalog(idCatalog);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.BAD_REQUEST).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		if(catalogo == null){
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/**
		 * encapsulacion de producto y listo para guardar
		 */
		String nuevoId = generarIdProducto();
		
		ProductDto productoGuardar = new ProductDto(nuevoId, catalogo, nombre, descripcion, precio, uom, new Date());
		
		try {
			productDao.createProduct(productoGuardar);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.BAD_REQUEST).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		List<String> listaAttachmentsGuardar = null;
		if(attachments != ""){
			listaAttachmentsGuardar = estraerAttachements(attachments);
		}
		
		/**
		 * se procede a guardar attachments del producto si existen
		 */
		if(listaAttachmentsGuardar != null){
			for(String attachmentGuardar: listaAttachmentsGuardar ){
				AttachmentsDto objetoAttachment = new AttachmentsDto(productoGuardar, attachmentGuardar);
				
				try {
					attachmentsDao.createAttachment(objetoAttachment);
				} catch(DAOException expDao){
					log.error(expDao.getMsjTecnico());
					System.out.println(expDao.getOrigen());
					return Response.status(Status.BAD_REQUEST).build();
				} catch(Exception e){
					log.error(e);
					System.out.println(e);
					return Response.status(Status.BAD_REQUEST).build();
				}
			}
		}
		
		/**
		 * Solicita el metodo que convertira a un String Json el producto y los attachments guardados
		 */
		ProductJson productoJson = new ProductJson();
		String productoConvertidoJson = productoJson.convertirProductoToJson(productoGuardar, listaAttachmentsGuardar);
		
		return Response.status(Response.Status.CREATED).entity(productoConvertidoJson).build();
	}
	
	/**
	 * El metodo actualiza producto ante llamado por URL en metodo PUT
	 * @param objeto Objeto Json con toda la información acerce de producto
	 * @param idCatalog identificación de catalogo
	 * @param id identificación de producto a actualizar
	 * @return Response con documento Json Product con los nuevos datos
	 */
	@Produces("application/json")
	@PUT
	@Path("/catalog/{id_Catalog}/products/{id_Product}")
	public Response actualizarProducto(JSONObject objeto, @PathParam("id_Catalog")String idCatalog, @PathParam("id_Product")String id){
		/**
		 * estracción del datos desde el archivo json recibido
		 */
		String nombre = "";
		String descripcion = "";
		int precio = 0;
		String uom = "";
		String attachments = "";
		try {
			if(objeto.has("name"))
				nombre = objeto.getString("name");
			if(objeto.has("description"))
				descripcion = objeto.getString("description");
			if(objeto.has("price"))
				precio = Integer.parseInt(objeto.getString("price"));
			if(objeto.has("uom"))
				uom = objeto.getString("uom");
			if(objeto.has("attachments"))
				attachments = objeto.getString("attachments");
		} catch (JSONException e) {
			System.out.println("error al intentar extraer datos del archivo json de catalogo: "+e);
			log.error(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/**
		 * verificamos variable uom
		 */
		if(!verificarUom(uom)){
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/**
		 * Consultamos id de catalogo ingresado y verificamos si el catalogo existe
		 */
		CatalogDto catalogoConsultar = null;
		if(idCatalog != null && (!"".equals(idCatalog))){
			try {
				catalogoConsultar = catalogDao.getCatalog(idCatalog);
			} catch(DAOException expDao){
				log.error(expDao.getMsjTecnico());
				System.out.println(expDao.getOrigen());
				return Response.status(Status.BAD_REQUEST).build();
			} catch(Exception e){
				log.error(e);
				System.out.println(e);
				return Response.status(Status.BAD_REQUEST).build();
			}
		}else{
			return Response.status(Status.BAD_REQUEST).build();
		}
		if(catalogoConsultar == null){
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/**
		 * Creamos el objeto Producto 
		 */
		ProductDto productoGuardar = null;
		productoGuardar = new ProductDto(id, catalogoConsultar, nombre, descripcion, precio, uom, new Date());
		try {
			productDao.updateProduct(productoGuardar);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.BAD_REQUEST).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/**
		 * se borra todos los atachments y crean los actualizados
		 */
		try {
			attachmentsDao.deleteAttachments(productoGuardar);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.BAD_REQUEST).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		List<String> listaAttachmentsGuardar = null;
		if(attachments != ""){
			listaAttachmentsGuardar = estraerAttachements(attachments);
		}
		
		/**
		 * se procede a guardar attachments del producto si existen
		 */
		if(listaAttachmentsGuardar != null){
			for(String attachmentGuardar: listaAttachmentsGuardar ){
				AttachmentsDto objetoAttachment = new AttachmentsDto(productoGuardar, attachmentGuardar);
				
				try {
					attachmentsDao.createAttachment(objetoAttachment);
				} catch(DAOException expDao){
					log.error(expDao.getMsjTecnico());
					System.out.println(expDao.getOrigen());
					return Response.status(Status.BAD_REQUEST).build();
				} catch(Exception e){
					log.error(e);
					System.out.println(e);
					return Response.status(Status.BAD_REQUEST).build();
				}
			}
		}
		
		/**
		 * Solicita el metodo que convertira a un String Json el producto y los attachments guardados
		 */
		ProductJson productoJson = new ProductJson();
		String productoConvertidoJson = productoJson.convertirProductoToJson(productoGuardar, listaAttachmentsGuardar);
		
		return Response.status(Response.Status.OK).entity(productoConvertidoJson).build();
	}
	
	@DELETE
	@Path("/catalog/{id_Catalog}/products/{id_Product}")
	public Response borrarProducto(@PathParam("id_Catalog")String idCatalog, @PathParam("id_Product")String id){
		/**
		 * Consultamos id de catalogo ingresado y verificamos si el catalogo existe
		 */
		CatalogDto catalogoConsultar = null;
		if(idCatalog != null && (!"".equals(idCatalog))){
			try {
				catalogoConsultar = catalogDao.getCatalog(idCatalog);
			} catch(DAOException expDao){
				log.error(expDao.getMsjTecnico());
				System.out.println(expDao.getOrigen());
				return Response.status(Status.NOT_FOUND).build();
			} catch(Exception e){
				log.error(e);
				System.out.println(e);
				return Response.status(Status.NOT_FOUND).build();
			}
		}else{
			return Response.status(Status.NOT_FOUND).build();
		}
		if(catalogoConsultar == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		
		/**
		 * Consultamos el Producto con dicho catalogo 
		 */
		List<ProductDto> listaProductoBorrar = null;
		try {
			listaProductoBorrar = productDao.getProductxCatalog(catalogoConsultar, id);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.NOT_FOUND).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.NOT_FOUND).build();
		}
		ProductDto productoBorrar = listaProductoBorrar.get(0);
		
		/**
		 * se borra todos los atachments y crean los actualizados
		 */
		try {
			attachmentsDao.deleteAttachments(productoBorrar);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.NOT_FOUND).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.NOT_FOUND).build();
		}
		/**
		 * se borra el producto
		 */
		try {
			productDao.delete(productoBorrar);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.NOT_FOUND).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.NOT_FOUND).build();
		}
		
		return Response.status(Status.NO_CONTENT).build();
	}
	
	@Produces("application/json")
	@GET
	@Path("/catalog/{id_Catalog}/products")
	public Response listarProductos(@PathParam("id_Catalog")String idCatalog, @QueryParam("name")String name,
			@QueryParam("skip")Integer skip, @QueryParam("limit")Integer limit){
		/**
		 * analizamos datos entrantes y los organizamos
		 */
		if(name == null)
			name = "%";
		else
			name = "%"+name+"%";
		
		if(skip == null)
			skip = 0;
		
		if(limit == null)
			limit = 10;
		
		if(skip > limit){
			return Response.status(Status.NOT_FOUND).build();
		}
		/**
		 * Obtenemos catalogo de id entrante por path
		 */
		CatalogDto catalogoConsultar = null;
		if(idCatalog != null && (!"".equals(idCatalog))){
			try {
				catalogoConsultar = catalogDao.getCatalog(idCatalog);
			} catch(DAOException expDao){
				log.error(expDao.getMsjTecnico());
				System.out.println(expDao.getOrigen());
				return Response.status(Status.NOT_FOUND).build();
			} catch(Exception e){
				log.error(e);
				System.out.println(e);
				return Response.status(Status.NOT_FOUND).build();
			}
		}else{
			return Response.status(Status.NOT_FOUND).build();
		}
		if(catalogoConsultar == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		/**
		 * Obtenemos productos por catalogo y con String buscar
		 */
		List<ProductDto> listaProductos = null;
		try {
			listaProductos = productDao.listProductsFiltered(catalogoConsultar, name);
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return Response.status(Status.NOT_FOUND).build();
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return Response.status(Status.NOT_FOUND).build();
		}
		/**
		 * Empaquetamos productos dentro del rango
		 */
		int contador = 0;
		String productoResponse = "[";
		for(ProductDto producto: listaProductos){
			if(contador>skip && (contador<=limit)){
				productoResponse = productoResponse + ",";
			}
			if(contador>=skip && (contador<=limit)){
				ProductJson productoJson = new ProductJson();
				List<String> listaAttachments = null;
				try{
					listaAttachments = obtenerAttachmets(producto);
				}catch(Exception e){
					log.error(e);
					System.out.println(e);
					return Response.status(Status.NOT_FOUND).build();
				}
				productoResponse = productoResponse + productoJson.convertirProductoToJson(producto, listaAttachments);
			}
			contador++;
		}
		productoResponse = productoResponse + "]";
		if(productoResponse.equals("[]"))
			return Response.status(Status.NOT_FOUND).build();
		
		return Response.status(Response.Status.OK).entity(productoResponse).build();
	}
	
	/**
	 * ---------------------------------------------------------------------
	 * Metodos de apoto para los servicios
	 * ---------------------------------------------------------------------
	 */
	
	public List<String> obtenerAttachmets(ProductDto producto) throws Exception{
		
		List<AttachmentsDto> attachments = null;
		if(producto != null){
			try {
				attachments = attachmentsDao.listAttachmentsByProduct(producto);
			} catch (Exception e) {
				System.out.println("Error al intentar obtener Attachments x Product: "+e);
				log.error(e);
				return null;
			}
		}else{
			return null;
		}
		
		List<String> listaAttachments = new ArrayList<String>();
		if(attachments != null){
			for(AttachmentsDto attachmentEncontrado: attachments){
				listaAttachments.add(attachmentEncontrado.getAttachment());
			}
		}else{
			return null;
		}
		
		return listaAttachments;
	}
	
	public String generarIdCatalogo(){
		int numeroCatalogos = 0;
		try {
			numeroCatalogos = catalogDao.countCatalog();
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return null;
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return null;
		}
		
		/**
		 * Creando nuevo Id para asignar al catalogo
		 */
		boolean idOcupado = true;
		String nuevoId = "";
		while(idOcupado){
			numeroCatalogos = numeroCatalogos+1;
			nuevoId = "CAT"+numeroCatalogos;
			System.out.println(nuevoId);
			CatalogDto catalogo = null;
			try {
				catalogo = catalogDao.getCatalog(nuevoId);
			} catch(DAOException expDao){
				log.error(expDao.getMsjTecnico());
				System.out.println(expDao.getOrigen());
				return null;
			} catch(Exception e){
				log.error(e);
				System.out.println(e);
				return null;
			}
			
			if(catalogo != null){
				idOcupado = true;
			}else{
				idOcupado = false;
			}
		}
		return nuevoId;
	}
	
	public String generarIdProducto(){
		int numeroProductos = 0;
		try {
			numeroProductos = productDao.countProducto();
		} catch(DAOException expDao){
			log.error(expDao.getMsjTecnico());
			System.out.println(expDao.getOrigen());
			return null;
		} catch(Exception e){
			log.error(e);
			System.out.println(e);
			return null;
		}
		
		/**
		 * Creando nuevo Id para asignar al producto
		 */
		boolean idOcupado = true;
		String nuevoId = "";
		while(idOcupado){
			numeroProductos = numeroProductos+1;
			nuevoId = "PROD"+numeroProductos;
			ProductDto producto = null;
			try {
				producto = productDao.getProduct(nuevoId);
			} catch(DAOException expDao){
				log.error(expDao.getMsjTecnico());
				System.out.println(expDao.getOrigen());
				return null;
			} catch(Exception e){
				log.error(e);
				System.out.println(e);
				return null;
			}
			
			if(producto != null){
				idOcupado = true;
			}else{
				idOcupado = false;
			}
		}
		return nuevoId;
	}
	
	public List<String> estraerAttachements(String attachments){
		/**
		 * quita corchetes si tiene
		 */
		if(attachments.charAt(0) == '['){
			attachments = attachments.substring(1, attachments.length()-2); 
		}
		
		/**
		 * separo elementos por comillas
		 */
		String[] attachmentsDividido = attachments.split("\"");
		
		/**
		 * excluyo elementos que no son atributos
		 */
		List <String> listaAttachments = new ArrayList<String>();
		for (int i=0; i<attachmentsDividido.length; i++) {
			if(!",".equals(attachmentsDividido[i]) && (!"".equals(attachmentsDividido[i]))){
				listaAttachments.add(attachmentsDividido[i]);
			}
		}
		return listaAttachments;
	}
	
	/**
	 * Verifica si es un parametro valido de producto
	 * @param uom parametro a evaluar
	 * @return true si es valido, false si no es valido
	 */
	public Boolean verificarUom(String uom){
		if(!"EA".equals(uom) && (!"BX".equals(uom)) && (!"SC".equals(uom)) && (!"PL".equals(uom))
				&& (!"pcs".equals(uom))&& (!"pkg".equals(uom))&& (!"kg".equals(uom))&& (!"g".equals(uom)))
			return false;
		
		return true;
	}
	
}
