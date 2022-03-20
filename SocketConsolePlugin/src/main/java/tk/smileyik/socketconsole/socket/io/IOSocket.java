package tk.smileyik.socketconsole.socket.io;

import tk.smileyik.socketconsole.SocketConsole;
import tk.smileyik.socketconsole.socket.console.SocketLoggerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IOSocket {
  private static boolean enable = false;


  public static void start(int port) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      return;
    }
    setEnable(true);
    SocketConsole.getInstance().getLogger().info("IO server listening port " + port);
    while (isEnable()) {
      try {
        Socket clientSocket = serverSocket.accept();
        if (SocketConsole.validIp(clientSocket.getInetAddress().getHostAddress())) {
          IOSocketTask.accept(clientSocket);
        } else {
          SocketConsole.log("Block remote IO connect: " + clientSocket.getInetAddress());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void setEnable(boolean enable) {
    IOSocket.enable = enable;
  }

  public static boolean isEnable() {
    return enable;
  }
}
