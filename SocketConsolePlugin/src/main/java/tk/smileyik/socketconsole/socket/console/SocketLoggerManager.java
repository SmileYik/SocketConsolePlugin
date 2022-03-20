package tk.smileyik.socketconsole.socket.console;

import tk.smileyik.socketconsole.SocketConsole;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SocketLoggerManager extends TimerTask {
  private static final List<ConsoleServerTask> tasks = new ArrayList<>();
  private static Timer timer;
  private static BufferedReader reader;

  public SocketLoggerManager() throws FileNotFoundException {
    if (reader == null) {
      File dir = SocketConsole.getInstance().getDataFolder();
      dir = dir.getParentFile().getParentFile();
      dir = new File(dir, "logs");
      dir = new File(dir, "latest.log");
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(dir), StandardCharsets.UTF_8));
    }
  }

  public synchronized static void accept(Socket client) throws FileNotFoundException {
    try {
      ConsoleServerTask task = new ConsoleServerTask(client);
      tasks.add(task);
      new Timer().schedule(task, 10L, 200L);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!tasks.isEmpty() && timer == null) {
      timer = new Timer();
      timer.schedule(new SocketLoggerManager(), 1000L, 1000L);
    }
  }

  public synchronized static void stop() {
    timer.cancel();
    timer = null;
    for (ConsoleServerTask task : tasks) {
      task.close();
    }
    tasks.clear();
  }

  private String getLog() throws IOException {
    StringBuilder sb = new StringBuilder();
    String str;
    if (reader.ready()) {
      while ((str = reader.readLine()) != null) {
        sb.append(str).append('\n');
      }
      return sb.toString();
    }
    return "";
  }

  @Override
  public void run() {
    String log = "\n";
    try {
      log = getLog();
    } catch (IOException e) {

    }
    if (log.isEmpty()) {
      return;
    }
    String finalLog = log;
    new ArrayList<>(tasks).forEach(task -> {
      try {
        task.getWriter().write(finalLog);
        task.getWriter().flush();
      } catch (IOException e) {
        task.close();
        tasks.remove(task);
      }
    });
    if (tasks.isEmpty()) {
      stop();
    }
  }
}
