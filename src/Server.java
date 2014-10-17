import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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
            System.out.println("->> Server2: Secundary Server ok...");
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
                    System.out.println("->> Server2: Sending request to Main...");
                    dataSocket.send(request);
                    byte[] buffer = new byte[1000];
                    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                    dataSocket.receive(reply);
                    System.out.println("->> Server2: Received reply from Main...");
                    flag = false;
                } else if ((((System.currentTimeMillis() / 1000) % 10) != 0) && (((System.currentTimeMillis() / 1000) % 10) != 5)) {
                    flag = true;
                }
            }
        } catch (SocketException e) {
            System.out.println("*** Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("*** EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("*** IO: " + e.getMessage());
        } finally {
            if (dataSocket != null) dataSocket.close();
            System.out.println("->> Server2: Main Server Timeout...");
            System.out.println("->> Becoming Main Server...");
            try {
                createServer();
            } catch (IOException e) {

            }
        }


    }

    private static void createServer() throws IOException {
        int serverPort = 6000;
        ServerSocket listenSocket = new ServerSocket(serverPort);
        System.out.println("->> Server: Main Server ok...");
        System.out.println("->> Server: Main server listening in port: " + serverPort);
        System.out.println("->> Server: LISTEN SOCKET= " + listenSocket);

//        System.getProperties().put("java.security.policy", "policy.all");
//        System.setSecurityManager(new RMISecurityManager());

        //Thread para responder ao 2o servidor que este ainda esta up
        new respondToSecundary();

        RmiServerInterface dataBaseServer = null;

        //Acesso ao servidor rmi
        try {
            dataBaseServer = (RmiServerInterface) Naming.lookup("DataBase");
        } catch (NotBoundException e) {
            System.out.println("->> Server: Registing to rmiServer " + e.getMessage());
        }

        //Aceitar novas connecçoes de cliente e ligar com elas
        while (true) {
            Socket clientSocket = listenSocket.accept();
            System.out.println("->> Server: Client connected with socket " + clientSocket);
            new Connection(clientSocket, dataBaseServer);
        }
    }
}


//Thread que lida com a connecçao ao 2o server
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
            System.out.println("->> Server: Socket to Secundary ready in port " + dataSocketPort);
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                dataSocket.receive(request);
                System.out.println("->> Server: Received request from Secundary...");
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
                System.out.println("->> Server: Responding to Secundary...");
                dataSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("*** DatagramSocket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("*** Comunication to Secundary: " + e.getMessage());
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
    RmiServerInterface dataBaseServer;

    Connection(Socket cSocket, RmiServerInterface dataBaseServer) {
        try {
            this.clientSocket = cSocket;
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.name = in.readUTF();
            this.dataBaseServer = dataBaseServer;
            System.out.println("->> Server: " + name + "Connected");
            Server.douts.add(this.out); //
            this.start();
        } catch (IOException e) {
            System.out.println("*** Connection: " + e.getMessage());
        }
    }

    public void run() {
        int request;
        try {
            while (true) {
                request = in.read();
                switch (request) {
                    case 1:
                        replyNewMeeting();
                        break;
                    case 2:
                        replyCheckUpcumingMeetings();
                        break;
                    case 3:
                        replyCheckPassedMeetings();
                        break;
                }
            }
        } catch (EOFException e) {
            System.out.println("*** Receiving request from client: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("*** Receiving request from client: " + e.getMessage());
        }

    }

    public synchronized void replyNewMeeting() {
        String newMeeting = null;
        try {
            System.out.println("->> Server: Received request from " + this.name + " to create new meeting");
            out.writeBoolean(true);
            System.out.println("->>Server: Waiting for meeting information");
            newMeeting = in.readUTF();
            System.out.println("->> Server: Information received");
            if (dataBaseServer.addNewMeeting(newMeeting) == true) {
                out.writeBoolean(true);
                System.out.println("->> Server: New meeting created");
            } else {
                out.writeBoolean(false);
                System.out.println("->> Server: Failed to create new meeting");
            }
        } catch (IOException e) {
            System.out.println("*** Reply new Meeting: " + e.getMessage());
        }
    }

    public void replyCheckUpcumingMeetings() {
        System.out.println("->> Server: Received request to send all upcuming meeting of " + this.name);
        try {
            System.out.println("->> Server: Sending all upcuming meeting of " + this.name);
            out.writeUTF("1- Reuniao de equipa");
        } catch (IOException e) {
            System.out.println("->> Replying upcuming meeting: " + e.getMessage());
        }
    }

    public void replyCheckPassedMeetings() {
        System.out.println("Server: Received request to send all passed meeting of " + this.name);
        try {
            System.out.println("Server: Sending all passed meeting of " + this.name);
            out.writeUTF("1- Stannis the Mannis Meeting\n2- Stannis King of the andal and YOUR MOTHER");
        } catch (IOException e) {
            System.out.println("Replying upcuming meeting: " + e.getMessage());
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