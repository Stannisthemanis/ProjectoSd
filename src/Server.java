import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Diogo on 14/10/2014.
 */
public class Server {

    public static ArrayList<DataOutputStream> douts = new ArrayList<DataOutputStream>();
//    public static boolean mainServer;

    public static void main(String[] args) {
        try {
            createServer();
        } catch (IOException e) {
//            mainServer = false;
            System.out.println("\n********************************************");
            System.out.println("Secundary Server: ok...");
            checkMainServer();
        }


    }

    private static void checkMainServer() {
        DatagramSocket dataSocket = null;
        String host = "localhost";
        int serverPort = 6666;
        byte[] m = new byte[1000];
        boolean flag = false;

        try {
            InetAddress aHost = InetAddress.getByName(host);
            dataSocket = new DatagramSocket();
            dataSocket.setSoTimeout(10000);

            while (true) {
                if (((((System.currentTimeMillis() / 1000) % 10) == 0) || (((System.currentTimeMillis() / 1000) % 10) == 5)) && flag == true) {
                    DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                    System.out.println("\n********************************************");
                    System.out.println("Sending request to Main...");
                    dataSocket.send(request);
                    byte[] buffer = new byte[1000];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    dataSocket.receive(reply);
                    System.out.println("\n********************************************");
                    System.out.println("Received reply from Main...");
                    flag = false;
                } else if ((((System.currentTimeMillis() / 1000) % 10) != 0) && (((System.currentTimeMillis() / 1000) % 10) != 5)) {
                    flag = true;
                }
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (dataSocket != null) dataSocket.close();
            System.out.println("\n********************************************");
            System.out.println("Main Server Timeout...");
            System.out.println("\n********************************************");
            System.out.println("Becoming Main Server...");
            try {
                createServer();
            } catch (IOException e) {

            }
        }


    }

    private static void createServer() throws IOException {
        int serverPort = 6000;
        ServerSocket listenSocket = new ServerSocket(serverPort);
        System.out.println("\n********************************************");
        System.out.println("Main Server: ok...");
        System.out.println("\n********************************************");
        System.out.println("Main server listening in port: " + serverPort);
        System.out.println("\n********************************************");
        System.out.println("LISTEN SOCKET= " + listenSocket);

        new respondToSecundary();

        while (true) {
            Socket clientSocket = listenSocket.accept();
            System.out.println("\n+++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Client connected with socket: " + clientSocket);
            new Connection(clientSocket);
        }
    }
}

class respondToSecundary extends Thread {
    DatagramSocket dataSocket;
    int dataSocketPort;

    respondToSecundary() {
        dataSocketPort = 6666;
        this.start();
    }

    public void run() {
        try {
            dataSocket = new DatagramSocket(dataSocketPort);
            System.out.println("\n********************************************");
            System.out.println("Socket to Secundary: Ready in port " + dataSocketPort);
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                dataSocket.receive(request);
                System.out.println("\n********************************************");
                System.out.println("Server: Received request from Secundary...");
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
                System.out.println("\n********************************************");
                System.out.println("Server: Responding to Secundary...");
                dataSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("DatagramSocket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Comunication to Secundary: " + e.getMessage());
        } finally {
            if (dataSocket != null) dataSocket.close();
        }
    }
}

class Connection extends Thread {

    DataOutputStream out;
    DataInputStream in;
    Socket clientSocket;
    String name;

    Connection(Socket cSocket) {
        try {
            this.clientSocket = cSocket;
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.name = in.readUTF();
            Server.douts.add(this.out); //
            this.start();
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    public void run() {
        int request;
        while (true) {
            try {
                request = in.read();
                switch (request) {
                    case 1:
                        replyNewMeeting();
                        break;
                }

            } catch (IOException e) {
                System.out.println("Receiving request from client: " + e.getMessage());
            }
        }

    }


    public void replyNewMeeting() {
        String newMeeting = null;
        try {
            System.out.println("Server: Received request from " + this.name + " to create new meeting");
            out.writeBoolean(true);
            System.out.println("Server: Waiting for meeting information");
            newMeeting = in.readUTF();
            System.out.println("Server: Information received");
            out.writeBoolean(true);
            System.out.println("Server: New meeting created");
        } catch (IOException e) {
            System.out.println("Reply new Meeting: " + e.getMessage());
        }
    }

    public void chat() {
        String name = null;
        String text = "";
        try {
            //saying hello!
            name = in.readUTF();
            System.out.println("-> " + name + " connected");
            out.writeUTF("Ola " + name);
            //
            while (true) {
                text = in.readUTF();
                for (DataOutputStream dout : Server.douts) { //
                    dout.writeUTF(text.toUpperCase());
                    System.out.println("sending... tam: " + Server.douts.size());
                }
            }
        } catch (IOException e) {
            System.out.println("Receiving name: " + e.getMessage());
        } finally {
            Server.douts.remove(this.out); //
            if (name != null) {
                System.out.println("-> " + name + " disconnected");
            }
        }
    }
}