import java.util.Comparator;

public class FileComparator implements Comparator<SearchFile> {
	public int compare(SearchFile f1, SearchFile f2) {
		if (f1.getSize() == f2.getSize()) {
			return f1.getName().compareTo(f2.getName());
		}
		return Long.compare(f1.getSize(), f2.getSize());
	}
}
