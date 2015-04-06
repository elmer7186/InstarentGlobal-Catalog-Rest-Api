package product_catalog_system.domain;

// Generated 4/04/2015 10:16:30 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Product generated by hbm2java
 */
public class Product implements java.io.Serializable {

	private String id;
	private Catalog catalog;
	private String name;
	private String description;
	private Integer price;
	private String uom;
	private Date createdAt;
	private Set attachmentses = new HashSet(0);

	public Product() {
	}

	public Product(String id, Catalog catalog) {
		this.id = id;
		this.catalog = catalog;
	}

	public Product(String id, Catalog catalog, String name, String description,
			Integer price, String uom, Date createdAt, Set attachmentses) {
		this.id = id;
		this.catalog = catalog;
		this.name = name;
		this.description = description;
		this.price = price;
		this.uom = uom;
		this.createdAt = createdAt;
		this.attachmentses = attachmentses;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Catalog getCatalog() {
		return this.catalog;
	}

	public void setCatalog(Catalog catalog) {
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