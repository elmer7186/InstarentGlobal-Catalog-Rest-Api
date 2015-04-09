package product_catalog_system.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import product_catalog_system.domain.AttachmentsDto;
import product_catalog_system.domain.ProductDto;
import product_catalog_system.services.util.exception.DAOException;
/**
 * Clase dao que gestionaran las consultas a la base de datos relacionadas con la tabla Attachments
 * @author Elmer
 *
 */
public class AttachmentsDaoImpl extends HibernateDaoSupport implements AttachmentsDao {

	/**
	 * Variable Logger de inicidencias
	 */
	private static Logger log=Logger.getLogger(AttachmentsDaoImpl.class);
	
	/**
	 * Metodo que pide a la persistencia de Hibernate los Attachments que coincidan con
	 * el Producto que llega por argumento
	 */
	public List<AttachmentsDto> listAttachmentsByProduct(ProductDto producto) throws DAOException{

		Session session = null;
        List<AttachmentsDto> listaAttachments = new ArrayList<AttachmentsDto>();
       
        try{
        	
        	session = getSession();
        	
        	Query query = session.createQuery("from AttachmentsDto where product = :producto ");
        	query.setEntity("producto", producto);
        	
        	listaAttachments = query.list();
        } catch (Exception e) {
        	DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}
        return listaAttachments;
        
	}
	
	/**
	 * Crea nuevo Attachment ingresado por parametro
	 * @param attachment objeto que contiene encapsulado información de un Attachment
	 */
	public void createAttachment(AttachmentsDto attachment) throws DAOException{

		Session session = null;
		
		try{
			session = getSession();
			session.save(attachment);
			session.flush();
		}catch(Exception e){
			DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		}finally{
			session.close();
		}

	}
	
	/***
	 * Elimina los Atachments asociados al producto
	 */
	public void deleteAttachments(ProductDto producto) throws DAOException{
		
		Session session = null;
		Transaction transaction = null;
       
        try{
        	
        	session = getSession();
        	transaction = session.beginTransaction();
        	
        	Query query = session.createQuery("delete from AttachmentsDto where product =:producto ");
        	query.setEntity("producto", producto);
        	int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
        	transaction.commit();
        } catch (Exception e) {
        	transaction.rollback();
        	DAOException expDAO = new DAOException();
			expDAO.setMsjTecnico(e.getMessage());
			expDAO.setOrigen(e);
			
			throw expDAO;
		} finally{
			session.close();
		}
	}

}
