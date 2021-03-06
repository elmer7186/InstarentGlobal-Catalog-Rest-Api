package product_catalog_system.domain;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Product generated by hbm2java
 */
public class ProductDto implements java.io.Serializable {

	private String id;
	private CatalogDto catalog;
	private String name;
	private String description;
	private Integer price;
	private String uom;
	private Date createdAt;
	private Set attachmentses = new HashSet(0);

	public ProductDto() {
	}

	public ProductDto(String id, CatalogDto catalog) {
		this.id = id;
		this.catalog = catalog;
	}

	public ProductDto(String id, CatalogDto catalog, String name, String description,
			Integer price, String uom, Date createdAt) {
		this.id = id;
		this.catalog = catalog;
		this.name = name;
		this.description = description;
		this.price = price;
		this.uom = uom;
		this.createdAt = createdAt;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CatalogDto getCatalog() {
		return this.catalog;
	}

	public void setCatalog(CatalogDto catalog) {
		this.catalog = catalog;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getUom() {
		return this.uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Set getAttachmentses() {
		return this.attachmentses;
	}

	public void setAttachmentses(Set attachmentses) {
		this.attachmentses = attachmentses;
	}

}
