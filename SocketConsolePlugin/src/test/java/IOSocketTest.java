import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class IOSocketTest {

  public static void main(String[] args) throws IOException {
    String host = "127.0.0.1";
    int port = 16565;
    Scanner in = new Scanner(System.in);
    while (true) {
      System.out.print("$ ");
      String str = in.nextLine();
      StringBuilder sb = new StringBuilder(str);
      sb.setCharAt(str.indexOf(" "), '\n');
      str = sb.toString();
      if (str.startsWith(">>")) {
        String body = in.nextLine();
        sendData(new Socket(host, port), str, body);
      } else {
        sendData(new Socket(host, port), str, "");
      }
    }

  }

  private static void sendData(Socket socket, String head, String body) throws IOException {
    long length = head.length();

    byte[] headSize = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(length).array();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    baos.write(headSize);
    baos.write(head.getBytes(StandardCharsets.UTF_8));
    baos.write(body.getBytes(StandardCharsets.UTF_8));
    socket.getOutputStream().write(baos.toByteArray());
    socket.getOutputStream().flush();
    socket.shutdownOutput();
    baos.close();
    BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
    baos = new ByteArrayOutputStream();
    byte[] buffers = new byte[8192];
    int len;
    while ((len = bis.read(buffers)) != -1) {
      baos.write(buffers, 0, len);
    }
    System.out.println(new String(baos.toByteArray()));
    baos.close();
    bis.close();
  }
}
