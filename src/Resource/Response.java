package Resource;

public class Response implements Data{

	private static final long serialVersionUID = 1L;
	private Object content;
	int status;
	private String name;
	public Response() {
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return this.status;
	}
	public void setContent(Object c) {
		this.content = c;
	}
	@Override
	public Object getContent() {
		return this.content;
	}

}
