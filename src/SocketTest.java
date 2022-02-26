import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SocketTest {

  public static void main(String[] args) throws IOException {
    Socket socket = new Socket("127.0.0.1", 16500);
    Scanner in = new Scanner(System.in);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

    new Thread(() -> {
      String str;
      try {
        while ((str = br.readLine()) != null) {
          System.out.println(str);
        }
      } catch (Exception e) {}
    }).start();

    String str;
    while (true) {
      str = in.nextLine();
      System.out.println(str);
      bw.write(str + "\n");
      bw.flush();
    }
  }
}
