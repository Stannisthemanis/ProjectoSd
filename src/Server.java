import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Diogo on 14/10/2014.
 */
public class Server {

    public static ArrayList<Connection> onlineUsers = new ArrayList<Connection>();
    public static RmiServerInterface dataBaseServer;
    public static String rmiConnectionException = "java.net.ConnectException: Connection refused: connect";

    public static void main(String[] args) {
        String hostname = null;
        while (true) {
            try {
                hostname = mainIsRunning();
                System.out.println(hostname);
                if (hostname == null)
                    createServer();
                else
                    checkMainServer(hostname);
            } catch (IOException e) {
                System.out.println("\n*** Creating Server: " + e.getMessage());
            }
        }


    }

    private static void checkMainServer(String hostname) {
        DatagramSocket dataSocket = null;
        String host = hostname;
        int serverPort = 6666;
        byte[] m = new byte[1000];

        try {
            InetAddress aHost = InetAddress.getByName(host);
            dataSocket = new DatagramSocket();
            dataSocket.setSoTimeout(10000);
            System.out.println("\n->> Server2: Secundary Server ok...");
            while (true) {
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                System.out.println("\n->> Server2: Sending request to Main...");
                dataSocket.send(request);
                byte[] buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                dataSocket.receive(reply);
                System.out.println("->> Server2: Received reply from Main...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("*** Server2: Sleeping..");
                }
            }
        } catch (SocketException e) {
            System.out.println("\n*** Socket on Server2: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("\n*** EOF on Server2: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("\n*** IO on Server2: " + e.getMessage());
        } finally {
            if (dataSocket != null) dataSocket.close();
            System.out.println("\n->> Server2: Main Server Timeout...");
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
        System.out.println("->> Server: Main server listening IN port: " + serverPort);
        System.out.println("->> Server: LISTEN SOCKET= " + listenSocket);

        //Thread para responder ao 2o servidor que este ainda esta up
        new respondToSecundary();


        connectToRmi();

        //Aceitar novas connecçoes de cliente e ligar com elas
        while (true) {
            System.out.println("Aqui");
            Socket clientSocket = listenSocket.accept();
            System.out.println("\n->> Server: Client connected with SOCKET " + clientSocket);
            new Connection(clientSocket);
        }
    }

    public static void connectToRmi() throws IOException {
        //Acesso ao servidor rmi
        String rmiHost[] = {"Roxkax", "ricardo"};
        boolean connected = false;
        int i = 0;
        while (connected == false) {
            i++;
            System.out.println(i);
            try {
                dataBaseServer = (RmiServerInterface) Naming.lookup("rmi://" + rmiHost[i % 2] + ":1099/DataBase");
                connected = true;
            } catch (MalformedURLException e) {
                System.out.println("->> URL Server: Registing to rmiServer " + e.getMessage());
                connected = false;
            } catch (NotBoundException e) {
                System.out.println("->> BOUND Server: Registing to rmiServer " + e.getMessage());
                connected = false;
            } catch (RemoteException e) {
                System.out.println("->> REMOTE Server: Registing to rmiServer " + e.getMessage());
                connected = false;
            }
        }
        System.out.println("->> Server: Connection to RmiServer ok...");
    }

    private static String mainIsRunning() {
        int serverPort = 6000;
        Socket test;
        InetAddress hostTest;
        int flag;
        try {
            hostTest = InetAddress.getByName("Roxkax");
            flag = 0;
        } catch (UnknownHostException e) {
            try {
                hostTest = InetAddress.getByName("ricardo");
                flag = 1;
            } catch (UnknownHostException e1) {
                flag = 2;
                System.out.println("ggajgaj");
            }
        }

        if (flag == 0) {
            try {
                test = new Socket("localhost", serverPort);
                return "localhost";
            } catch (IOException e) {
                System.out.println("1-" + e.getMessage());
                try {
                    test = new Socket("ricardo", serverPort);
                    return "ricardo";
                } catch (IOException e1) {
                    System.out.println("2-" + e.getMessage());
                    return null;
                }
            }

        } else if (flag == 1) {
            try {
                test = new Socket("localhost", serverPort);
                return "localhost";
            } catch (IOException e) {
                System.out.println("1-" + e.getMessage());
                try {
                    test = new Socket("Roxkax", serverPort);
                    return "Roxkax";
                } catch (IOException e1) {
                    System.out.println("2-" + e.getMessage());
                    return null;
                }
            }
        } else {
            System.out.println("gjajga");
            return null;
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
            System.out.println("->> Server: Socket to Secundary ready IN port " + dataSocketPort);
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                dataSocket.receive(request);
                System.out.println("\n->> Server: Received request from Secundary...");
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
                System.out.println("->> Server: Responding to Secundary...");
                dataSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("\n*** DatagramSocket on comunication to secundary: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("\n*** Comunication to Secundary: " + e.getMessage());
        } finally {
            if (dataSocket != null) dataSocket.close();
        }
    }
}

class Connection extends Thread {

    DataOutputStream out;
    DataInputStream in;
    Socket clientSocket;
    String user;

    Connection(Socket cSocket) {

        try {

            this.clientSocket = cSocket;
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.user = null;

            Server.onlineUsers.add(this); //
            this.start();
        } catch (IOException e) {
            Server.onlineUsers.remove(this);
            System.out.println("\n*** Connection of  " + user + ": " + e.getMessage());
        }
    }

    public void run() {
        String read = null;
        boolean login;
        while (user == null) {
            try {
                System.out.println(read);
                if (read == null)
                    read = in.readUTF();
                if (read.split(",").length == 1) {
                    out.writeBoolean(Server.dataBaseServer.findUser(read) != null);
                    read = null;
                } else if (read.split(",").length == 2) {
                    login = Server.dataBaseServer.checkLogin(read.split(",")[0], read.split(",")[1]);
                    out.writeBoolean(login);
                    if (login) {
                        this.user = Server.dataBaseServer.findUser(read.split(",")[0]).getUserName();
                    }
                    read = null;
                } else if (read.split(",").length == 6) {
                    this.user = Server.dataBaseServer.addNewUser(read.replaceAll(",", "-")).getUserName();
                    if (this.user == null)
                        out.writeBoolean(false);
                    else
                        out.writeBoolean(true);
                    read = null;
                } else {
                    out.writeBoolean(false);
                    read = null;
                    System.out.println("\n*** Sintax incorrect for login/register..");
                }

            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Testing login.." + e.getMessage());
                    return;
                }
            }
        }
        System.out.println("->> Server: " + user + " connected");
        int request = 0;
        while (true) {
            System.out.println("waintg request");
            try {
                if (request == 0)
                    request = in.read();
                switch (request) {
                    case 1:
                        replyNewMeeting();
                        request = 0;
                        break;
                    case 2:
                        replyCheckUpcumingMeetings();
                        request = 0;
                        break;
                    case 3:
                        replyCheckPassedMeetings();
                        request = 0;
                        break;
                    case 4:
                        replyInfoMeeting(1);
                        request = 0;
                        break;
                    case 5:
                        replyInfoMeeting(2);
                        request = 0;
                        break;
                    case 6:
                        replyAgendaItensFromMeeting(1);
                        request = 0;
                        break;
                    case 7:
                        replyAgendaItensFromMeeting(2);
                        request = 0;
                        break;
                    case 8:
                        replyUnreadMessages();
                        request = 0;
                        break;
                    case 9:
                        replyMessage();
                        request = 0;
                        break;
                    case 10:
                        replyNumberOfMessages();
                        request = 0;
                        break;
                    case 11:
                        replyAddAgendaItem();
                        request = 0;
                        break;
                    case 12:
                        replyRemoveAgendaItem();
                        request = 0;
                        break;
                    case 13:
                        replyModifyTitleAgendaItem();
                        request = 0;
                        break;
                    case 14:
                        replyAddKeyDecisionToAgendaItem();
                        request = 0;
                        break;
                    case 15:
                        replyAddActionItem();
                        request = 0;
                        break;
                    case 16:
                        replySizeOfTodo();
                        request = 0;
                        break;
                    case 17:
                        replyActionItemsFromUser();
                        request = 0;
                        break;
                    case 18:
                        replySetActionAsDone();
                        request = 0;
                        break;
                    case 19:
                        replyCheckCurrentMeetings();
                        request = 0;
                        break;
                    case 20:
                        replyInfoMeeting(3);
                        request = 0;
                        break;
                    case 21:
                        replyAgendaItensFromMeeting(3);
                        request = 0;
                        break;
                    case 22:
                        replyActionItensFromMeeting();
                        request = 0;
                        break;
                    case 23:
                        replyMessagesFromAgendaItem();
                        request = 0;
                        break;
                    case 24:
                        replyAddMessageToAgendaItem();
                        request = 0;
                        break;
                    case 25:
                        System.out.println("a");
                        replyIfUserExists();
                        request = 0;
                        break;
                    case 26:
                        replyRemoveFromChat();
                        request = 0;
                        break;
                    case 27:
                        replyMessageHistoryFromAgendaItem();
                        request = 0;
                        break;
                    case 28:
                        replyInviteToMeeting();
                        request = 0;
                        break;
                }
            } catch (EOFException e) {
                System.out.println("\n*** EOF Receiving request from " + user + ": " + e.getMessage());
                return;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** IO Receiving request from " + user + ": " + e.getMessage());
                    return;
                }
            }
        }

    }

    public void replyNewMeeting() {
        String newMeeting = null;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("\n->> Server: Received request from " + this.user + " to create new meeting");
                System.out.println("->>Server: Waiting for meeting information");
                if (newMeeting == null)
                    newMeeting = in.readUTF();
                System.out.println("->> Server: Information received");
                if (Server.dataBaseServer.addNewMeeting(newMeeting) == true) {
                    System.out.println("->> Server: New meeting created");
                    out.writeBoolean(true);
                } else {
                    out.writeBoolean(false);
                    System.out.println("->> Server: Failed to create new meeting");
                }
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Reply new meeting creating by " + user + ": " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyCheckUpcumingMeetings() {
        boolean sucess = false;
        System.out.println("\n->> Server: Received request to send all upcuming meeting of " + this.user);
        while (sucess == false) {
            try {
                System.out.println("->> Server: Sending all upcuming meeting of " + this.user);
                out.writeUTF(Server.dataBaseServer.getUpcumingMeetings(user));
                System.out.println("-> sended.....");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Replying upcuming meeting by " + user + ": " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyCheckPassedMeetings() {
        System.out.println("\n->> Server: Received request to send all passed meeting of " + this.user);
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("->> Server: Sending all passed meeting of " + this.user);
                out.writeUTF(Server.dataBaseServer.getPassedMeetings(user));
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Replying passed Meeting: " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyCheckCurrentMeetings() {
        boolean sucess = false;
        System.out.println("\n->> Server: Received request to send all current meeting of " + this.user);
        while (sucess == false) {
            try {
                System.out.println("->> Server: Sending all current meeting of " + this.user);
                out.writeUTF(Server.dataBaseServer.getCurrentMeetings(user));
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Replying current meeting by " + user + ": " + e.getMessage());
                    return;
                }
            }
        }
    }

    //flag 1- FutureMeeting 2- PassedMeeting
    public void replyInfoMeeting(int flag) {
        System.out.println("\n->> Server: Received request to send meeting information from " + this.user);
        int meeting = -1;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("->> Server: Waiting for info of requested meeting by " + this.user);
                if (meeting == -1)
                    meeting = in.read();
                System.out.println("->> Server: Sending meeting info to " + this.user);
                out.writeUTF(Server.dataBaseServer.getMeetingInfo(flag, meeting, this.user));
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Replying to send info of meeting to " + user + ": " + e.getMessage());
                    return;
                }
            }
        }

    }

    //flag 1- FutureMeeting 2- PassedMeeting
    public void replyAgendaItensFromMeeting(int flag) {
        System.out.println("\n->> Server: Received request to send agenda itens from a meeting by " + user);
        int n = -1;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("->> Server: Wainting for info meeting...");
                if (n == -1)
                    n = in.read();
                System.out.println("->> Server: Sending agenda itens of meeting.. ");
                out.writeUTF(Server.dataBaseServer.getAgendaItemFromMeeting(flag, n, user));
                System.out.println("->> Server Info send with sucess..");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Receiving meeting number for agenda item... " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyUnreadMessages() {
        boolean sucess = false;
        System.out.println("\n->> Server: Received request to send messages of USER: " + user);
        while (sucess == false) {
            try {
                System.out.println("->> Server: Sending messages of " + user);
                out.writeUTF(Server.dataBaseServer.getMessagesByUser(user));
                System.out.println("->> Server: Messages send with sucess ");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Sending messages: " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyMessage() {
        System.out.println("\n->> Server: Received request of " + user + " to respond to a message..");
        int n = -1;
        boolean reply = false;
        boolean readedReply = false;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("->> Server: Waiting for message number..");
                if (n == -1)
                    n = in.read();
                System.out.println("->> Server: Sending message resume..");
                out.writeUTF(Server.dataBaseServer.getResumeOfMessage(user, n));
                System.out.println("->> Server: Waiting for USER to decline or accept..");
                if (readedReply == false) {
                    reply = in.readBoolean();
                    readedReply = true;
                }
                out.writeBoolean(Server.dataBaseServer.setReplyOfInvite(user, n, reply));
                System.out.println("->> Server: Answer received with sucess");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Replying to message: " + e.getMessage());
                    return;
                }
            }
        }

    }

    public void replyNumberOfMessages() {
        boolean sucess = false;
        System.out.println("\n->> Server: Received request to send number of messages of USER " + user);
        while (sucess == false) {
            try {
                out.write(Server.dataBaseServer.getNumberOfMessages(user));
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Server: Sending number of messages of USER " + user + ":" + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyAddAgendaItem() {
        String newItem = null;
        int n = -1;
        boolean sucess = false;

        while (sucess == false) {
            try {
                System.out.println("\n->> Server: Received request to add a new agenda item ..");
                System.out.println("->> Server: Waiting for the info of the new agenda item ..");
                if (n == -1)
                    n = in.read();
                if (newItem == null)
                    newItem = in.readUTF();
                System.out.println("->> Server: Info received add agenda item now ..");
                out.writeBoolean(Server.dataBaseServer.addAgendaItem(n, newItem, user));
                System.out.println("->> Server: New agenda item added with sucess ..");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Server: Adding new agendaItem " + e.getMessage());
                    return;
                }
            }
        }

    }

    public void replyRemoveAgendaItem() {
        int numAgendaItem = -1;
        int n = -1;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("\n->> Server: Received request to remove aagenda item ..");
                System.out.println("->> Server: Waiting for the info of agenda item to remove..");
                if (n == -1)
                    n = in.read();
                if (numAgendaItem == -1)
                    numAgendaItem = in.read();
                System.out.println("->> Server: Info received, removing agenda item now ..");
                out.writeBoolean(Server.dataBaseServer.removeAgendaItem(n, numAgendaItem, user));
                System.out.println("->> Server: Agenda item removed with sucess ..");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Server: Adding new agendaItem " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyModifyTitleAgendaItem() {
        int numAgendaItem = -1;
        int n = -1;
        String newAgendaItem = null;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("\n->> Server: Received request to modify agenda item ..");
                System.out.println("->> Server: Waiting for the info of agenda item to modify..");
                if (n == -1)
                    n = in.read();
                if (numAgendaItem == -1)
                    numAgendaItem = in.read();
                System.out.println("->> Server: Info received Waiting for new agenda itemToDiscuss now ..");
                if (newAgendaItem == null)
                    newAgendaItem = in.readUTF();
                out.writeBoolean(Server.dataBaseServer.modifyTitleAgendaItem(n, numAgendaItem, newAgendaItem, user));
                System.out.println("->> Server: Agenda item changed with sucess ..");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Server: Adding new agendaItem " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyAddKeyDecisionToAgendaItem() {
        int numAgendaItem = -1;
        int n = -1;
        String newKeyDecision = null;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("\n->> Server: Received request to add key decision to agenda item ..");
                System.out.println("->> Server: Waiting for the info of agenda item to modify..");
                if (n == -1)
                    n = in.read();
                if (numAgendaItem == -1)
                    numAgendaItem = in.read();
                System.out.println("->> Server: Info received Waiting for key decision now ..");
                if (newKeyDecision == null) {
                    newKeyDecision = in.readUTF();
                }
                out.writeBoolean(Server.dataBaseServer.addKeyDecisionToAgendaItem(n, numAgendaItem, newKeyDecision, user));
                System.out.println("->> Server: Agenda item changed with sucess ..");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Server: Adding new agendaItem " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyAddActionItem() {
        String newItem = null;
        int n = -1;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("\n->> Server: Received request to add action item ..");
                System.out.println("->> Server: Waiting for the info of the new action item ..");
                if (n == -1)
                    n = in.read();
                if (newItem == null)
                    newItem = in.readUTF();
                System.out.println("->> Server: Info received add action item now ..");
                out.writeBoolean(Server.dataBaseServer.addActionItem(n, newItem, user));
                System.out.println("->> Server: New action item added with sucess ..");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Server: Adding new actionItem " + e.getMessage());
                    return;
                }
            }
        }

    }

    public void replySizeOfTodo() {
        boolean sucess = false;
        System.out.println("\n->> Server: Received request to send number of action itens of USER " + user);
        while (sucess == false) {
            try {
                out.write(Server.dataBaseServer.getSizeOfTodo(user));
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("*** Server: Sending number of action itens of USER " + user);
                    return;
                }
            }
        }
    }

    public void replyActionItemsFromUser() {
        boolean sucess = false;
        System.out.println("\n->> Server: Received request to send action of USER: " + user);

        while (sucess == false) {
            try {
                System.out.println("->> Server: Sending actions of " + user);
                out.writeUTF(Server.dataBaseServer.getActionItemFromUser(user));
                System.out.println("->> Server: actions send with sucess ");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Sending actionItens of USER: " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replySetActionAsDone() {
        System.out.println("\n->> Server: Received request of " + user + " to complete a action..");
        int n = -1;
        boolean reply = false;
        boolean readedReply = false;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("->> Server: Waiting for action number..");
                if (n == -1)
                    n = in.read();
                System.out.println("->> Server: Waiting for USER to decline or accept..");
                if (readedReply == false) {
                    reply = in.readBoolean();
                    readedReply = true;
                }
                if (reply) {
                    out.writeBoolean(Server.dataBaseServer.setActionAsCompleted(user, n));
                    System.out.println("->> Server: Action set as completed with sucess");
                } else {
                    out.writeBoolean(false);
                    System.out.println("->> Server: Operation canceled by USER");
                }
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Replying to message: " + e.getMessage());
                    return;
                }
            }
        }


    }

    public void replyActionItensFromMeeting() {
        System.out.println("\n->> Server: Received request to send action itens from a meeting by " + user);
        int n = -1;
        boolean sucess = false;
        while (sucess == false) {
            try {
                out.writeBoolean(true);
                System.out.println("->> Server: Wainting for info meeting...");
                if (n == -1) {
                    n = in.read();
                }
                System.out.println("->> Server: Sending agenda itens of meeting.. ");
                out.writeUTF(Server.dataBaseServer.getActionItensFromMeeting(n, user));
                System.out.println("->> Server Info send with sucess..");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Receiving meeting number for action item... " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyMessagesFromAgendaItem() {
        int numAgendaItem = -1;
        int n = -1;
        int flag;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("\n->> Server: Received request send messages from agenda item ..");
                System.out.println("->> Server: Waiting for the info of agenda item to send messages..");
                if (n == -1){
                    n = in.read();
                    System.out.println("n: "+n);
                }
                if (numAgendaItem == -1){
                    numAgendaItem = in.read();
                    System.out.println("numagedaaitem: "+numAgendaItem);
                }
                System.out.println("->> Server: Info received sending messages now ..");
                out.writeUTF(Server.dataBaseServer.getMessagesFromAgendaItem(n, numAgendaItem, user));
                System.out.println("->> Server: Agenda item messages sended with sucess ..");
                flag = Server.dataBaseServer.addClientToChat(n, numAgendaItem, user);
                if (flag == 0)
                    return;
                ArrayList<Connection> clientsOnChat = new ArrayList<Connection>();
                for (Connection userOn : Server.onlineUsers) {
                    if (Server.dataBaseServer.userOnChat(n, numAgendaItem, userOn.user)) {
                        clientsOnChat.add(userOn);
                    }
                }
                for (Connection outs : clientsOnChat) {
                    System.out.println("->> Server: Broadcasting message to " + outs.user);
                    outs.out.writeUTF("\n>>: \n*** " + user + " as entered the chat \n***");
                }

                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Server: Adding new agendaItem " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyAddMessageToAgendaItem() {
        int numAgendaItem = -1;
        int n = -1;
        boolean sucess = false;
        String messageReaded = null;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.MONTH, 1);
        while (sucess == false) {
            String messageAdded = now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE) +
                    " -> " + user + ": ";
            ArrayList<Connection> clientsOnChat = new ArrayList<Connection>();
            try {
                System.out.println("\n->> Server: Received request add messages to agenda item ..");
                System.out.println("->> Server: Waiting for the info of meeting to add message..");
                if (n == -1)
                    n = in.read();
                System.out.println("->> Server: Waiting for the info of agenda to add message..");
                if (numAgendaItem == -1)
                    numAgendaItem = in.read();
                System.out.println("->> Server: Info of agenda item received waiting for message now ..");
                if (messageReaded == null)
                    messageReaded = in.readUTF();
                System.out.println("->> Server: Message received, adding message now..");
                messageAdded += messageReaded;
                if (Server.dataBaseServer.addMessage(n, numAgendaItem, user, messageAdded.concat("\n"))) {
                    System.out.println("->>>> " + Server.dataBaseServer.getUsersOnChat(n, numAgendaItem, user));
                    for (Connection userOn : Server.onlineUsers) {
                        if (Server.dataBaseServer.userOnChat(n, numAgendaItem, userOn.user)) {
                            clientsOnChat.add(userOn);
                        }
                    }
                    for (Connection outs : clientsOnChat) {
                        System.out.println("->> Server: Broadcasting message to " + outs.user);
                        outs.out.writeUTF(messageAdded.concat("\n"));
                    }
                } else
                    System.out.println("->> Server: Message sended with sucess ..");
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Server: Adding new message " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyRemoveFromChat() {
        int n = -1;
        int numAgendaItem = -1;
        boolean sucess = false;
        while (sucess == false) {
            try {
                out.writeUTF("");
                System.out.println("\n->> Server: leaving chat ..");
                System.out.println("->> Server: leaving chat n meeting..");
                if (n == -1)
                    n = in.read();
                System.out.println("->> Server: leaving chat n Agenda..");
                if (numAgendaItem == -1)
                    numAgendaItem = in.read();
                System.out.println("->> Server: leaving chat 2..");
//                out.writeBoolean(Server.dataBaseServer.removeClientFromChat(n, numAgendaItem, user));
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** removing from chat" + e.getMessage());
                    return;
                }
            }
        }

    }

    public void replyMessageHistoryFromAgendaItem() {
        int numAgendaItem = -1;
        int n = -1;
        boolean sucess = false;
        while (sucess == false) {
            try {
                System.out.println("\n->> Server: Received request send history of messages from agenda item ..");
                System.out.println("->> Server: Waiting for the info of agenda item to send messages..");
                if (n == -1)
                    n = in.read();
                if (numAgendaItem == -1)
                    numAgendaItem = in.read();
                System.out.println("->> Server: Info received,sending messages now ..");
                out.writeUTF(Server.dataBaseServer.getMessagesHistoryFromAgendaItem(n, numAgendaItem, user));
                sucess = true;
                System.out.println("->> Server: Agenda item messages sended with sucess ..");
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Sending pass Meeting history chat " + e.getMessage());
                    return;
                }
            }
        }
    }

    public void replyInviteToMeeting() {
        boolean sucess = false;
        int n = -1;
        String invitedUser = null;
        while (sucess == false) {
            try {
                if (n == -1)
                    n = in.read();
                if (invitedUser == null)
                    invitedUser = in.readUTF();
                out.writeBoolean(Server.dataBaseServer.inviteUserToMeeting(n, invitedUser,user));
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n*** Inviting to meeting " + e.getMessage());
                    return;
                }
            }
            sucess = true;
        }
    }

    //TODO put sout's
    public void replyIfUserExists() {
        String name = null;
        boolean sucess = false;
        while (sucess == false) {
            try {
                if (name == null)
                    name = in.readUTF();
                out.writeBoolean(Server.dataBaseServer.findUser(name) != null);
                sucess = true;
            } catch (IOException e) {
                if (e.getCause().toString().equals(Server.rmiConnectionException)) {
                    try {
                        Server.connectToRmi();
                    } catch (IOException e1) {
                        System.out.println("*** Reconnecting to rmiServer" + e1.getMessage());
                    }
                } else {
                    System.out.println("\n *** testing if user exists" + e.getMessage());
                    return;
                }
            }

        }

    }
}