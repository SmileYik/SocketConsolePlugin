package tk.smileyik.socketconsoleclient.controller;

import org.springframework.web.bind.annotation.*;
import tk.smileyik.socketconsoleclient.service.IOService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class IOController {
  private static final IOService IO_SERVICE = new IOService();

  @GetMapping("/console/io/ls")
  public String ls(@RequestParam String path, HttpServletRequest request) throws ExecutionException, InterruptedException {
    return IO_SERVICE.ls(path);
  }

  @GetMapping("/console/io/cat")
  public String cat(@RequestParam String path, HttpServletRequest request) {
    return IO_SERVICE.cat(path);
  }

  @PostMapping("/console/io/write")
  public String write(@RequestParam String path, @RequestBody Map<String, String> b, HttpServletRequest request) {
    String body = b.get("b");
    return IO_SERVICE.write(path, body);
  }
}
