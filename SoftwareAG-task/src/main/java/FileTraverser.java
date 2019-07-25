import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import Exceptions.FileException;
import common.Writable;
import org.apache.commons.io.FilenameUtils;

public class FileTraverser {
	private static final String NO_RESULTS_FOUND = "No results found";
	private static final String NOT_EXIST_DIRECTORY = "ERROR: Directory %s don't exists";
	private static final String LINE_RESULT_FORMAT = "File name: %s, size: %d bytes";
	private static final long FIRST_BYTES_FOR_COMPRESSED_FILE = 0x504B0304;

	private Set<SearchFile> resultFiles;
	private final Writable writer;

	FileTraverser(Writable writer){
		this.resultFiles = new TreeSet<>(new FileComparator());
		this.writer = writer;
	}

	String traverseDirectory(String path, String searchWord) throws FileException, IOException {
		File root = new File(path);

		if (!root.exists()) {
			throw new FileException(String.format(NOT_EXIST_DIRECTORY,path));
		}

		dfs(root, searchWord);
		return getResult();
	}

	private void dfs(File root, String searchWord) throws FileException, IOException {
		File[] currentFiles = root.listFiles();

		if (currentFiles != null) {
			for (File file : currentFiles) {
				if (file.isDirectory()) {
					dfs(file, searchWord);
				} else {
					if (this.compressed(file)) {
						traverseCompressed(file.getAbsolutePath(), searchWord);
					} else {
						boolean isContainsSearchWord = wordCheck(file, searchWord);
						if (isContainsSearchWord) {
							addFileToResultSet(file);
						}
					}
				}
			}
		}
	}

	private boolean compressed(File file) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		long n = raf.readInt();
		raf.close();
		return n == FIRST_BYTES_FOR_COMPRESSED_FILE;

	}

	private void traverseCompressed(String fileName, String searchWord) throws IOException, FileException {
		ZipInputStream zipInp;
		try (Scanner sc = new Scanner(zipInp = new ZipInputStream(new FileInputStream(new File(fileName))))){

			for (ZipEntry zipEntry; (zipEntry = zipInp.getNextEntry()) != null;) {
				boolean isExistSearchWord = false;
				while (sc.hasNextLine()) {
					if (sc.nextLine().contains(searchWord)) {
						isExistSearchWord = true;
						break;
					}
				}
				
				if (isExistSearchWord) {
					addFileToResultSet(zipEntry);
				}
			}
		} 
	}
	
	private boolean wordCheck(File file, String searchWord) throws FileNotFoundException {
		try (Scanner sc = new Scanner(file)) {
			while (sc.hasNextLine()) {
				if (sc.nextLine().contains(searchWord)) {
					return true;
				}
			}
			return false;
		}
	}

	private void addFileToResultSet(File file) throws FileException {
		String fileAbsolutePath = file.getName();
		long fileSize = file.length();
		String fileSimpleName = this.fileName(fileAbsolutePath);
		SearchFile searchFile = this.create(fileSimpleName, fileSize);
		this.resultFiles.add(searchFile);
	}

	private void addFileToResultSet(ZipEntry file) throws FileException {
		String fileAbsolutePath = file.getName();
		long fileSize = file.getSize();
		String fileSimpleName = this.fileName(fileAbsolutePath);
		SearchFile searchFile = this.create(fileSimpleName, fileSize);
		this.resultFiles.add(searchFile);
	}

	private String fileName(String absolutePath) {
		String baseName = FilenameUtils.getBaseName(absolutePath);
		String extension = FilenameUtils.getExtension(absolutePath);
		return baseName + "." + extension;
	}

	private SearchFile create(String name, long size) throws FileException {
		return new SearchFile(name, size);
	}

	private String getResult() {
		if (this.resultFiles.size() == 0) {
			this.writer.write(NO_RESULTS_FOUND);
		} else {
			for (SearchFile searchFile : this.resultFiles) {
				this.writer.write(String.format(LINE_RESULT_FORMAT, searchFile.getName(), searchFile.getSize()));
			}
		}

		return this.writer.getResult();
	}
}
