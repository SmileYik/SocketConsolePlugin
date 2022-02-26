package tk.smileyik.socketconsoleclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.smileyik.socketconsoleclient.service.UserService;
import tk.smileyik.socketconsoleclient.socket.SocketConsoleClient;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@RestController
public class LoggerController {
  private static final UserService service = new UserService();

  @GetMapping("/console/log")
  public String getLog(@RequestParam String token, HttpServletRequest request) throws ExecutionException, InterruptedException {
    if (service.isValidToken(token, request.getRemoteHost())) {
      return SocketConsoleClient.getLogs().get();
    }
    return "";
  }
}
