package Music;

import java.io.Serializable;

public class parole implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//type:1->homme,2->femme,3->choeurs
	int type;
	//les positions de lyrics de ce parole dois chante
	int position;
	public parole() {
		// TODO Auto-generated constructor stub
	}

}
