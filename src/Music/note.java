package Music;

import java.io.Serializable;

public class note implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long dure;
	int hauteur;
	double time;
	int puissance;
	int channel;
	int action;
	byte[] data;
	boolean isMetaNote;
	//notes (avec leur durée, leur hauteur, et peut-être leur date)
	public note(byte[] data,int type,double time) {
		this.data = data;
		this.action = type;
		this.time = time;
		isMetaNote = true;
	}
	public note(int action,int channel, int hauteur, int puissance,double time) {
		// TODO Auto-generated constructor stub
		this.action = action;
		this.channel = channel;
		this.puissance = puissance;
		this.hauteur = hauteur;
		this.time = time;
		isMetaNote = false;
	}
	public boolean isMetaNote() {
		return isMetaNote;
	}
	public void setDure(long dure) {
		this.dure = dure;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public byte[] getData() {
		return this.data;
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
	public int getPuissance() {
		return this.puissance;
	}
	public int getAction() {
		return this.action;
	}
	public int getChannel() {
		return this.channel;
	}

}
