package tk.smileyik.socketconsoleclient.controller.component;

import org.springframework.stereotype.Component;
import tk.smileyik.socketconsoleclient.service.UserService;
import tk.smileyik.socketconsoleclient.socket.SocketConsoleClient;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

@Component
@ServerEndpoint("/console/consoleSocket")
public class ConsoleMessageSocket extends TimerTask {
  private static final UserService userService = new UserService();

  private String lastSend = null;
  private Session session;
  private String token;

  @OnOpen
  public void onOpen(Session session) throws IOException {
    if (!session.getRequestParameterMap().containsKey("token")) {
      session.close();
      return;
    }
    String token = session.getRequestParameterMap().get("token").stream().findFirst().orElse("");
    if (userService.isValidToken(token, null)) {
      this.session = session;
      this.token = token;
      this.lastSend = null;
      new Timer().schedule(this, 1000L, 200L);
    } else {
      session.close();
    }
  }

  @OnClose
  public void onClose() {
    cancel();
    userService.destroyToken(token);
    try {
      session.close();
    } catch (Exception e) {
    }
  }

  @OnError
  public void onError(Throwable e) {
    e.printStackTrace();
  }

  @OnMessage
  public void onMessage(String message) throws ExecutionException, InterruptedException {
    new Thread(() -> {
      SocketConsoleClient.sendCommand(message);
    }).start();
  }

  public void sendMessage(String message) {
    try {
      session.getAsyncRemote().sendText(message);
    } catch (Exception e) {
      onClose();
    }
  }

  /**
   * The action to be performed by this timer task.
   */
  @Override
  public void run() {
    String now;
    try {
      now = SocketConsoleClient.getLogs().get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return;
    }

    if (now.length() > 5000) {
      now = now.substring(now.length() - 5000);
    }

    if (lastSend == null) {
      lastSend = now;
      sendMessage(lastSend);
    } else if (!now.equals(lastSend)) {
      lastSend = now;
      sendMessage(lastSend);
    }
  }
}
