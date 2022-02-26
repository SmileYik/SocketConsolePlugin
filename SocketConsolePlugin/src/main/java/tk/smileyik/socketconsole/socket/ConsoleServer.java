package tk.smileyik.socketconsole.socket;

import tk.smileyik.socketconsole.SocketConsole;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsoleServer {
  public static void start(int port) {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      return;
    }
    SocketConsole.getInstance().getLogger().info("server listening port " + port);
    while (true) {
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
