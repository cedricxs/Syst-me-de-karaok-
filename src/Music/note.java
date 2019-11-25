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
	int instrument;
	int channel;
	int action;
	//notes (avec leur durée, leur hauteur, et peut-être leur date)
	public note(int action,int channel, int instrument, int hauteur,long time) {
		// TODO Auto-generated constructor stub
		this.action = action;
		this.channel = channel;
		this.instrument = instrument;
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
	public int getInstrument() {
		return this.instrument;
	}
	public int getAction() {
		return this.action;
	}
	public int getChannel() {
		return this.channel;
	}

}
