import Exceptions.FileException;

class SearchFile {
	private static final String INVALID_FILE_NAME = "File name cannot be null or empty string!";

	private String name;
	private long size;

	SearchFile(String name, long size) throws FileException {
		this.setName(name);
		this.setSize(size);
	}

	String getName() {
		return name;
	}

	private void setName(String name) throws FileException {
		if (name == null || name.trim().length() <= 0) {
			throw new FileException(INVALID_FILE_NAME);
		}

		this.name = name;
	}

	long getSize() {
		return size;
	}

	private void setSize(long size) {
		this.size = size;
	}

}
