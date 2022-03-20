package tk.smileyik.socketconsole.socket.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public class IOCommand {
  private static File basePath;

  public static void setBasePath(File basePath) throws IOException {
    IOCommand.basePath = basePath.getCanonicalFile();
  }

  private static String getSubPath(String str) {
    return str.substring((basePath + "").length());
  }

  public static byte[] ls(String path) {
    if (!path.startsWith("/")) {
      return new byte[0];
    }
    File subPath = new File(basePath, path);

    if (subPath.isFile()) {
      return (path + " is a file.").getBytes(StandardCharsets.UTF_8);
    }

    StringBuilder sb = new StringBuilder();
    for (String file : Objects.requireNonNull(subPath.list())) {
      sb.append((file)).append('\n');
    }
    return sb.toString().getBytes(StandardCharsets.UTF_8);
  }

  public static byte[] getFile(String path) {
    if (!path.startsWith("/")) {
      return new byte[0];
    }
    File file = new File(basePath, path);
    if (file.exists() && file.isFile()) {
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
           BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
           BufferedOutputStream bos = new BufferedOutputStream(baos)) {
        byte[] bytes = new byte[8192];
        int length = -1;
        while ((length = bis.read(bytes)) != -1) {
          bos.write(bytes, 0, length);
        }
        bos.flush();
        return baos.toByteArray();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return new byte[0];
  }

  public static boolean writeFile(String path, byte[] data) {
    if (!path.startsWith("/")) {
      return false;
    }
    File file = new File(basePath, path);
    File parent = file.getParentFile();
    if (!parent.exists()) {
      if (!parent.mkdirs()) {
        return false;
      }
    }
    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
      bos.write(data);
      bos.flush();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }
}
