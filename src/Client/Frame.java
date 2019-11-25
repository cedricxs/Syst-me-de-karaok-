package Client;


import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Frame extends JFrame {
	JTextPane j;
	Frame(){
		super();
		Container container = this.getContentPane();
		//container.setLayout(new GridLayout(1,1));
		j = new JTextPane();
		JScrollPane ScrollPane = new JScrollPane(j);
		//JPanel contPane = new JPanel();
		//contPane.add(ScrollPane);
		container.add(ScrollPane);
		//addCloseListenser();
		//insertDocument("Blue text", Color.BLUE);
	}
	public int length() {
		return j.getStyledDocument().getLength();
	}
	
	public String getDocument(int offset, int length) {
		Document doc = j.getStyledDocument();
		try {
			return doc.getText(offset, length);
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public void removeDocument(int offset, int length) {
		Document doc = j.getStyledDocument();
		try {
			doc.remove(offset, length);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insertDocument(String text , Color textColor,int offset)//根据传入的颜色及文字，将文字插入文本域
	{
		SimpleAttributeSet set = new SimpleAttributeSet();
		StyleConstants.setForeground(set, textColor);//设置文字颜色
		StyleConstants.setFontSize(set, 12);//设置字体大小
		Document doc = j.getStyledDocument();
		try
		{
			doc.insertString(offset, text, set);//插入文字
		}
		catch (BadLocationException e)
		{
		}
	}
	
	void addCloseListenser() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		addWindowListener(new WindowAdapter(){
//			public void windowClosing(WindowEvent e) {
//				System.exit(0);
//			}
//		});
	}
	void lanchFrame() {
		setSize(600,400);
		setLocation(50,50);
		setVisible(true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		Frame f = new Frame();
		f.lanchFrame();
		
	}
	private Image offScreenImage;  //图形缓存
	@Override
	  public void update(Graphics g)
	  {
	         if(offScreenImage == null)
	            offScreenImage = this.createImage(500, 500);     //新建一个图像缓存空间,这里图像大小为800*600
	            Graphics gImage = offScreenImage.getGraphics();  //把它的画笔拿过来,给gImage保存着
	            paint(gImage);                                   //将要画的东西画到图像缓存空间去
	            g.drawImage(offScreenImage, 0, 0, null);         //然后一次性显示出来
	  }



}
