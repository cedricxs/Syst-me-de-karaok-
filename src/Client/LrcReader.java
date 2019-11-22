package Client;

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
	
	public LrcReader(String LrcFileName) {
		// TODO Auto-generated constructor stub
		this.LrcFileName = LrcFileName;
		timer = new Timer();
		time = 0; 
		current = 0;
		lrcs = this.parse(LrcFileName);
	}
	void print(int index, long pos) {
		long start = lrcs.get(index).getKey();
		String text = lrcs.get(index).getText();
		long dure = lrcs.get(index).getDure();
		System.out.println(text);
	}
	
	
	public void run() {
		timer.scheduleAtFixedRate(new TimerTask() {   
		    public void run() {   
		    	time++;
		    	if(time>lrcs.get(current).getKey()+lrcs.get(current).getDure()) {
		    		current++;
		    		print(current,time);
		    	}
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
            // String encoding = "utf-8"; //encoding set 
            String encoding = "GBK";
            File file = new File(path);
            if (file.isFile() && file.exists()) { // if it's exist
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]"; // regex
                Pattern pattern = Pattern.compile(regex); // creat Pattern object
                String lineStr = null; // read string of every line
                while ((lineStr = bufferedReader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(lineStr);
                    while (matcher.find()) {
                        //save current time and lyrics
                        Entry e = new Entry();
                        // System.out.println(m.group(0)); // 例：[02:34.94]
                        // [02:34.94] ----correspondant---> [min:sec.millsec]
                        String min = matcher.group(1);  
                        String sec = matcher.group(2); 
                        String mill = matcher.group(3); // millsec, mutiply by 10
                        long time = getLongTime(min, sec, mill + "0");
                        // get lyrics of current time
                        String text = lineStr.substring(matcher.end());
                        e.put(time, text); // add to contatiner
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
                System.out.println("parse done...");
                return list;
            } else {
                System.out.println("Can't find file:" + path);
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
