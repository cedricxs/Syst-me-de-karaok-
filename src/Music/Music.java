package Music;

import java.io.Serializable;
import java.util.ArrayList;

public class Music implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	private ArrayList<ArrayList<note>> notes;
	private ArrayList<parole> paroles;
	double vitesse; 
	public Music(String name) {
		this.name = name;
		notes = new ArrayList<ArrayList<note>>();
		paroles = new ArrayList<parole>();
	}
	public ArrayList<ArrayList<note>> getNotes(){
		return this.notes;
	}
	
	public ArrayList<parole> getParoles(){
		return this.paroles;
	}
	public void setVitesse(double tempo) {
		this.vitesse = tempo;
	}
	public double getVitesse() {
		return this.vitesse;
	}
	public String getName() {
		return this.name;
	}
	
	
}
