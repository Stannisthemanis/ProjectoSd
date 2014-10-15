import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Diogo on 14/10/2014.
 */
public class Server {

    public static boolean mainServer = true;

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Sintax: java Server function(0 = main Server, 1 = secundary)");
            System.exit(0);
        }


        //args[0] define se e servidor principal ou secundario
        if (args[0].equals("0")) {
            mainServer = true;
            System.out.println("Main Server: ok..");
        } else if (args[0].equals("1")) {
            System.out.println("Secundary Server: ok..");
            mainServer = false;
        } else {
            System.out.println("Sintax: java Server function(0 = main Server, 1 = secundary)");
            System.exit(0);
        }

        if (mainServer == true) {

            try {
                int serverPort = 6000;
                System.out.println("Main server listening in port: " + serverPort);
                ServerSocket listenSocket = new ServerSocket(serverPort);
                System.out.println("LISTEN SOCKET= " + listenSocket);

                //estar a escuta de clientes

                while (true) {
                    Socket clientSocket = listenSocket.accept();
                    System.out.println("Client connected with socket: " + clientSocket);
                }
            } catch (IOException e) {
                System.out.println("Listen: " + e.getMessage());
            }

        }

    }
}

class Connection extends Thread {

    DataOutputStream out;
    DataInputStream in;
    Socket clientSocket;

    Connection(DataOutputStream out, DataInputStream in, Socket clientSocket) {
        this.out = out;
        this.in = in;
        this.clientSocket = clientSocket;
    }


}