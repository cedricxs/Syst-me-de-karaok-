package Music;

import java.io.Serializable;

public class note implements Serializable{

	private static final long serialVersionUID = 1L;
	long dure;
	int hauteur;
	double time;
	int puissance;
	int command;
	//notes (avec leur durée, leur hauteur, et leur position)
	public note(int command, int hauteur, int puissance, double time) {
		// TODO Auto-generated constructor stub
		this.command = command;
		this.puissance = puissance;
		this.hauteur = hauteur;
		this.time = time;
	}
	public void setDure(long dure) {
		this.dure = dure;
	}
	public long getDure() {
		return this.dure;
	}
	public int getHauteur() {
		return this.hauteur;
	}
	public double getTime() {
		return this.time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public int getPuissance() {
		return this.puissance;
	}
	public int getCommand() {
		return this.command;
	}

}
