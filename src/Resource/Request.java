package Resource;


public class Request implements Data{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String commande;
	Object content;
	public Request(String commande) {
		// TODO Auto-generated constructor stub
		this.commande = commande;
	}
	public void setCommande(String commande) {
		this.commande = commande;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public Object getContent() {
		return this.content;
	}
	public String getCommande() {
		return this.commande;
	}

}
