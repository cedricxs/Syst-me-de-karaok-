package Music;

import java.io.Serializable;

public class note implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long dure;
	int hauteur;
	long time;
	int puissance;
	int channel;
	int action;
	byte[] data;
	boolean metaNote;
	//notes (avec leur durée, leur hauteur, et peut-être leur date)
	public void setData(byte[] data) {
		this.data = data;
	}
	public note(byte[] data,int type,long time) {
		this.data = data;
		this.action = type;
		this.time = time;
		metaNote = true;
	}
	public boolean getType() {
		return metaNote;
	}
	public byte[] getData() {
		return this.data;
	}
	public note(int action,int channel, int hauteur, int puissance,long time) {
		// TODO Auto-generated constructor stub
		this.action = action;
		this.channel = channel;
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
	public long getTime() {
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
