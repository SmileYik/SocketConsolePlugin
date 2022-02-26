package tk.smileyik.socketconsoleclient.dao;

import com.google.gson.Gson;
import org.springframework.boot.json.GsonJsonParser;
import tk.smileyik.socketconsoleclient.SocketConsoleClientApplication;
import tk.smileyik.socketconsoleclient.entry.User;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
  private static final List<User> users = SocketConsoleClientApplication.getSetting().getUsers();

  public User getUserByUsernameAndPassword(String username, String password) {
    for (User user : users) {
      if (user.getUsername().equalsIgnoreCase(username)
              && user.getPassword().equals(password)) {
        return user;
      }
    }
    return null;
  }
}
