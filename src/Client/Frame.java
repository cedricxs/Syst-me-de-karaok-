package Client;


import java.awt.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Frame extends JFrame {
	JTextPane jtp;
	Animation beat;
	JLayeredPane layeredPane;
	Frame(){
		super();
		Container container = this.getContentPane();
		jtp = new JTextPane();
		JScrollPane ScrollPane = new JScrollPane(jtp);
		beat = new Animation(50);
		beat.setBounds(350, 100, 500, 500);
		ScrollPane.setBounds(0, 0, 300, 500);
		layeredPane = new JLayeredPane();
		layeredPane.add(beat, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(ScrollPane, JLayeredPane.POPUP_LAYER);
		container.add(layeredPane);
	}
	public int length() {
		return jtp.getStyledDocument().getLength();
	}
	
	public String getDocument(int offset, int length) {
		Document doc = jtp.getStyledDocument();
		try {
			return doc.getText(offset, length);
			
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void removeDocument(int offset, int length) {
		Document doc = jtp.getStyledDocument();
		try {
			doc.remove(offset, length);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	public void clear() {
		Document doc = jtp.getStyledDocument();
		try {
			doc.remove(0,doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	//insert un texte avec couleur et offset dans le textPane
	public void insertDocument(String text , Color textColor,int offset)
	{
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, textColor);//assigne couleur
		StyleConstants.setFontSize(set, 12);//assigne pointure

		//StyleConstants.setBackground(set, Color.BLACK);

		Document doc = jtp.getStyledDocument();
		try
		{
			doc.insertString(offset, text, set);//insert le texte
		}
		catch (BadLocationException e)
		{
		}
	}

	public void changeBeat(int hauteur){
		if(hauteur > 0) {
			layeredPane.remove(layeredPane.getIndexOf(beat));
			beat.w = 2*hauteur;
			beat.h = 2*hauteur;
			layeredPane.add(beat, JLayeredPane.DEFAULT_LAYER);
		}
	}

	void lanchFrame() {
		setSize(600,400);
		setLocation(50,50);
		setVisible(true);
	}

	private static final long serialVersionUID = 1L;
}
