package Client;


import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Frame extends JFrame {
	JTextPane jtp;
	Frame(){
		super();
		Container container = this.getContentPane();
		jtp = new JTextPane();
		JScrollPane ScrollPane = new JScrollPane(jtp);
		container.add(ScrollPane);
		//addCloseListenser();
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

		/*
		StyleConstants.setBackground(set, Color.BLACK);
		Document doc = jtp.getStyledDocument();
		 */
		try
		{
			doc.insertString(offset, text, set);//insert le texte
		}
		catch (BadLocationException e)
		{
		}
	}
	
	void addCloseListenser() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	void lanchFrame() {
		setSize(600,400);
		setLocation(50,50);
		setVisible(true);
	}

	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		Frame f = new Frame();
		f.lanchFrame();
		
	}



}
