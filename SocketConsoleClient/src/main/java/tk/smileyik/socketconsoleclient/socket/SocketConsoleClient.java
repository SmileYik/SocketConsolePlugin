package tk.smileyik.socketconsoleclient.socket;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import tk.smileyik.socketconsoleclient.SocketConsoleClientApplication;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.logging.Logger;

@Async
public class SocketConsoleClient {
  private static final StringBuilder log = new StringBuilder();
  private static SocketConsoleClient client;

  private final Socket socket;
  private final BufferedReader reader;
  private final BufferedWriter writer;

  private SocketConsoleClient(String ip, int port) throws IOException {
    socket = new Socket(ip, port);
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
  }

  public static void start() throws IOException {
    client = new SocketConsoleClient(
            SocketConsoleClientApplication.getSetting().getHost(),
            SocketConsoleClientApplication.getSetting().getPort()
    );
  }

  public static void stop() {
    try {
      client.writer.close();
    } catch (IOException e) {

    }
    try {
      client.reader.close();
    } catch (IOException e) {

    }
    try {
      client.socket.close();
    } catch (IOException e) {

    }
  }
  public static void sendCommand(String message) {
    client.writeLine(message);
  }

  public void writeLine(String str) {
    str += '\n';
    try {
      writer.write(str);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Async
  public void getLog() {
    StringBuilder sb = new StringBuilder();
    String str = null;
    try {
      while (reader.ready() && (str = reader.readLine()) != null) {
        sb.append(str).append('\n');
      }
    } catch (Exception e) {
      stop();
      Logger logger = Logger.getLogger("SocketClient");

      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          logger.warning("Try to reconnect to server ...");
          try {
            start();
            logger.warning("Reconnect to server successes ...");
          } catch (IOException ex) {
            logger.warning("Reconnect to server failed ...");
          }
        }
      }, 1000L);
    }

    if (sb.length() != 0) {
      SocketConsoleClient.log.append(sb);
    }
  }

  public static Future<String> getLogs() {
    client.getLog();
    return AsyncResult.forValue(log.toString());
  }
}
