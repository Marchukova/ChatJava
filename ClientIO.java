import java.net.*;
import java.io.*;

public class ClientIO implements Closeable  {
  private Socket socket = null;
  private ObjectOutputStream out = null;
  private ObjectInputStream in = null;
  private Message message;

  public ClientIO(Socket socket) throws IOException {
      this.socket = socket;
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
      message = new Message("a", "a", "a", "a");
  }

  /* Read message from socket */
  String read() throws ClassNotFoundException, IOException {
         message = (Message) in.readObject();
      return message.getMessageText();
  }
  /* Write message to socket */
  void print( String str ) {
      message = new Message(message, str);
      try {
          out.writeObject(message);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  public void close() {
       try {
          if (out != null)
            out.close();
          if (in != null)
            in.close();
           if (socket != null)
             socket.close();
     } catch (Exception e) {
           ;
     }
  }
}
