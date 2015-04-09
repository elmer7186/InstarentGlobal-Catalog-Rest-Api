package product_catalog_system.services.util.exception;

public class DAOException extends Exception {
	
	private String msjTecnico;
	private Exception origen;
	
	public String getMsjTecnico() {
		return msjTecnico;
	}
	public void setMsjTecnico(String msjTecnico) {
		this.msjTecnico = msjTecnico;
	}
	public Exception getOrigen() {
		return origen;
	}
	public void setOrigen(Exception origen) {
		this.origen = origen;
	}
	
	public DAOException() {
		
	}

}
