package product_catalog_system.services.dominio;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductDtoService {
	private String id;
	private String catalogName;
	private String name;
	private String description;
	private Integer price;
	private String unidades;
	private String dateCreated;
	
	public ProductDtoService(String id, String catalogName, String name,
			String description, int price, String unidades, String dateCreated) {
		super();
		this.id = id;
		this.catalogName = catalogName;
		this.name = name;
		this.description = description;
		this.price = price;
		this.unidades = unidades;
		this.dateCreated = dateCreated;
	}
	
	public ProductDtoService(){
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getUnidades() {
		return unidades;
	}

	public void setUnidades(String unidades) {
		this.unidades = unidades;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	
}
