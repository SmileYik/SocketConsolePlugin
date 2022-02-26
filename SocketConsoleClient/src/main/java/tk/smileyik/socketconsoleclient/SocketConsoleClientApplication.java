package tk.smileyik.socketconsoleclient;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.smileyik.socketconsoleclient.config.ConsoleSetting;
import tk.smileyik.socketconsoleclient.socket.SocketConsoleClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@EnableAsync
@SpringBootApplication
@ServletComponentScan
public class SocketConsoleClientApplication {
  private static ConsoleSetting setting;

  public static void main(String[] args) {
    setting = loadSetting();
    try {
      SocketConsoleClient.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
    SpringApplication.run(SocketConsoleClientApplication.class, args);
  }


  private static ConsoleSetting loadSetting() {
    InputStream is = SocketConsoleClientApplication.class.getResourceAsStream("/console-setting.json");
    Gson gson = new Gson();
    ConsoleSetting setting = gson.fromJson(
            new InputStreamReader(is, StandardCharsets.UTF_8),
            ConsoleSetting.class
    );
    try {
      is.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return setting;
  }

  public static ConsoleSetting getSetting() {
    return setting;
  }
}
