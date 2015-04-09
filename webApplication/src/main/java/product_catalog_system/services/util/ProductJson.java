package product_catalog_system.services.util;

import java.text.DateFormat;
import java.util.List;

import product_catalog_system.domain.ProductDto;

public class ProductJson {

	public ProductJson() {
		
	}
	
	/**
	 * El metodo convierte una entidad Producto a un String Json y agrega los attachments asociados
	 * @param producto objeto con datos de producto encapsulado
	 * @param listaAttachments Lista de documentos asociados a el producto
	 * @return String Json listo para retornar al cliente
	 */
	public String convertirProductoToJson(ProductDto producto, List<String> listaAttachments){
		String attachments = "[";
		boolean primerDato = true;
		
		if(listaAttachments != null){
			for(String attachmentAdjuntar : listaAttachments){
				if(primerDato){
					attachments = attachments+"\""+attachmentAdjuntar+"\"";
					primerDato = false;
				}else{
					attachments = attachments+", \""+attachmentAdjuntar+"\"";
				}
			}
			attachments = attachments+"]";
		}else{
			attachments = "\"\"";
		}
		
		if(attachments.equals("[]"))
			attachments = "\"\"";
		
		DateFormat formato = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
		String json = "{\"id\": \""+producto.getId()+"\", \"catalog_id\": \""+producto.getCatalog().getId()+
					"\", \"name\": \""+producto.getName()+"\", \"description\": \""+producto.getDescription()+
					"\", \"price\": \""+producto.getPrice()+"\", \"uom\": \""+producto.getUom()+
					"\", \"created_at\": \""+formato.format(producto.getCreatedAt())+"\", \"attachments\": "+attachments+"}";
		return json;
	}
	
}
