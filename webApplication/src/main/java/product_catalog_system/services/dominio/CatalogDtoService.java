package product_catalog_system.services.dominio;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CatalogDtoService {

	private String id;
	private String name;
	private String description;
	
	public CatalogDtoService(String id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
