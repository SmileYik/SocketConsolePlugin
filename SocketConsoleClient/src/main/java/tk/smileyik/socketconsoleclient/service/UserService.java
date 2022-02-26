package tk.smileyik.socketconsoleclient.service;

import tk.smileyik.socketconsoleclient.dao.UserDao;
import tk.smileyik.socketconsoleclient.entry.User;

import java.util.*;

public class UserService {
  private static final UserDao userDao = new UserDao();
  private static final Map<String, User> tokens = new HashMap<>();
  private static final Map<String, String> tokensToIp = new HashMap<>();

  public User login(String username, String password) {
    if (username == null || password == null) {
      return null;
    }
    return userDao.getUserByUsernameAndPassword(username, password);
  }

  public String attachToken(User user, String remoteIp) {
    String uuid = null;

    for (Map.Entry<String, User> entry : tokens.entrySet()) {
      if (entry.getValue().getUsername().equals(user.getUsername())) {
        uuid = entry.getKey();
        break;
      }
    }
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
      tokens.put(uuid, user);
    }
    tokensToIp.put(uuid, remoteIp);

    String finalUuid = uuid;
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        tokens.remove(finalUuid);
        tokensToIp.remove(finalUuid);
      }
    }, 3600000L);

    return finalUuid;
  }

  public boolean isValidToken(String token, String remoteIp) {
    if (token == null) {
      return false;
    }
    return tokens.containsKey(token) && (remoteIp == null || tokensToIp.get(token).equalsIgnoreCase(remoteIp));
  }

  public void destroyToken(String token) {
    if (token == null) {
      return;
    }
    tokens.remove(token);
    tokensToIp.remove(token);
  }
}
