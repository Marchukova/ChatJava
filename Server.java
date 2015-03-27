import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        XMLReader configData;
        Thread t = null;
        String fileName = "chat.xml";
        int portNumber = 1234;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(portNumber); //connect to portNumber
        } catch (IOException e) {
            System.out.println("Could not listen on port " + portNumber);
            //Can't connect so stop the program
            return;
        }
        try {
            /* Read configuration data from XML file */
            configData = new XMLReader(fileName);
            try {
                while (listening) {
                    try {
                        /* Create thread of new client on server */
                        t = new Thread(new ClientHandle(serverSocket.accept(), configData));
                        t.start();
                    } catch (TooManyClientsException e) {
                        ;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Main thread was interrupted");
            } catch (ClassNotFoundException e) {
                System.out.println("Serialization mistake");
            } catch (IOException e) {
                System.out.println("Could not open stream or read");
            }
        } catch (ParserConfigurationException e) {
            System.out.println("Wrong parser configuration");
        } catch (SAXException e) {
            System.out.println("Mistake in xml file");
        } catch (IOException e) {
            System.out.println("Couldn't open file " + fileName);
        } catch (Exception e) {
            System.out.println("Something wrong");
        } finally {
            if (serverSocket != null)
                serverSocket.close(); //always close connection
        }
    }
}
