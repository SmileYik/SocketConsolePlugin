
import org.junit.jupiter.api.Test;
import tk.smileyik.socketconsole.socket.io.IOCommand;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class IOCommandTest {
  @Test
  public void getSubPath() throws IOException {
    IOCommand.setBasePath(new File("").getCanonicalFile());
    System.out.println(new String(IOCommand.ls("/")));
    System.out.println(IOCommand.writeFile("/abc/bcd/456.txt", "bbb".getBytes(StandardCharsets.UTF_8)));
    System.out.println(new String(IOCommand.getFile("/abc/bcd/456.txt")));
  }
}
