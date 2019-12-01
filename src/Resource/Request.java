package Resource;


public class Request implements Data{

	private static final long serialVersionUID = 1L;

	String commande;
	Object content;
	String utilisateur;
	public Request(String commande) {
		this.commande = commande;
	}
	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}
	public String getUtilisateur() {
		return this.utilisateur;
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
