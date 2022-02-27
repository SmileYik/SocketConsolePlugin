package tk.smileyik.socketconsole.socket;

import tk.smileyik.socketconsole.SocketConsole;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

public class ConsoleServerTask extends TimerTask {
  private final Socket client;
  private final BufferedReader reader;
  private final BufferedWriter writer;

  public ConsoleServerTask(Socket client) throws IOException {
    this.client = client;
    reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
    writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
  }

  @Override
  public void run() {
    try {
      if (!reader.ready()) {
        return;
      }
    } catch (IOException e) {
      cancel();
      return;
    }
    String msg;
    try {
      if (reader.ready() && (msg = reader.readLine()) != null) {
        SocketConsole.executeCommand(msg);
      }
    } catch (IOException e) {
      cancel();
      close();
    }
  }

  public BufferedReader getReader() {
    return reader;
  }

  public BufferedWriter getWriter() {
    return writer;
  }

  public Socket getClient() {
    return client;
  }

  public void close() {
    cancel();
    try {
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
