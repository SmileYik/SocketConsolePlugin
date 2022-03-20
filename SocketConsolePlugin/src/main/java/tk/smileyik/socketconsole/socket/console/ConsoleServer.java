package tk.smileyik.socketconsole.socket.console;

import tk.smileyik.socketconsole.SocketConsole;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsoleServer {
  public static boolean enable = true;

  public static void setEnable(boolean enable) {
    ConsoleServer.enable = enable;
  }

  public static void start(int port) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      return;
    }
    setEnable(true);
    SocketConsole.getInstance().getLogger().info("server listening port " + port);
    while (enable) {
      try {
        Socket clientSocket = serverSocket.accept();
        if (SocketConsole.validIp(clientSocket.getInetAddress().getHostAddress())) {
          SocketLoggerManager.accept(clientSocket);
          SocketConsole.log("Remote connect: " + clientSocket.getInetAddress());
        } else {
          SocketConsole.log("Block remote connect: " + clientSocket.getInetAddress());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
