import java.io.Serializable;
/* This class is sending between server and clients */
public class Message implements Serializable {
  private String userName;
  private String date;
  private String IP;
  private String state;
  private String messageText;

  Message( String userName, String date, String IP, String state ) {
    this.userName = userName;
    this.date = date;
    this.IP = IP;
    this.state = state;
  }

  Message( Message m, String str ) {
    this(m.userName, m.date, m.IP, m.state);
    this.messageText = str;
  }

  void setMessageText( String messageText ) {
      this.messageText = messageText;
  }
  String getMessageText() { return messageText; }

}
