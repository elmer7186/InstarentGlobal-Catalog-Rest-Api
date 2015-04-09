package product_catalog_system.services.util;

import product_catalog_system.domain.CatalogDto;

public class CatalogJson {
	
	public CatalogJson(){
		
	}
	
	public String convertirCatalogoToJson(CatalogDto catalogo){
		 String json = "{\"id\": \""+catalogo.getId()+"\", \"name\": \""+catalogo.getName()+
				 		"\", \"description\": \""+catalogo.getDescription()+"\"}";
		 return json;
	}
	
}
