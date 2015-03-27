
import java.io.*;
import java.net.*;

public class Client {
    private static ClientIO inOut = null; //variable for interaction with server
    private static BufferedReader stdIn = null;
    private static String name;

    public static void main(String[] args) throws IOException {
        Thread ownMessage, anotherMessage;
        String hostName = "127.0.0.1";
        int portNumber = 1234;

        try {
            inOut = new ClientIO(new Socket(hostName, portNumber));
            stdIn = new BufferedReader(
                            new InputStreamReader(System.in));

            System.out.println("Enter your name:");
            name = stdIn.readLine();
            inOut.print(name);
            System.out.println("Enter your password:");
            inOut.print(stdIn.readLine());

            /* Sent to server what get from System.in */
            ownMessage = new Thread( new Runnable(){
                @Override
                public void run() {
                    String userInput;
                    try {
                        while ((userInput = stdIn.readLine()) != null) {
                            if (userInput.length() == 0) {
                                inOut.print(userInput);
                                break;
                            }
                            inOut.print(name + ":" + userInput);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            /* Get messages from server and print them on scree */
            anotherMessage  = new Thread( new Runnable(){
                @Override
                public void run() {
                    String anotherUserInput;
                    try {
                        while ((anotherUserInput = inOut.read()) != null) {
                            System.out.println(anotherUserInput);
                        }
                    } catch (EOFException e) {
                        //This means client quit the program, so do nothing
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace(); // Something wrong with serialization
                    }
                }
            });
            /* Start threads */
            anotherMessage.start();
            ownMessage.start();
            ownMessage.join();
            anotherMessage.join();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (stdIn != null)
                stdIn.close();
            if (inOut != null)
                inOut.close();
        }
    }
}
