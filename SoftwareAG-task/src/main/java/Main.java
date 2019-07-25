import common.Writable;

import java.util.Scanner;

public class Main {
	private static final String EMPTY_DIRECTORY_NAME_MESSAGE = "ERROR: Empty directory";
	private static final String EMPTY_SEARCH_TEXT_MESSAGE = "ERROR: Empty text";
	private static final String DIRECTORY_MESSAGE = "Directory: ";
	private static final String TEXT_MESSAGE = "Search: ";

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)){
			System.out.println(DIRECTORY_MESSAGE);
			String root = scanner.nextLine().trim();
			while(root.isEmpty()) {
				System.out.println(EMPTY_DIRECTORY_NAME_MESSAGE);
				root = scanner.nextLine();
			}

			System.out.println(TEXT_MESSAGE);
			String searchText = scanner.nextLine().trim();
			while(searchText.isEmpty()) {
				System.out.println(EMPTY_SEARCH_TEXT_MESSAGE);
				searchText = scanner.nextLine();
			}
			
			Writable writer = new StringBuilderWriter();
			FileTraverser fileTraverser = new FileTraverser(writer);
			
			String result = fileTraverser.traverseDirectory(root, searchText);
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
