package product_catalog_system.dao;

import java.util.List;

import product_catalog_system.domain.AttachmentsDto;
import product_catalog_system.domain.ProductDto;
import product_catalog_system.services.util.exception.DAOException;

public interface AttachmentsDao {
	
	public List<AttachmentsDto> listAttachmentsByProduct(ProductDto producto) throws DAOException;
	
	public void createAttachment(AttachmentsDto attachment) throws DAOException;
	
	public void deleteAttachments(ProductDto producto) throws DAOException;
	
}
