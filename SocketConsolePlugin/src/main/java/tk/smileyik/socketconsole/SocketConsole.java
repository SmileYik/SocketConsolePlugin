package tk.smileyik.socketconsole;

import org.bukkit.plugin.java.JavaPlugin;
import tk.smileyik.socketconsole.socket.ConsoleServer;
import tk.smileyik.socketconsole.socket.SocketLoggerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SocketConsole extends JavaPlugin {
  private static SocketConsole instance;
  private static List<String> ips;

  public SocketConsole() {
    instance = this;
  }

  @Override
  public void onEnable() {
    File dir = getDataFolder();
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File config = new File(dir, "config.yml");
    if (!config.exists()) {
      saveDefaultConfig();
    }
    reloadConfig();

    ips = getConfig().getStringList("white-list");
    int port = getConfig().getInt("port");
    getServer().getScheduler().runTaskAsynchronously(this, () -> {
      new Thread(() -> ConsoleServer.start(port)).start();
    });

  }

  public static SocketConsole getInstance() {
    return instance;
  }

  public static boolean validIp(String ip) {
    if (ips == null) {
      ips = new ArrayList<>();
    }
    return ips.contains(ip);
  }

  public static void executeCommand(String command) {
    instance.getServer().getScheduler().runTask(instance, () -> {
      instance.getLogger().info("dispatch command: " + command);
      instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), command);
    });
  }

  public static void log(String msg) {
    instance.getLogger().info(msg);
  }

  @Override
  public void onDisable() {
    ConsoleServer.setEnable(false);
    SocketLoggerManager.stop();
  }
}
