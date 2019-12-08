package Music;

import java.io.Serializable;

public class parole implements Serializable{

	private static final long serialVersionUID = 1L;
	// type:0->trémolo
	// 1->crié
	// 2->portamento
	int type;
	//voix:0->homme,1->femme,2->choeurs
	int voix;
	//Position des paroles
	long time;
	long duree;
	String text;
	public parole(long time,String text) {
		this.time = time;
		this.text = text;
	}
	public void setDuree(long duree) {
		this.duree = duree;
	}
	public long getDuree() {
		return this.duree;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return this.type;
	}
	public void setVoix(int voix) {
		this.voix = voix;
	}
	public int getVoix() {
		return this.voix;
	}
	public long getTime() {
		return this.time;
	}
	public String getText() {
		return this.text;
	}
}
