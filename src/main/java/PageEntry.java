import java.io.File;
import java.util.List;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }



    @Override
    public String toString() {
        return "PageEntry{" +
                "pdf='" + pdfName +
                ", page=" + page +
                ", count=" + count + '}' + "\n";
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(PageEntry o) {

        return Integer.compare(count, o.count);
    }
}
