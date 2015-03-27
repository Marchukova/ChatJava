//import java.xml.parses;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLReader {
    private final File file;
    private final int numberOfClients, numberOfLastMessages;
    private final NodeList clients;

    XMLReader( String fileName ) throws ParserConfigurationException,
                                        SAXException, IOException {
        this.file = new File(fileName);

        /* Initialize builder for parsing the xml file */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();

        /* Parse xml file */
        Document document = builder.parse(file);
        Element root = document.getDocumentElement();
        numberOfClients = Integer.parseInt(root.getAttributes().
                            getNamedItemNS(null, "numberOfClients").getNodeValue());
        numberOfLastMessages = Integer.parseInt(root.getAttributes().
                                getNamedItemNS(null, "numberOfLastMessages").getNodeValue());
        /* Save clients data for future use
         * in check of client and its password */
        clients = document.getElementsByTagName("client");
    }

    /* Check exist client with given name and password or not */
    public boolean checkNewClient( String name, String password ) {
      for (int i = 0; i < clients.getLength(); i++) {
          NamedNodeMap clientAttr = clients.item(i).getAttributes();
          if (clientAttr.getNamedItemNS(null, "name").getNodeValue().equals(name)
                  && clientAttr.getNamedItemNS(null, "password").getNodeValue().equals(password))
              return true;
      }

      return false;
    }
    public int getNumberOfClients() { return numberOfClients; }
    public int getNumberOfLastMessages() { return numberOfLastMessages; }
}

