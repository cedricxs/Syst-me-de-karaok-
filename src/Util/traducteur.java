package Util;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

/**
 * 解析MIDI文件主方法
 * 
 * @author Jonny
 * 
 */
public class traducteur {
	// 获取midi
	private File midiFile;
	private ReadMidi readMidi;
	//private WriteMidi writeMidi;

	 /**
	  * 构造方法，初始化数据
	  * 
	  * @param filePath
	  *            文件路径
	  */
	public traducteur(String filePath) {
		  this.readMidi = new ReadMidi();
		  //this.writeMidi = new WriteMidi();
		  this.midiFile = new File(filePath);
		
	}
	
	public static void main(String[] args) throws InvalidMidiDataException, IOException {
		//Sequence s = MidiSystem.getSequence(new File("music/baga01.mid"));
		
		traducteur analysisMain = new traducteur(
	    "music/baga01.mid");
	  try {
	   analysisMain.readMidi.readDateFIle(analysisMain.midiFile);
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	 }
	
}



/**
 * 读取Midi文件信息
 * 
 * @author Jonny
 * 
 */
 class ReadMidi {

 /**
  * 获取midi信息
  * 
  * @param midiFile
  * @throws Exception
  */
public void readDateFIle(File midiFile) throws Exception {
  FileInputStream stream = new FileInputStream(midiFile);
  byte[] data = new byte[stream.available()];
  stream.read(data);
  ByteBuffer byteBuffer = ByteBuffer.wrap(data);
  // 调用获取MTHD信息方法得到头部信息
  HeaderChunks headerChunks = this.readHeaderChunks(byteBuffer);
  List<TrackChunk> trackChunklist = this.readTreackChunks(byteBuffer,
    headerChunks);
  this.outInforMidi(headerChunks, trackChunklist);
  stream.close();
 }

public void outInforMidi(HeaderChunks headerChunks,
   List<TrackChunk> trackChunklist) {
  System.out.println("-------------MThd-----------------");
  System.out.println("头块类型:" + new String(headerChunks.getMidiId()));
  System.out.println("头块长度:" + headerChunks.getLength());
  System.out.println("格式:" + headerChunks.getFormat());
  System.out.println("音轨数:" + headerChunks.getTrackNumber());
  System.out.println("分区:" + headerChunks.getMidiTimeSet());

  for (int i = 0; i < trackChunklist.size(); i++) {
   System.out.println("-------------MTrk-----------------");
   System.out.println("音轨块" + i + ":"
     + new String(trackChunklist.get(i).getMidiId()));
   System.out.println("音轨数据长度:" + trackChunklist.get(i).getLength());
  }
 }

 /**
  * 获取头部信息MThd
  * 
  * @param buffer
  * @return
  */
public HeaderChunks readHeaderChunks(ByteBuffer buffer) {
  HeaderChunks headerChunks = new HeaderChunks();
  for (int i = 0; i < headerChunks.getMidiId().length; i++) {
   headerChunks.getMidiId()[i] = (byte) (buffer.get());
  }
  headerChunks.setLength(buffer.getInt());
  headerChunks.setFormat(buffer.getShort());
  headerChunks.setTrackNumber(buffer.getShort());
  headerChunks.setMidiTimeSet(buffer.getShort());
  return headerChunks;
 }

 /**
  * 获取MTrk信息块
  * 
  * @param buffer
  * @param headerChunks
  * @return
  */
	public List<TrackChunk> readTreackChunks(ByteBuffer buffer,
	   HeaderChunks headerChunks) {

	  ArrayList<TrackChunk> trackChunklist = new ArrayList<TrackChunk>();
	  TrackChunk trackChunk;
	  for (int i = 0; i < headerChunks.getTrackNumber(); i++) {
	   trackChunk = new TrackChunk();
	   for (int j = 0; j < trackChunk.getMidiId().length; j++) {
	    trackChunk.getMidiId()[j] = (byte) (buffer.get());
	   }
	   trackChunk.setLength(buffer.getInt());
	   byte[] data = new byte[trackChunk.getLength()];
	   buffer.get(data);
	   for(int k=0;k<data.length;k++) {
		   System.out.println(data[k]);
	   }
	   trackChunklist.add(trackChunk);
	  }
	  return trackChunklist;
	 }
	}

	 


 class HeaderChunks {
	 // MThd
	private byte[] midiId;
	 // 长度
	private int length;
	 // 格式
	private int format;
	 // track 的数量
	private int trackNumber;
	 // MIDI 的时间设置
	private int midiTimeSet;
	
	public HeaderChunks() {
	  this.midiId = new byte[4];
	 }
	
	public byte[] getMidiId() {
	  return midiId;
	 }
	
	public void setMidiId(byte[] midiId) {
	  this.midiId = midiId;
	 }
	
	public int getLength() {
	  return length;
	 }
	
	public void setLength(int length) {
	  this.length = length;
	 }
	
	public int getFormat() {
	  return format;
	 }
	
	public void setFormat(int format) {
	  this.format = format;
	 }
	
	public int getTrackNumber() {
	  return trackNumber;
	 }
	
	public void setTrackNumber(int trackNumber) {
	  this.trackNumber = trackNumber;
	 }
	
	public int getMidiTimeSet() {
	  return midiTimeSet;
	 }
	
	public void setMidiTimeSet(int midiTimeSet) {
	  this.midiTimeSet = midiTimeSet;
	 }

}

 

 

class TrackChunk {

// MTrk
	private byte[] midiId;

 // 长度
	private int length;
	

	public TrackChunk() {
	  this.midiId = new byte[4];
	 }

	public byte[] getMidiId() {
	  return midiId;
	 }

	public void setMidiId(byte[] midiId) {
	  this.midiId = midiId;
	 }

	public int getLength() {
	  return length;
	 }

	public void setLength(int length) {
	  this.length = length;
	 }

}

