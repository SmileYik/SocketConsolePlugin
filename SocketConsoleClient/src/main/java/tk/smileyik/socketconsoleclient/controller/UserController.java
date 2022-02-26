package tk.smileyik.socketconsoleclient.controller;

import org.springframework.web.bind.annotation.*;
import tk.smileyik.socketconsoleclient.entry.User;
import tk.smileyik.socketconsoleclient.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
  private static final UserService userService = new UserService();

  @PostMapping("/login")
  public Map<String, Object> login(@RequestBody User user, HttpServletRequest request) {
    User hasUser = userService.login(user.getUsername(), user.getPassword());
    if (hasUser == null) {
      return new HashMap<>();
    }
    request.getSession().setAttribute("user", hasUser);
    String token = userService.attachToken(user, request.getRemoteHost());
    Map<String, Object> map = new HashMap<>();
    map.put("token", token);
    return map;
  }
}
