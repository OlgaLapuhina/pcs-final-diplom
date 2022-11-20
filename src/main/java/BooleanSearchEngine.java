import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> indexing = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) {
        // Прочтите тут все pdf и сохраните нужные данные,
        // т.к. во время поиска сервер не должен уже читать файлы
        for (File fileEntry : pdfsDir.listFiles()) {

            try (var doc = new PdfDocument(new PdfReader(fileEntry))) {
                for (int i = 1; i <= doc.getNumberOfPages(); i++) { // страницы начинаются с 1
                    var text = PdfTextExtractor.getTextFromPage(doc.getPage(i)); //получить текст со страницы
                    String pdfName = fileEntry.getName();
                    int pageEntryPage = i;
                    var words = text.split("\\P{IsAlphabetic}+");//разбить текст на слова
                    Map<String, Integer> freqs = new HashMap<>();// мапа, где ключом будет слово, а значением - частота
                    for (var word : words) { // перебираем слова в файлах пдф
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    }

                    for (var entry : freqs.entrySet()) {
                        List<PageEntry> pageEntryFinal = new ArrayList<>();
                        if (indexing.containsKey(entry.getKey())) {
                            pageEntryFinal = indexing.get(entry.getKey());
                        }
                        pageEntryFinal.add(new PageEntry(pdfName, pageEntryPage, entry.getValue()));

                        //   Collections.sort(pageEntryFinal, Collections.reverseOrder());
                        //   Корректировка по замечанию: Эффективнее отсортировать каждый список по разу
                        //   после их заполнения, а не при добавлении каждого элемента в них - сортировку перенесла в
                        //   метод поиска списка по слову
                        indexing.put(entry.getKey(), pageEntryFinal);
                    }

                }

            } catch (IOException e) {
                System.out.println("Документ не открывается");
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> pageEntryResult = new ArrayList<>(indexing.get(word.toLowerCase()));//корректировка по замечанию: Нужно учитывать регистр букв запроса
        pageEntryResult.sort(Collections.reverseOrder());
        return pageEntryResult;
    }
}




