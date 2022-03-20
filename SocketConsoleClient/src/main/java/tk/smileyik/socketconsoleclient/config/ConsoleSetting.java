package tk.smileyik.socketconsoleclient.config;

import tk.smileyik.socketconsoleclient.entry.User;

import java.util.List;

public class ConsoleSetting {
  private String host;
  private int port;
  private List<User> users;
  private String iohost;
  private int ioport;

  public ConsoleSetting() {

  }

  public ConsoleSetting(String host, int port, List<User> users) {
    this.host = host;
    this.port = port;
    this.users = users;
  }

  public ConsoleSetting(String host, int port, List<User> users, String iohost, int ioport) {
    this.host = host;
    this.port = port;
    this.users = users;
    this.iohost = iohost;
    this.ioport = ioport;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  @Override
  public String toString() {
    return "ConsoleSetting{" +
            "host='" + host + '\'' +
            ", port=" + port +
            ", users=" + users +
            '}';
  }

  public String getIohost() {
    return iohost;
  }

  public void setIohost(String iohost) {
    this.iohost = iohost;
  }

  public int getIoport() {
    return ioport;
  }

  public void setIoport(int ioport) {
    this.ioport = ioport;
  }
}
