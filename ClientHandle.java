import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandle implements Runnable {
    private static LinkedBlockingQueue<ClientIO> clients = null;
    private static FixedSizeQueue<String> previousMessages = null;
    private ClientIO currentClient = null;
    XMLReader configData;
    /* Initialize only once */
    static {
        clients = new LinkedBlockingQueue<ClientIO>();
        previousMessages = new FixedSizeQueue<String>();
    }

    public ClientHandle(Socket socket, XMLReader configData ) throws Exception {
        try {
            this.configData = configData;
            currentClient = new ClientIO(socket);
            String name = currentClient.read();
            String password = currentClient.read();
            /* Check can we connect this user or not */
            if (!configData.checkNewClient(name, password)) {
                currentClient.print("Wrong name or password");
                return;
            }
            if (clients.size() < configData.getNumberOfClients())
              clients.put(currentClient);
            else {
                currentClient.print("Sorry, we already have too many users");
                currentClient.print("Please, try later");
                throw new TooManyClientsException();
            }
            /* Everything o'key.
             * Send last messages to new client */
            for (String str : previousMessages)
                currentClient.print(str);
        } catch(Exception e) {
            if (currentClient != null)
              currentClient.close();
            System.out.println("Can't connected");
            throw e;
        }
    }

    public void run() {
        String inputLine;

        try {
            /* Read from client */
            while ((inputLine = currentClient.read()) != null) {
                if (inputLine.equals("")) {
                    currentClient.close();
                    clients.remove(currentClient);
                    break;
                }
                /* Save this message */
                previousMessages.put(inputLine, configData.getNumberOfLastMessages());
                /* Send this message to other clients */
                for (ClientIO c : clients)
                    if (!currentClient.equals(c))
                        c.print(inputLine);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
