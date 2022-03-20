package tk.smileyik.socketconsole.socket.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class IOSocketTask implements Runnable {
  private final Socket socket;

  private IOSocketTask(Socket socket) {
    this.socket = socket;
  }

  protected static void accept(Socket socket) {
    if (socket != null) {
      new Thread(new IOSocketTask(socket)).start();
    }
  }

  private long byteArrayToNumber(byte[] bytes) {
    if (bytes.length == 0) {
      return 0;
    }
    long num = bytes[0];
    for (int i = 1; i < bytes.length; ++i) {
      num <<= 8;
      num += bytes[i];
    }
    return num;
  }

  private String readHead(BufferedInputStream bis) throws IOException {
    byte[] headSize = new byte[8];
    int length = bis.read(headSize);
    if (length < headSize.length) {
      return "";
    }
    length = (int) byteArrayToNumber(headSize);
    byte[] bytes = new byte[length];
    bis.read(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  private byte[] getBodyData(BufferedInputStream bis) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] bytes = new byte[8192];
    int length = 0;
    while ((length = bis.read(bytes)) != -1) {
      baos.write(bytes, 0, length);
    }
    return baos.toByteArray();
  }

  public void sendData(BufferedOutputStream bos, byte[] bytes) throws IOException {
    bos.write(bytes);
    bos.flush();
  }

  @Override
  public void run() {
    try (BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
         BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {
      String head = readHead(bis);
      String[] command = head.split("\n");

      if (command.length == 0) {
        return;
      } else if (command.length == 2 && command[0].equalsIgnoreCase("ls")) {
        byte[] data = IOCommand.ls(command[1]);
        sendData(bos, data);
      } else if (command.length == 2 && command[0].equalsIgnoreCase("cat")) {
        byte[] data = IOCommand.getFile(command[1]);
        sendData(bos, data);
      } else if (command.length == 2 && command[0].equalsIgnoreCase(">>")) {
        byte[] data = getBodyData(bis);
        boolean flag = IOCommand.writeFile(command[1], data);
        sendData(bos, ("" + flag).getBytes(StandardCharsets.UTF_8));
      } else {
        sendData(bos, ("unknown").getBytes(StandardCharsets.UTF_8));
      }
      socket.shutdownOutput();
      socket.getInputStream();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
