import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Diogo on 14/10/2014.
 */
public class Client {
    public static Scanner SC = new Scanner(System.in);
    public static String USERNAME, PASSWORD;
    public static Socket SOCKET;
    public static int SERVERSOCKET;
    public static String HOSTNAME;
    public static DataInputStream IN;
    public static DataOutputStream OUT;

    public static void main(String[] args) {
//        String username = "", password;

        USERNAME = null;
        PASSWORD = null;
        SOCKET = null;
        SERVERSOCKET = 6000;
        HOSTNAME = "localhost";
//        USER = new User("Jon Snow", "root", "dragonstone", new Date("12/1/2110"), 212233, "stannisthemannis@kingoftheandals.wes");
//        int tries = 0;
//        //Login
//        while ((username = login()) == null) {
//            System.OUT.println("Invalid username/password please try again (" + (2 - tries) + " left)");
//            tries++;
//            if (tries == 3) {
//                System.OUT.println("You execeded maximum number of tries(3)");
//                System.exit(0);
//            }
//        }
        //System.OUT.println("\n\n\n\n\n\n\n\n\n\n\n");
//        System.OUT.println("Welcome " + USER.getUserName());


        connect();

        //mainMenu();
        //chat();

    }

    public static void connect() {
        try {
            SOCKET = new Socket(HOSTNAME, SERVERSOCKET);
            IN = new DataInputStream(SOCKET.getInputStream());
            OUT = new DataOutputStream(SOCKET.getOutputStream());
            loginMenu();
        } catch (IOException e) {
            connect();
        } finally {
            if (SOCKET != null)
                try {
                    SOCKET.close();
                } catch (IOException e) {
                }
        }

    }

//    private static String login() {
//        User teste = new User("Zeih", "root", "Rua da Guinola", new Date("10/10/1990"), 912345678, "zeih@guinola.pt");
//
//        Scanner SC = new Scanner(System.IN);
//        System.OUT.print("Username: ");
//        String username = SC.next();
//        System.OUT.print("Password: ");
//        String password = SC.next();
//
//        if (username.equals(teste.getUserName()) && password.equals(teste.getPassWord())) {
//            return username;
//
//        } else {
//            return null;
//        }
//    }


    //-------------------------------------- MENUS

    public static void loginMenu() {
        int option;
        String name, password, getback;
        boolean logIn = false;
        if (USERNAME == null) {
            do {
                System.out.println("\n\n\n\n\n");
                System.out.println("1-> Login");
                System.out.println("2-> Register");
                System.out.println("0-> GFO");
                System.out.println("choose an option: ");
                option = SC.nextInt();
                SC.nextLine();
                if (option == 0) {
                    System.exit(0);
                }
                switch (option) {
                    case 1: {
                        do {
                            System.out.println("User name: ");

                            name = SC.nextLine();
                            System.out.println("PassWord: ");
                            password = SC.nextLine();
                            try {
                                OUT.writeUTF(name + "," + password);
                                logIn = IN.readBoolean();
                            } catch (IOException e) {
                                connect();
                            }
                            if (!logIn) {
                                do {

                                    System.out.println("Logn failed, please try again? (y/n)\n");
                                    getback = SC.nextLine();
                                } while (!getback.equalsIgnoreCase("y") && !getback.equalsIgnoreCase("n"));

                                if (getback.equalsIgnoreCase("n")) {
                                    break;
                                }
                            }
                        } while (!logIn);
                        USERNAME = name;
                        PASSWORD = password;
                        mainMenu();

                    }
                    break;
                    case 2: {
                        registerNewClient();

                    }
                    break;
                    default: {
                        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                        System.out.println("Wrong option");
                    }
                    break;
                }
            } while (true);
        } else {
            try {
                OUT.writeUTF(USERNAME + "," + PASSWORD);
            } catch (IOException e) {
                connect();
            }
        }
    }

    public static void mainMenu() {
        int option;
        System.out.println("\n\n\n\n\n");
        do {

            System.out.println("Main Menu");
            System.out.println("1-> Meetings");
            System.out.println("2-> Messages (" + requestNumberOfMessegesToRead() + " new messages)");
            System.out.println("3-> TODO list (" + requestSizeToDo() + " actions to be done)");
            System.out.println("0-> GFO!");
            System.out.print("Choose option: ");
            option = SC.nextInt();
            switch (option) {
                case 0:
                    System.exit(0);
                case 1: {
                    subMenuMeetings();
                }
                break;
                case 2: {
                    subMenuMessages();
                }
                break;
                case 3: {
                    System.out.println();
                    subMenuTodo();
                }
                break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }

        }
        while (true);
    }

    public static void subMenuMessages() {
        int optUm, size;
        String dec = "";
        boolean aux = false;
        String options = requestMessages();
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display all messages
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optUm = SC.nextInt();
        } while (optUm < 0 || optUm > size);
        do {
            if (optUm == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Resume from message " + optUm);
            System.out.println(requestResumeMesage(optUm));
            System.out.println("Do you accept this invite? (y/n)");
            dec = SC.next();
            dec = dec.toLowerCase();
            //reply
            if (dec.equals("y")) {
                aux = replyInvite(true);
            } else if (dec.equals("n")) {
                aux = replyInvite(false);
            }
            //response
            if (aux) {
                System.out.println("Invite accept with success!\n");
                break;
            } else {
                System.out.println("Invite not accepted...\n");
                break;
            }
        } while (true);
    }

    public static void subMenuMeetings() {
        int option;
        do {
            System.out.println("\n\n\n");
            System.out.println("Menu Meetings");
            System.out.println("1-> Create new meeting");
            System.out.println("2-> Check upcoming meetings");
            System.out.println("3-> Check current Meetings");
            System.out.println("4-> Check past meetings");
            System.out.println("0-> Back");
            System.out.print("Choose option: ");
            option = SC.nextInt();
            if (option == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (option) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("\nCreate new meeting: ");
                    creatNewMeeting();
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    SubMenuUpcomingMeetings();
                }
                break;
                case 3: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    SubMenuCurrentMeetings();
                }
                break;
                case 4: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    SubMenupPastMeetings();
                }
                break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        } while (true);
    }

    public static void SubMenuUpcomingMeetings() {
        int size, optUm, optAi;
        System.out.println("All upcoming meetings: ");
        String options = requestUpcomingMeetings();
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all upcoming meetings
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optUm = SC.nextInt();
        } while (optUm < 0 || optUm > size);
        do {
            if (optUm == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Resume from meeting " + optUm);
            System.out.println("\n" + requestResumeUpcumingMeeting(optUm)); // resume of chosen meeting
            System.out.println("Options for meeting " + optUm);
            System.out.println("1-> Consult Agenda Items");
            System.out.println("2-> Add items to agenda");
            System.out.println("3-> Modify items in agenda");
            System.out.println("4-> Delete items from agenda");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            optAi = SC.nextInt();
            if (optAi == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (optAi) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Consult Agenda Items: ");
                    SubMenuConsultAgendaItemsUM(optUm);
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    addItemstoAgenda(optUm);
                }
                break;
                case 3: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Modify items IN agenda: ");
                    subMenuModifyAgendaItem(optUm);
                }
                break;
                case 4: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Delete items from agenda: ");
                    subMenuDeleteItemstFromAgenda(optUm);
                }
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        } while (true);
    }

    public static void SubMenuCurrentMeetings() {
        int size, optMeeting, optAi;
        System.out.println("All Current meetings: ");
        String options = requestCurrentMeetings();
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all current meetings
            System.out.println("\n0-> Back");
            System.out.print("Choose an option: ");
            optMeeting = SC.nextInt();
        } while (optMeeting < 0 || optMeeting > size);
        do {
            if (optMeeting == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Resume from meeting " + optMeeting);
            System.out.println("\n" + requestResumeCurrentMeetings(optMeeting) + "\n"); // resume of chosen meeting
            System.out.println("\nOptions for meeting " + optMeeting);
            System.out.println("1-> Consult/modify/discuss Agenda Items");
            System.out.println("2-> Add new action Item");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            optAi = SC.nextInt();
            if (optAi == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (optAi) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Consult Agenda Items: ");
                    SubMenuConsultAgendaItemsCM(optMeeting);
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Add new Action Item: ");
                    addNewActionItem(optMeeting);
                }
                break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        } while (true);
    }

    public static void SubMenuConsultAgendaItemsUM(int opt) {
        String options = requestAgendaItemsFromUpComingMeeting(opt);
        do {
            System.out.println(options); //display name of all agenda items
            System.out.println("Press any key to return!");
            SC.next();
            SC.nextLine();
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
            break;
        } while (true);
    }

    public static void SubMenuConsultAgendaItemsPM(int opt) {
        int optUm, size, opt2;
        String options = requestAgendaItemsFromPastMeeting(opt);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all agen1da items
            System.out.println("0-> Back");
            System.out.print("Choose an item to open chat: ");
            optUm = SC.nextInt();
        } while (optUm < 0 || optUm > size);
        System.out.println(resquestChatFromItemPastMeeting(opt, optUm));
        System.out.println("Press any key to continue...");
        SC.next();
        SC.nextLine();
    }

    public static void SubMenuConsultAgendaItemsCM(int optMeeting) {
        int optItem, opt2, size;
        String options = requestAgendaItemsFromCurrentMeetings(optMeeting);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options);
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optItem = SC.nextInt();
        } while (optItem < 0 || optItem > size);
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        do {
            if (optItem == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Options for Agenda item " + optItem);
            System.out.println("1-> Open chat");
            System.out.println("2-> Add key decsions");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            opt2 = SC.nextInt();
            if (opt2 == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (opt2) {
                case 1: {
                    System.out.println("\n\n\n");
                    System.out.println(requestMessagesFromAgendaItem(optMeeting, optItem));
                    try {
                        chat(optMeeting, optItem);
                    } catch (IOException e) {
                        connect();
                    }
                    requestLeaveChat(optMeeting, optItem);
                }
                break;
                case 2: {
                    System.out.println("Add/modify key decision");
                    addNewKeyDecisionToAgendaitem(optMeeting, optItem);
                }
                break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        } while (true);
    }

    public static void SubMenupPastMeetings() {
        int size, optUm, optAi;
        System.out.println("All past meetings: ");
        String options = requestPastMeetings();
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all past meetings
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optUm = SC.nextInt();
        } while (optUm < 0 || optUm > size);
        do {
            if (optUm == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Resume from meeting " + optUm);

            System.out.println("\n" + requestResumePastMeeting(optUm) + "\n"); // resume of chosen meeting
            System.out.println("\nOptions from meeting " + optUm);
            System.out.println("1-> Consult Agenda Items");
            System.out.println("2-> Consult Action Items");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            optAi = SC.nextInt();
            if (optAi == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (optAi) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Consult Agenda Items: ");
                    //System.OUT.println("Under construction... sorry :( \n\n");
                    SubMenuConsultAgendaItemsPM(optUm);
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Consult Action Items: ");
                    System.out.println(requestActionItemsPastMeeting(optUm));
                    System.out.println("Press any key to continue...");
                    SC.next();
                    SC.nextLine();
                }
                break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        } while (true);
    }

    public static void subMenuModifyAgendaItem(int optMeeting) {
        int optItemtoModify, size;
        String options = requestAgendaItemsFromUpComingMeeting(optMeeting);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all agenda items
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optItemtoModify = SC.nextInt();
            if (optItemtoModify == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                return;
            } else if (optItemtoModify < 0 || optItemtoModify > size) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Wrong option, try again");
            }
        } while (optItemtoModify < 0 || optItemtoModify > size);

        modifyNameFromAgendaItem(optMeeting, optItemtoModify);
    }

    public static void subMenuTodo() {
        int size, optActionItem, optAux;
        System.out.println("All my actions to be done: ");
        String options = requestActionItemsFromUser();
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display all action items
            System.out.println("\n0-> Back");
            System.out.print("Choose an option: ");
            optActionItem = SC.nextInt();
        } while (optActionItem < 0 || optActionItem > size);
        do {
            boolean aux = false;
            if (optActionItem == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("1-> Mark as done");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            optAux = SC.nextInt();
            if (optAux == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (optAux) {
                case 1: {
                    String dec;

                    do {
                        System.out.println("Mark as done? (y/n)");
                        dec = SC.next();
                        dec = dec.toLowerCase();
                        //reply
                    } while (!dec.equals("y") && !dec.equals("n"));
                    if (dec.equals("y")) {
                        aux = requestMarkActionAsDone(optActionItem, true);
                    } else if (dec.equals("n")) {
                        aux = requestMarkActionAsDone(optActionItem, false);
                    }


                    //response
                    if (aux) {
                        System.out.println("Invite accept with success!\n");
                        return;
                    } else {
                        System.out.println("Invite not accepted...\n");
                        return;
                    }
                }
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
            if (aux) break;
        } while (true);
    }


    //-------------------------------------- REQUEST/REPLY

    public static int requestNumberOfMessegesToRead() {
        try {
            OUT.write(10);
            return IN.read();
        } catch (IOException e) {
            connect();
            return -1;
        }
    }

    public static boolean requestServerNewMeeting(String request) {
        boolean aceptSignal;
        try {
            OUT.write(1);
        } catch (Exception e) {
            connect();
            return false;
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.writeUTF(request);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static String requestUpcomingMeetings() {
        String result = "";
        try {
            OUT.write(2);
            result = IN.readUTF();
        } catch (Exception e) {
            connect();
        }
        return result;
    }

    public static String requestPastMeetings() {
        String result = "";
        try {
            OUT.write(3);
            result = IN.readUTF();
        } catch (Exception e) {
            connect();
        }
        return result;
    }

    public static String requestMessages() {
        String result = "";
        try {
            OUT.write(8);
            result = IN.readUTF();
        } catch (Exception e) {
            connect();
        }
        return result;
    }

    public static String requestAgendaItemsFromUpComingMeeting(int opt) {
        boolean aceptSignal;
        String result = "";
        try {
            OUT.write(6);
        } catch (Exception e) {
            connect();
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(opt);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static String requestAgendaItemsFromPastMeeting(int opt) {
        boolean aceptSignal;
        String result = "";
        try {
            OUT.write(7);
        } catch (Exception e) {
            connect();
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(opt);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static String requestAgendaItemsFromCurrentMeetings(int opt) {
        boolean aceptSignal;
        String result = "";
        try {
            OUT.write(21);
        } catch (Exception e) {
            connect();
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(opt);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static String requestResumeUpcumingMeeting(int opt) {
        String result = "";
        try {
            OUT.write(4);
        } catch (Exception e) {
            connect();
        }
        try {
            IN.readBoolean();
            OUT.write(opt);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static String requestResumePastMeeting(int opt) {
        String result = "";
        try {
            OUT.write(5);

        } catch (Exception e) {
            connect();
        }
        try {
            IN.readBoolean();
            OUT.write(opt);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static String requestResumeMesage(int opt) {
        String result = "";
        try {
            OUT.write(9);
        } catch (Exception e) {
            connect();
        }
        try {
            IN.readBoolean();
            OUT.write(opt);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static String requestActionItemsPastMeeting(int opt) {
        boolean aceptSignal;
        String result = "";
        try {
            OUT.write(22);
        } catch (Exception e) {
            connect();
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(opt);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static String resquestChatFromItemPastMeeting(int optMeeting, int optItem) {
        boolean aceptSignal;
        String result = "";
        try {
            OUT.write(27);
        } catch (Exception e) {
            connect();
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(optMeeting);
            aceptSignal = IN.readBoolean();
            OUT.write(optItem);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
//        return "Conversation: \n Stannis-> Davos give me my magic sword! \n2-> Davos-> here yougo you're grace... melessiandre as bee excpteing you yoy're grace";
    }

    public static boolean replyInvite(boolean decision) {
        try {
            OUT.writeBoolean(decision);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static boolean requestAddItemToAgenda(int opt, String itemToadd) {
        boolean aceptSignal;
        try {
            OUT.write(11);
        } catch (Exception e) {
            connect();
            return false;
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(opt);
            aceptSignal = IN.readBoolean();
            OUT.writeUTF(itemToadd);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static boolean requestDeleteItemToAgenda(int optMeetenig, int itemToDelete) {
        boolean aceptSignal;
        try {
            OUT.write(12);
        } catch (Exception e) {
            connect();
            return false;
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(optMeetenig);
            aceptSignal = IN.readBoolean();
            OUT.write(itemToDelete);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static boolean requestMofifyItemToAgenda(int optMeeting, int optItemToModify, String newAgendaItem) {
        boolean aceptSignal;
        try {
            OUT.write(13);
        } catch (Exception e) {
            connect();
            return false;
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(optMeeting);
            aceptSignal = IN.readBoolean();
            OUT.write(optItemToModify);
            aceptSignal = IN.readBoolean();
            OUT.writeUTF(newAgendaItem);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static boolean requestAddKeyDecisionToAgendaItem(int optMeeting, int optItemToModify, String newKeyDecision) {
        boolean aceptSignal;
        try {
            OUT.write(14);
        } catch (Exception e) {
            connect();
            return false;
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(optMeeting);
            aceptSignal = IN.readBoolean();
            OUT.write(optItemToModify);
            aceptSignal = IN.readBoolean();
            OUT.writeUTF(newKeyDecision);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static boolean requestAddNewAcionItem(int opt, String newActionItem) {
        boolean aceptSignal;
        try {
            OUT.write(15);
        } catch (Exception e) {
            connect();
            return false;
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.write(opt);
            aceptSignal = IN.readBoolean();
            OUT.writeUTF(newActionItem);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static int requestSizeToDo() {
        try {
            OUT.write(16);
            return IN.read();
        } catch (IOException e) {
            connect();
            return -1;
        }
    }

    public static String requestActionItemsFromUser() {
        String result = "";
        try {
            OUT.write(17);
        } catch (Exception e) {
            connect();
        }
        try {
            result = IN.readUTF();
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static boolean requestMarkActionAsDone(int optAction, boolean decision) {
        boolean success;
        try {
            OUT.write(18);
            success = IN.readBoolean();
            OUT.write(optAction);
            success = IN.readBoolean();
            OUT.writeBoolean(decision);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static String requestCurrentMeetings() {
        String result = "merda";
        try {
            OUT.write(19);
            result = IN.readUTF();
        } catch (Exception e) {
            connect();
        }
        return result;
    }

    public static String requestResumeCurrentMeetings(int optCurrentMeeting) {
        String result = "";
        try {
            OUT.write(20);
        } catch (Exception e) {
            connect();
        }
        try {
            IN.readBoolean();
            OUT.write(optCurrentMeeting);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static String requestMessagesFromAgendaItem(int optCurrentMeeting, int optItem) {
        String result = "";
        try {
            OUT.write(23);
        } catch (Exception e) {
            connect();
        }
        try {
            IN.readBoolean();
            OUT.write(optCurrentMeeting);
            IN.readBoolean();
            OUT.write(optItem);
            result = IN.readUTF(IN);
        } catch (IOException e) {
            connect();
        }
        return result;
    }

    public static boolean requestIfClientExists(String userName) {
        boolean aceptSignal;
        try {
            OUT.write(25);
        } catch (Exception e) {
            connect();
            return false;
        }
        try {
            aceptSignal = IN.readBoolean();
            OUT.writeUTF(userName);
            return IN.readBoolean();
        } catch (IOException e) {
            connect();
            return false;
        }
    }

    public static void requestLeaveChat(int optCurrentMeeting, int optItem) {
        try {
            OUT.write(26);
        } catch (Exception e) {
            connect();
        }
        try {
            IN.readBoolean();
            OUT.write(optCurrentMeeting);
            IN.readBoolean();
            OUT.write(optItem);
            IN.readBoolean();
        } catch (IOException e) {
            connect();
        }
    }


    //-------------------------------------- AUXILIAR FUNCTIONS MENU

    public static void registerNewClient() {
        String userName, passWord, address, dob, phoneNumer, mail, finalInfo = "";
        boolean testName = false, testDob = false;
        System.out.println("Register new USER\n");
        SC.nextLine();
        do {
            System.out.println("Insert USER name:");
            userName = SC.nextLine();
            try {
                OUT.writeUTF(userName);
                testName = IN.readBoolean();
            } catch (IOException e) {
                connect();
            }
            if (testName) {
                System.out.println("Name already exists, try again\n");
            }
        } while (testName);
        System.out.println("PassWord: ");
        passWord = SC.nextLine();
        System.out.println("Address: ");
        address = SC.nextLine();
        do {

            System.out.println("Date of birthday (dd/mm/yyyy): ");
            dob = SC.nextLine();
            testDob = testDateOfBirthDay(dob);
            if (!testDob) {
                System.out.println("Wrong format, try again\n");
            }
        } while (!testDob);
        System.out.println("Phone number: ");
        phoneNumer = SC.nextLine();
        System.out.println("Email: ");
        mail = SC.nextLine();

        finalInfo = userName + "," + passWord + "," + address + "," + dob + "," + phoneNumer + "," + mail;
        boolean success = false;
        try {
            OUT.writeUTF(finalInfo);
            success = IN.readBoolean();
        } catch (IOException e) {
            connect();
        }
        if (success) {
            USERNAME = userName;
            PASSWORD = passWord;
            mainMenu();
            System.out.println("Inserted wit success! ");
        } else {
            System.out.println("Not inserted with success...");
        }
    }

    public static void chat(int optMeeting, int optagendaItem) throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader bfr = new BufferedReader(isr);
        ReadingThread rt = new ReadingThread();
        String textRecived = "";
        System.out.println("Type '.quit' to leave");
        while (true) {
            System.out.print("\n>>: ");
            try {
                textRecived = bfr.readLine();
            } catch (Exception e) {
            }
            if (textRecived.equalsIgnoreCase(".quit")) {
                rt.kill();
                return;
            }
            OUT.write(24);
//            IN.readBoolean();
            OUT.write(optMeeting);
//            IN.readBoolean();
            OUT.write(optagendaItem);
//            IN.readBoolean();
            OUT.writeUTF(textRecived);
//            IN.readBoolean();
        }
    }

    public static void creatNewMeeting() {
        String responsible, desireOutCome, local, title, date = "", guests = null, agendaItems, request;
        int duration;
        responsible = USERNAME;
        SC.nextLine();
        System.out.print("Title: ");
        title = SC.nextLine();
        System.out.print("Desire outcome: ");
        desireOutCome = SC.nextLine();
        System.out.print("Local: ");
        local = SC.nextLine();

        boolean dateTest = false;
        boolean pastDate = false;
        do {
            System.out.print("Date (dd/mm/yy hh:mm): ");
            date = SC.nextLine();
            dateTest = myDateTest(date);
            pastDate = checkPastDate(date);
            if (!dateTest) {
                System.out.println("Wrong format, try again (min 0h:30m / max 2 years)");
            } else if (!pastDate) {
                System.out.println("Can't creat a meeting IN the past, try again");
            }
        } while (!dateTest || !pastDate);
        date = date.replaceAll(" ", ",");

        boolean userTest = false;
        do {
            System.out.print("Guests (g1,g2,...): ");
            guests = SC.nextLine();
            userTest = testIfUserNamesExists(guests);
            if (userTest == false) {
                System.out.println("One or more USER names do not exist, try again");
            }
        } while (!userTest);
        if (guests == null)
            guests = "none";

        System.out.print("agendaItems (ai1,ai2,...): ");
        agendaItems = SC.nextLine();
        System.out.print("Duration IN minutes: ");
        duration = SC.nextInt();
        SC.nextLine();
        System.out.println();
        request = responsible + "-" + desireOutCome + "-" + local + "-" + title + "-" + date + "-" + guests + "-" + agendaItems + "-" + duration;
        boolean success = requestServerNewMeeting(request);
        if (success)
            System.out.println("Meeting successfully created!");
        else
            System.out.println("Error creating meeting...");
    }

    public static void addItemstoAgenda(int opt) {
        String itemToDiscuss;
        System.out.println("Add items to agenda: ");
        System.out.println("Item to discuss: ");
        SC.nextLine();
        itemToDiscuss = SC.nextLine();
        boolean success = requestAddItemToAgenda(opt, itemToDiscuss);
        if (success)
            System.out.println("Agenda item was added successfully!!");
        else
            System.out.println("Error adding Item to Agenda....");
    }

    public static void subMenuDeleteItemstFromAgenda(int optMeeting) {
        int optItemtoDelete, size;
        String options = requestAgendaItemsFromUpComingMeeting(optMeeting);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        options = options.replaceAll("Any other businness", "");
        do {
            for (int i = 0; i < size - 1; i++) {
                System.out.println(countOptions[i]);
            }
            System.out.println();
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optItemtoDelete = SC.nextInt();
            if (optItemtoDelete == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                return;
            } else if (optItemtoDelete < 0 || optItemtoDelete > size) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Wrong option, try again");
            }

        } while (optItemtoDelete < 0 || optItemtoDelete > size);

        String deleteConfirm = "";
        do {
            System.out.println("Delete this item? (y/n)");
            deleteConfirm = SC.next();
        } while (!deleteConfirm.equals("y") && !deleteConfirm.equals("n"));
        System.out.println("------------------");
        if (deleteConfirm.equals("y")) {
            boolean success = requestDeleteItemToAgenda(optMeeting, optItemtoDelete);
            if (success) {
                System.out.println("Agenda item was deleted successfully!!");
            } else {
                System.out.println("Error deleting Item from Agenda....");
            }
            System.out.println("Press any key to return ");
            SC.next();
        }
    }

    public static void modifyNameFromAgendaItem(int optMeeting, int optItemtoModify) {
        String NewItemToDiscuss;
        System.out.println("New item to discuss: ");
        SC.nextLine();
        NewItemToDiscuss = SC.nextLine();
        boolean success = requestMofifyItemToAgenda(optMeeting, optItemtoModify, NewItemToDiscuss);
        if (success)
            System.out.println("Agenda item was modified successfully!!");
        else
            System.out.println("Error changing Item fom Agenda....");
    }

    public static void addNewKeyDecisionToAgendaitem(int optMeeting, int optItemtoAddKeyDecision) {
        String NewKeyDecision;
        System.out.println("New key Decision: ");
        SC.nextLine();
        NewKeyDecision = SC.nextLine();
        boolean success = requestAddKeyDecisionToAgendaItem(optMeeting, optItemtoAddKeyDecision, NewKeyDecision);
        if (success) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("Key decision added successfully!!");
        } else {
            System.out.println("Error ading key decision....");
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    public static void addNewActionItem(int optMeeting) {
        String newActionItem = "", responsableUser = "";
        SC.nextLine();
        System.out.println("New ation Item: ");
        newActionItem = SC.nextLine();
        System.out.println("Responsable USER: ");
        responsableUser = SC.nextLine();
        boolean success = requestAddNewAcionItem(optMeeting, newActionItem + "-" + responsableUser);
        if (success)
            System.out.println("Agenda item was added successfully!!");
        else
            System.out.println("Error adding Item to Agenda....");

    }


    //-------------------------------------- TEST DATA INPUT
    public static boolean myDateTest(String date) { // receives "dd/mm/yyyy hh:mm"
        String localDate = date;
        localDate = localDate.replaceAll("/", ",");
        localDate = localDate.replaceAll(":", ",");
        localDate = localDate.replaceAll(" ", ",");
        String[] data = localDate.split(",");
        if (data.length != 5)
            return false;
        int day = Integer.parseInt(data[0]);
        int month = Integer.parseInt(data[1]);
        int year = Integer.parseInt(data[2]);
        int hours = Integer.parseInt(data[3]);
        int minuts = Integer.parseInt(data[4]);

        Date actualDate = new Date();
        int yearAux = actualDate.getYear() + 1902;
        if (hours < 0 || hours > 24)
            return false;
        else if (minuts < 0 || minuts > 59)
            return false;
        if (year < (yearAux - 100) || year > yearAux) {
            return false;
        } else {
            if (month < 1 || month > 12) {
                return false;
            } else {
                if (day < 1 || day > 31) {
                    return false;
                } else if (day > 28 && month == 2 && isLeapYear(year) == false) {
                    return false;
                } else if (day > 29 && month == 2 && isLeapYear(year) == true) {
                    return false;
                } else if (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isLeapYear(int ano) {
        return (ano % 4 == 0);
    }

    public static boolean checkPastDate(String date) {
        String localDate = date;
        localDate = localDate.replaceAll("/", ",");
        localDate = localDate.replaceAll(":", ",");
        localDate = localDate.replaceAll(" ", ",");
        String[] data = localDate.split(",");
        if (data.length != 5)
            return false;
        Calendar datetoTest = Calendar.getInstance();
        int day = Integer.parseInt(data[0]);
        int month = Integer.parseInt(data[1]);
        int year = Integer.parseInt(data[2]);
        int hours = Integer.parseInt(data[3]);
        int minuts = Integer.parseInt(data[4]);
        datetoTest.set(year, month, day, hours, minuts);

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.MONTH, 1);

        datetoTest.add(Calendar.MINUTE, -29);

        if (datetoTest.before(now)) {
            return false;
        }
        return true;
    }

    public static boolean testIfUserNamesExists(String guests) {
        guests = guests.replaceAll(", ", ",");
        String[] listOfGuests = guests.split(",");
        SC.next();
        for (String g : listOfGuests) {
            System.out.println("testing-> " + g);
            if (!requestIfClientExists(g)) {
                System.out.println("true");
                return false;
            }
        }
        System.out.println("false");
        return true;
    }

    public static boolean testDateOfBirthDay(String dob) {
        String localDate = dob;
        localDate = localDate.replaceAll("/", ",");
        String[] data = localDate.split(",");
        if (data.length != 3)
            return false;
        int day = Integer.parseInt(data[0]);
        int month = Integer.parseInt(data[1]);
        int year = Integer.parseInt(data[2]);

        Date actualDate = new Date();
        int yearAux = actualDate.getYear() + 1902;

        if (year < (yearAux - 100) || year > yearAux) {
            return false;
        } else {
            if (month < 1 || month > 12) {
                return false;
            } else {
                if (day < 1 || day > 31) {
                    return false;
                } else if (day > 28 && month == 2 && isLeapYear(year) == false) {
                    return false;
                } else if (day > 29 && month == 2 && isLeapYear(year) == true) {
                    return false;
                } else if (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11)) {
                    return false;
                }
            }
        }
        return true;
    }
}

class ReadingThread extends Thread {
    protected DataInputStream din;
    boolean isRunning;

    public ReadingThread() {
        this.din = Client.IN;
        isRunning = true;
        this.start();
    }

    public void run() {
        try {
            while (isRunning) {
                System.out.println(din.readUTF());
                System.out.print(">>: ");
            }
        } catch (IOException e) {
            Client.connect();
        }
    }

    public void kill() {
        isRunning = false;
    }
}
