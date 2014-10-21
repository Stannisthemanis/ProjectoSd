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
//    public static boolean mainServer;

    public static void main(String[] args) {
        try {
            createServer();
        } catch (IOException e) {
//            mainServer = false;
            System.out.println("*** Creating Server: " + e.getMessage());
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
        boolean connected = false;
        while (connected == false) {
            try {
                dataBaseServer = (RmiServerInterface) Naming.lookup("DataBase");
                connected = true;
            } catch (MalformedURLException e) {
                System.out.println("->> Server: Registing to rmiServer " + e.getMessage());
                connected = false;
            } catch (NotBoundException e) {
                System.out.println("->> Server: Registing to rmiServer " + e.getMessage());
                connected = false;
            } catch (RemoteException e) {
                System.out.println("->> Server: Registing to rmiServer " + e.getMessage());
                connected = false;
            }
        }
        System.out.println("->> Server: Connection to RmiServer ok...");

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
    String user;
    RmiServerInterface dataBaseServer;

    Connection(Socket cSocket, RmiServerInterface dataBaseServer) {
        try {
            this.clientSocket = cSocket;
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.dataBaseServer = dataBaseServer;
            this.user = dataBaseServer.findUser(in.readUTF()).getUserName();

            System.out.println("->> Server: " + user + " connected");
            Server.onlineUsers.add(this); //
            this.start();
        } catch (IOException e) {
            Server.onlineUsers.remove(this);
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
                    case 4:
                        replyInfoMeeting(1);
                        break;
                    case 5:
                        replyInfoMeeting(2);
                        break;
                    case 6:
                        replyAgendaItensFromMeeting(1);
                        break;
                    case 7:
                        replyAgendaItensFromMeeting(2);
                        break;
                    case 8:
                        replyUnreadMessages();
                        break;
                    case 9:
                        replyMessage();
                        break;
                    case 10:
                        replyNumberOfMessages();
                        break;
                    case 11:
                        replyAddAgendaItem();
                        break;
                    case 12:
                        replyRemoveAgendaItem();
                        break;
                    case 13:
                        replyModifyTitleAgendaItem();
                        break;
                    case 14:
                        replyAddKeyDecisionToAgendaItem();
                        break;
                    case 15:
                        replyAddActionItem();
                        break;
                    case 16:
                        replySizeOfTodo();
                        break;
                    case 17:
                        replyActionItemsFromUser();
                        break;
                    case 18:
                        replySetActionAsDone();
                        break;
                    case 19:
                        replyCheckCurrentMeetings();
                        break;
                    case 20:
                        replyInfoMeeting(3);
                        break;
                    case 21:
                        replyAgendaItensFromMeeting(3);
                        break;
                    case 22:
                        replyActionItensFromMeeting();
                        break;
                    case 23:
                        replyMessagesFromAgendaItem();
                        break;
                    case 24:
                        replyAddMessageToAgendaItem();
                        break;
                    case 25:
                        replyIfUserExists();
                        break;
                    case 26:
                        replyRemoveFromChat();
                        break;
                }
            }
        } catch (EOFException e) {
            System.out.println("*** Receiving request from client: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("*** Receiving request from client: " + e.getMessage());
        }

    }

    public void replyNewMeeting() {
        String newMeeting = null;
        try {
            System.out.println("->> Server: Received request from " + this.user + " to create new meeting");
            out.writeBoolean(true);
            System.out.println("->>Server: Waiting for meeting information");
            newMeeting = in.readUTF();
            System.out.println("->> Server: Information received");
            if (dataBaseServer.addNewMeeting(newMeeting) == true) {
                System.out.println("->> Server: New meeting created");
                out.writeBoolean(true);
            } else {
                out.writeBoolean(false);
                System.out.println("->> Server: Failed to create new meeting");
            }
        } catch (IOException e) {
            System.out.println("*** Reply new Meeting: " + e.getMessage());
        }
    }

    public void replyCheckUpcumingMeetings() {
        System.out.println("->> Server: Received request to send all upcuming meeting of " + this.user);
        try {
            System.out.println("->> Server: Sending all upcuming meeting of " + this.user);
            out.writeUTF(dataBaseServer.getUpcumingMeetings(user));
        } catch (IOException e) {
            System.out.println("*** Replying upcuming meeting: " + e.getMessage());
        }
    }

    public void replyCheckPassedMeetings() {
        System.out.println("->> Server: Received request to send all passed meeting of " + this.user);
        try {
            System.out.println("->> Server: Sending all passed meeting of " + this.user);
            out.writeUTF(dataBaseServer.getPassedMeetings(user));
        } catch (IOException e) {
            System.out.println("*** Replying upcuming meeting: " + e.getMessage());
        }
    }

    public void replyCheckCurrentMeetings() {
        System.out.println("->> Server: Received request to send all current meeting of " + this.user);
        try {
            System.out.println("->> Server: Sending all current meeting of " + this.user);
            out.writeUTF(dataBaseServer.getCurrentMeetings(user));
        } catch (IOException e) {
            System.out.println("*** Replying current meeting: " + e.getMessage());
        }
    }


    //flag 1- FutureMeeting 2- PassedMeeting
    public void replyInfoMeeting(int flag) {
        System.out.println("->> Server: Received request to send meeting information from " + this.user);
        int meeting;
        try {
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for info of requested meeting...");
            meeting = in.read();
            out.writeUTF(dataBaseServer.getMeetingInfo(flag, meeting, this.user));
        } catch (IOException e) {
            System.out.println("*** Replying to send info of meeting");
        }

    }

    //flag 1- FutureMeeting 2- PassedMeeting
    public void replyAgendaItensFromMeeting(int flag) {
        System.out.println("->> Server: Received request to send agenda itens from a meeting by " + user);
        int n;
        try {
            out.writeBoolean(true);
            System.out.println("->> Server: Wainting for info meeting...");
            n = in.read();
            System.out.println("->> Server: Sending agenda itens of meeting.. ");
            out.writeUTF(dataBaseServer.getAgendaItemFromMeeting(flag, n, user));
            System.out.println("->> Server Info send with sucess..");
        } catch (IOException e) {
            System.out.println("*** Receiving meeting number for agenda item... " + e.getMessage());
        }
    }

    public void replyUnreadMessages() {
        System.out.println("->> Server: Received request to send messages of user: " + user);
        try {
            System.out.println("->> Server: Sending messages of " + user);
            out.writeUTF(dataBaseServer.getMessagesByUser(user));
            System.out.println("->> Server: Messages send with sucess ");
        } catch (IOException e) {
            System.out.println("*** Sending messages: " + e.getMessage());
        }
    }

    public void replyMessage() {
        System.out.println("->> Server: Received request of " + user + " to respond to a message..");
        int n;
        boolean reply;
        try {
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for message number..");
            n = in.read();
            System.out.println("->> Server: Sending message resume..");
            out.writeUTF(dataBaseServer.getResumeOfMessage(user, n));
            System.out.println("->> Server: Waiting for user to decline or accept..");
            reply = in.readBoolean();
            out.writeBoolean(dataBaseServer.setReplyOfInvite(user, n, reply));
            System.out.println("->> Server: Answer received with sucess");
        } catch (IOException e) {
            System.out.println("*** Replying to message: " + e.getMessage());
        }

    }

    public void replyNumberOfMessages() {
        System.out.println("->> Server: Received request to send number of messages of user " + user);
        System.out.println("->> Server: Received request to send number of messages of user " + user);
        try {
            out.write(dataBaseServer.getNumberOfMessages(user));
        } catch (IOException e) {
            System.out.println("->> Server: Sending number of messages of user " + user);
        }
    }

    public void replyAddAgendaItem() {
        String newItem = "";
        int n;
        try {
            System.out.println("->> Server: Received request to add a new agenda item ..");
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for the info of the new agenda item ..");
            n = in.read();
            out.writeBoolean(true);
            newItem = in.readUTF();
            System.out.println("->> Server: Info received add agenda item now ..");
            out.writeBoolean(dataBaseServer.addAgendaItem(n, newItem, user));
            System.out.println("->> Server: New agenda item added with sucess ..");
        } catch (IOException e) {
            System.out.println("*** Server: Adding new agendaItem " + e.getMessage());
        }

    }

    public void replyRemoveAgendaItem() {
        int numAgendaItem;
        int n;
        try {
            System.out.println("->> Server: Received request to remove aagenda item ..");
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for the info of agenda item to remove..");
            n = in.read();
            out.writeBoolean(true);
            numAgendaItem = in.read();
            System.out.println("->> Server: Info received, removing agenda item now ..");
            out.writeBoolean(dataBaseServer.removeAgendaItem(n, numAgendaItem, user));
            System.out.println("->> Server: Agenda item removed with sucess ..");
        } catch (IOException e) {
            System.out.println("*** Server: Adding new agendaItem " + e.getMessage());
        }
    }

    public void replyModifyTitleAgendaItem() {
        int numAgendaItem;
        int n;
        try {
            System.out.println("->> Server: Received request to modify agenda item ..");
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for the info of agenda item to modify..");
            n = in.read();
            out.writeBoolean(true);
            numAgendaItem = in.read();
            System.out.println("->> Server: Info received Waiting for new agenda itemToDiscuss now ..");
            out.writeBoolean(true);
            out.writeBoolean(dataBaseServer.modifyTitleAgendaItem(n, numAgendaItem, in.readUTF(), user));
            System.out.println("->> Server: Agenda item changed with sucess ..");
        } catch (IOException e) {
            System.out.println("*** Server: Adding new agendaItem " + e.getMessage());
        }
    }

    public void replyAddKeyDecisionToAgendaItem() {
        int numAgendaItem;
        int n;
        try {
            System.out.println("->> Server: Received request to add key decision to agenda item ..");
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for the info of agenda item to modify..");
            n = in.read();
            out.writeBoolean(true);
            numAgendaItem = in.read();
            System.out.println("->> Server: Info received Waiting for key decision now ..");
            out.writeBoolean(true);
            out.writeBoolean(dataBaseServer.addKeyDecisionToAgendaItem(n, numAgendaItem, in.readUTF(), user));
            System.out.println("->> Server: Agenda item changed with sucess ..");
        } catch (IOException e) {
            System.out.println("*** Server: Adding new agendaItem " + e.getMessage());
        }
    }

    public void replyAddActionItem() {
        String newItem = "";
        int n;
        try {
            System.out.println("->> Server: Received request to add action item ..");
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for the info of the new action item ..");
            n = in.read();
            out.writeBoolean(true);
            newItem = in.readUTF();
            System.out.println("->> Server: Info received add action item now ..");
            out.writeBoolean(dataBaseServer.addActionItem(n, newItem, user));
            System.out.println("->> Server: New action item added with sucess ..");
        } catch (IOException e) {
            System.out.println("*** Server: Adding new actionItem " + e.getMessage());
        }

    }

    public void replySizeOfTodo() {
        System.out.println("->> Server: Received request to send number of action itens of user " + user);
        try {
            out.write(dataBaseServer.getSizeOfTodo(user));
        } catch (IOException e) {
            System.out.println("->> Server: Sending number of action itens of user " + user);
        }
    }

    public void replyActionItemsFromUser() {
        System.out.println("->> Server: Received request to send action of user: " + user);
        try {
            System.out.println("->> Server: Sending actions of " + user);
            out.writeUTF(dataBaseServer.getActionItemFromUser(user));
            System.out.println("->> Server: actions send with sucess ");
        } catch (IOException e) {
            System.out.println("*** Sending actionItens of user: " + e.getMessage());
        }
    }

    public void replySetActionAsDone() {
        System.out.println("->> Server: Received request of " + user + " to complete a action..");
        int n;
        boolean reply;
        try {
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for action number..");
            n = in.read();
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for user to decline or accept..");
            reply = in.readBoolean();
            if (reply) {
                out.writeBoolean(dataBaseServer.setActionAsCompleted(user, n));
                System.out.println("->> Server: Action set as completed with sucess");
            } else {
                out.writeBoolean(false);
                System.out.println("->> Server: Operation canceled by user");
            }
        } catch (IOException e) {
            System.out.println("*** Replying to message: " + e.getMessage());
        }


    }

    public void replyActionItensFromMeeting() {
        System.out.println("->> Server: Received request to send action itens from a meeting by " + user);
        int n;
        try {
            out.writeBoolean(true);
            System.out.println("->> Server: Wainting for info meeting...");
            n = in.read();
            System.out.println("->> Server: Sending agenda itens of meeting.. ");
            out.writeUTF(dataBaseServer.getActionItensFromMeeting(n, user));
            System.out.println("->> Server Info send with sucess..");
        } catch (IOException e) {
            System.out.println("*** Receiving meeting number for action item... " + e.getMessage());
        }
    }

    public void replyMessagesFromAgendaItem() {
        int numAgendaItem;
        int n;
        try {
            System.out.println("->> Server: Received request send messages from agenda item ..");
            out.writeBoolean(true);
            System.out.println("->> Server: Waiting for the info of agenda item to send messages..");
            n = in.read();
            out.writeBoolean(true);
            numAgendaItem = in.read();
            System.out.println("->> Server: Info received sending messages now ..");
            out.writeUTF(dataBaseServer.getMessagesFromAgendaItem(n, numAgendaItem, user));
            System.out.println("->> Server: Agenda item messages sended with sucess ..");
            dataBaseServer.addClientToChat(n, numAgendaItem, user);
            ArrayList<Connection> clientsOnChat = new ArrayList<Connection>();
            for (Connection userOn : Server.onlineUsers) {
                if (dataBaseServer.userOnChat(n, numAgendaItem, userOn.user)) {
                    clientsOnChat.add(userOn);
                }
            }
            for (Connection outs : clientsOnChat) {
                System.out.println("->> Server: Broadcasting message to " + outs.user);
                outs.out.writeUTF("\n>>: *** " + user + " as entered the chat ***");
            }

        } catch (IOException e) {
            System.out.println("*** Server: Adding new agendaItem " + e.getMessage());
        }
    }

    public void replyAddMessageToAgendaItem() {
        int numAgendaItem;
        int n;
        String messageReaded;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.MONTH, 1);
        String messageAdded = now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE) +
                " -> " + user + ": ";
        ArrayList<Connection> clientsOnChat = new ArrayList<Connection>();
        try {
            System.out.println("->> Server: Received request add messages to agenda item ..");
            System.out.println("->> Server: Waiting for the info of meeting to add message..");
            n = in.read();
            System.out.println("->> Server: Waiting for the info of agenda to add message..");
            numAgendaItem = in.read();
            System.out.println("->> Server: Info of agenda item received waiting for message now ..");
            messageReaded = in.readUTF();
            System.out.println("->> Server: Message received, adding message now..");
            messageAdded += messageReaded;
            if (dataBaseServer.addMessage(n, numAgendaItem, user, messageAdded.concat("\n"))) {
                for (Connection userOn : Server.onlineUsers) {
                    if (dataBaseServer.userOnChat(n, numAgendaItem, userOn.user)) {
                        clientsOnChat.add(userOn);
                    }
                }
                for (Connection outs : clientsOnChat) {
                    System.out.println("->> Server: Broadcasting message to " + outs.user);
                    outs.out.writeUTF(messageAdded.concat("\n"));
                }
            } else
                System.out.println("->> Server: Message sended with sucess ..");
        } catch (IOException e) {
            System.out.println("*** Server: Adding new message " + e.getMessage());
        }
    }

    public void replyRemoveFromChat() {
        int n, numAgendaItem;
        try {
            out.writeUTF("");
            System.out.println("->> Server: leaving chat ..");
            out.writeBoolean(true);
            System.out.println("->> Server: leaving chat n meeting..");
            n = in.read();
            out.writeBoolean(true);
            System.out.println("->> Server: leaving chat n Agenda..");
            numAgendaItem = in.read();
            System.out.println("->> Server: leaving chat 2..");
            out.writeBoolean(dataBaseServer.removeClientFromChat(n, numAgendaItem, user));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //TODO put sout's
    public void replyIfUserExists() {
        String name = "";
        try {
            out.writeBoolean(true);
            name = in.readUTF();
            out.writeBoolean(dataBaseServer.findUser(name) != null);
        } catch (IOException e) {
        }

    }
}