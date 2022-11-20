import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    // здесь создайте сервер, который отвечал бы на нужные запросы
    // слушать он должен порт 8989
    // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
    private static final int PORT = 8989;

    public static void main(String[] args) {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            System.out.println("Сервер стартовал!");
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    out.println("Введите слово для поиска:");
                    String word = in.readLine();
                    List<PageEntry> searchResult = engine.search(word);
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonEngine = mapper.writeValueAsString(searchResult);
                    out.println(jsonEngine);// корректировка по замечанию: Вы ничего не пишете в ответ клиенту, вы выводите на консоль
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}