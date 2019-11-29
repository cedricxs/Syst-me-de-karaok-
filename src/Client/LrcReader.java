package Client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcReader {

	Timer timer;
	long time;
	String LrcFileName;
	List<Entry> lrcs;
	int current;
	Frame myFrame;
	
	public LrcReader(String LrcFileName) {
		// TODO Auto-generated constructor stub
		this.LrcFileName = LrcFileName;
		timer = new Timer();
		time = 0; 
		current = 0;
		lrcs = this.parse(LrcFileName);
		myFrame = new Frame();
	}
	void ChangeProcessus(int index, long pos) {
		long start = lrcs.get(index).getKey();
		String text = lrcs.get(index).getText();
		long dure = lrcs.get(index).getDure();
		//System.out.println(text);
		;
		int position = (int) ((pos-start)*text.length()/dure);
		int rest = text.length()+1-position;
		int last = (int) ((pos-1-start)*text.length()/dure);
		if(position>last) {
			String r = myFrame.getDocument(myFrame.length()-rest-1,1);
			myFrame.removeDocument(myFrame.length()-rest-1,1);
			myFrame.insertDocument(r, Color.green, myFrame.length()-rest);
		}
				
	}
	
	
	
	public void run() {
		myFrame.lanchFrame();
		myFrame.insertDocument(lrcs.get(current).getText()+"\n", Color.GRAY,0);
		timer.scheduleAtFixedRate(new TimerTask() {   
		    public void run() {   
		    	time++;
		    	if(time>lrcs.get(current).getKey()+lrcs.get(current).getDure()) {
		    		current++;
		    		myFrame.insertDocument(lrcs.get(current).getText()+"\n", Color.GRAY,myFrame.length());
		    	}
		    	ChangeProcessus(current,time);
		    	//System.out.println(" ---"+((time-lrcs.get(current).getKey()))*100/lrcs.get(current).getDure()+"/100%---");
		    }   
		}  , new Date(), 1);
	}
	public static void main(String[] args) {
		LrcReader l = new LrcReader("client/芒种.lrc");
		l.run();
		
	}
	

	/**
     * 解析LRC歌词文件
     * 
     * @param path
     *            lrc文件路径
     * @return
     */
    private List<Entry> parse(String path) {
        // 存储所有歌词信息的容器
        List<Entry> list = new ArrayList<Entry>();
        try {
            // String encoding = "utf-8"; // 字符编码，若与歌词文件编码不符将会出现乱码
            String encoding = "GBK";
            File file = new File(path);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]"; // 正则表达式
                Pattern pattern = Pattern.compile(regex); // 创建 Pattern 对象
                String lineStr = null; // 每次读取一行字符串
                while ((lineStr = bufferedReader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(lineStr);
                    while (matcher.find()) {
                        // 用于存储当前时间和文字信息的容器
                        Entry e = new Entry();
                        // System.out.println(m.group(0)); // 例：[02:34.94]
                        // [02:34.94] ----对应---> [分钟:秒.毫秒]
                        String min = matcher.group(1); // 分钟
                        String sec = matcher.group(2); // 秒
                        String mill = matcher.group(3); // 毫秒，注意这里其实还要乘以10
                        long time = getLongTime(min, sec, mill + "0");
                        // 获取当前时间的歌词信息
                        String text = lineStr.substring(matcher.end());
                        e.put(time, text); // 添加到容器中
                        if(!list.isEmpty()) {
                        	Entry last = list.get(list.size()-1);
                        	last.setDure(e.getKey()-last.getKey());
                        }
                        list.add(e);
                    }
                }
                Entry last = list.get(list.size()-1);
            	last.setDure(10);
                read.close();
                System.out.println("解析完成。。。");
                return list;
            } else {
                System.out.println("找不到指定的文件:" + path);
            }
        } catch (Exception e) {
            System.out.println("读取文件出错!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将以字符串形式给定的分钟、秒钟、毫秒转换成一个以毫秒为单位的long型数
     * 
     * @param min
     *            分钟
     * @param sec
     *            秒钟
     * @param mill
     *            毫秒
     * @return
     */
    private long getLongTime(String min, String sec, String mill) {
        // 转成整型
        int m = Integer.parseInt(min);
        int s = Integer.parseInt(sec);
        int ms = Integer.parseInt(mill);

        if (s >= 60) {
            System.out.println("警告: 出现了一个时间不正确的项 --> [" + min + ":" + sec + "."
                    + mill.substring(0, 2) + "]");
        }
        // 组合成一个长整型表示的以毫秒为单位的时间
        long time = m * 60 * 1000 + s * 1000 + ms;
        return time;
    }

}
class Entry{
	private long key;
	private String text;
	private long dure;
	void setKey(long key) {
		this.key = key;
	}
	long getKey() {
		return key;
	}
	void setText(String text) {
		this.text = text;
	}
	String getText() {
		return this.text;
	}
	void put(long key, String text) {
		setKey(key);
		setText(text);
	}
	void setDure(long dure) {
		this.dure = dure;
	}
	long getDure() {
		return this.dure;
	}
}
