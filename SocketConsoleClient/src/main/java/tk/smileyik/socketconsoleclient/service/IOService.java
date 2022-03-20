package tk.smileyik.socketconsoleclient.service;

import tk.smileyik.socketconsoleclient.SocketConsoleClientApplication;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class IOService {

  public String ls(String path) {
    if (path.isEmpty()) {
      path = "/";
    }
    String head = "ls\n" + path;
    try {
      return sendData(head, "");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String cat(String path) {
    if (path.isEmpty()) {
      path = "/";
    }
    String head = "cat\n" + path;
    try {
      return sendData(head, "");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String write(String path, String data) {
    if (path.isEmpty()) {
      path = "/";
    }
    String head = ">>\n" + path;
    try {
      return sendData(head, data);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  private static String sendData(String head, String body) throws IOException {
    Socket socket = new Socket(
            SocketConsoleClientApplication.getSetting().getIohost(),
            SocketConsoleClientApplication.getSetting().getIoport()
    );
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
    byte[] bytes = baos.toByteArray();
    baos.close();
    bis.close();
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
