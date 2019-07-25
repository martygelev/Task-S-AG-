import common.Writable;

public class StringBuilderWriter implements Writable {
	private StringBuilder sb;
	
	StringBuilderWriter() {
		this.sb = new StringBuilder();
	}
	
	public void write(String text) {
		sb.append(text).append(System.lineSeparator());
	}

	public String getResult() {
		return sb.toString().trim();
	}
}
