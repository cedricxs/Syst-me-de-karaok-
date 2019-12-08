package Music;

import java.io.Serializable;

public class parole implements Serializable{

	private static final long serialVersionUID = 1L;
	// type:0->trémolo
	// 1->crié
	// 2->portamento
	int type;
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
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return this.type;
	}
	public long getTime() {
		return this.time;
	}
	public String getText() {
		return this.text;
	}
	public long getDuree() {
		return this.duree;
	}
}
