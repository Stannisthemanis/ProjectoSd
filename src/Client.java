import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Diogo on 14/10/2014.
 */
public class Client {
    public static Scanner sc = new Scanner(System.in);

    public static User admin;

    public static void main(String[] args) {
        String username = "", password;


        Socket socket = null;
        int ServerSocket = 6000;
        String hostname = "localhost";
        admin = new User("Jon Snow", "root", "dragonstone", new Date("12/1/2110"), 212233, "stannisthemannis@kingoftheandals.wes");
//        int tries = 0;
//        //Login
//        while ((username = login()) == null) {
//            System.out.println("Invalid username/password please try again (" + (2 - tries) + " left)");
//            tries++;
//            if (tries == 3) {
//                System.out.println("You execeded maximum number of tries(3)");
//                System.exit(0);
//            }
//        }
        //System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Welcome " + admin.getUserName());


        while (true) {
            try {
                socket = new Socket(hostname, ServerSocket);

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.writeUTF(admin.getUserName());
                // chat(in, out);

                mainMenu(in, out);
                //chat(in, out);
            } catch (UnknownHostException e) {
            } catch (EOFException e) {
            } catch (IOException e) {
            } finally {
                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
            }
        }
    }

    private static String login() {
        User teste = new User("Zeih", "root", "Rua da Guinola", new Date("10/10/1990"), 912345678, "zeih@guinola.pt");

        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Password: ");
        String password = sc.next();

        if (username.equals(teste.getUserName()) && password.equals(teste.getPassWord())) {
            return username;

        } else {
            return null;
        }
    }


    //-------------------------------------- MENUS

    public static void mainMenu(DataInputStream in, DataOutputStream out) {
        int option;
        System.out.println("\n\n\n\n\n\n\n\n\n");
        do {
            System.out.println("Main Menu");
            System.out.println("1-> Meetings");
            System.out.println("2-> Messages (" + requestNumberOfMessegesToRead(in, out) + " new messages)");
            System.out.println("3-> TODO list (" + requestSizeToDo(in, out) + " actions to be done)");
            System.out.println("0-> Leave");
            System.out.print("Choose option: ");
            option = sc.nextInt();
            switch (option) {
                case 0:
                    System.exit(0);
                case 1: {
                    subMenuMeetings(in, out);
                }
                break;
                case 2: {
                    subMenuMessages(in, out);
                }
                break;
                case 3: {
                    subMenuTodo(in, out);
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

    public static void subMenuMessages(DataInputStream in, DataOutputStream out) {
        int optUm, size;
        String dec = "";
        boolean aux = false;
        String options = requestMessages(in, out);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display all messages
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optUm = sc.nextInt();
        } while (optUm < 0 || optUm > size);
        do {
            if (optUm == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Resume from message " + optUm);
            System.out.println(requestResumeMesage(in, out, optUm));
            System.out.println("Do you accept this invite? (y/n)");
            dec = sc.next();
            dec = dec.toLowerCase();
            //reply
            if (dec.equals("y")) {
                aux = replyInvite(in, out, true);
            } else if (dec.equals("n")) {
                aux = replyInvite(in, out, false);
            }
            //response
            if (aux) {
                System.out.println("Invite accept with success!");
                break;
            } else {
                System.out.println("Invite not accepted...");
                break;
            }
        } while (true);
    }

    public static void subMenuMeetings(DataInputStream in, DataOutputStream out) {
        int option;
        do {
            System.out.println("\n\n\n");
            System.out.println("Menu Meetings");
            System.out.println("1-> Create new meeting");
            System.out.println("2-> Check upcoming meetings");
            System.out.println("3-> Current Meetings");
            System.out.println("4-> Check past meetings");
            System.out.println("0-> Back");
            System.out.print("Choose option: ");
            option = sc.nextInt();
            if (option == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (option) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("\nCreate new meeting: ");
                    creatNewMeeting(in, out);
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    SubMenuUpcomingMeetings(in, out);
                }
                break;
                case 3: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    SubMenuCurrentMeetings(in, out);
                }
                case 4: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    SubMenupPastMeetings(in, out);
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

    public static void SubMenuUpcomingMeetings(DataInputStream in, DataOutputStream out) {
        int size, optUm, optAi;
        System.out.println("All upcoming meetings: ");
        String options = requestUpcomingMeetings(in, out);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all upcoming meetings
            System.out.println("\n0-> Back");
            System.out.print("Choose an option: ");
            optUm = sc.nextInt();
        } while (optUm < 0 || optUm > size);
        do {
            if (optUm == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Resume from meeting " + optUm);
            System.out.println("\n" + requestResumeUpcumingMeeting(in, out, optUm) + "\n"); // resume of chosen meeting
            System.out.println("\nOptions for meeting " + optUm);
            System.out.println("1-> Consult Agenda Items");
            System.out.println("2-> Add items to agenda");
            System.out.println("3-> Modify items in agenda");
            System.out.println("4-> Delete items from agenda");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            optAi = sc.nextInt();
            if (optAi == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (optAi) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Consult Agenda Items: ");
                    SubMenuConsultAgendaItemsUM(in, out, optUm);
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    addItemstoAgenda(in, out, optUm);
                }
                break;
                case 3: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Modify items in agenda: ");
                    subMenuModifyAgendaItem(in, out, optUm);
                }
                break;
                case 4: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Delete items from agenda: ");
                    DeleteItemstFromAgenda(in, out, optUm);
                }
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        } while (true);
    }

    public static void SubMenuCurrentMeetings(DataInputStream in, DataOutputStream out) {
        int size, optMeeting, optAi;
        System.out.println("All Current meetings: ");
        String options = requestCurrentMeetings(in, out);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all current meetings
            System.out.println("\n0-> Back");
            System.out.print("Choose an option: ");
            optMeeting = sc.nextInt();
        } while (optMeeting < 0 || optMeeting > size);
        do {
            if (optMeeting == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Resume from meeting " + optMeeting);
            System.out.println("\n" + requestResumeCurrentMeetings(in, out, optMeeting) + "\n"); // resume of chosen meeting
            System.out.println("\nOptions for meeting " + optMeeting);
            System.out.println("1-> Consult Agenda Items"); 
            System.out.println("2-> Add new action Item");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            optAi = sc.nextInt();
            if (optAi == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (optAi) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Consult Agenda Items: ");
                    SubMenuConsultAgendaItemsCM(in, out, optMeeting);
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Add new Action Item: ");
                    addNewActionItem(in, out, optMeeting);
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

    public static void SubMenuConsultAgendaItemsUM(DataInputStream in, DataOutputStream out, int opt) {
        int optBack;
        String options = requestAgendaItemsFromUpComingMeeting(in, out, opt);
        do {
            System.out.println(options); //display name of all agenda items
            System.out.println("0-> Back");
            optBack=sc.nextInt();
            if(optBack!=0){
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
        }while(optBack!=0);
//        String[] countOptions = options.split("\n");
//        size = countOptions.length;
//        do {
//            System.out.println("0-> Back");
//            System.out.print("Choose an option: ");
//            optUm = sc.nextInt();
//        } while (optUm < 0 || optUm > size);
//        do {
//            if (optUm == 0) {
//                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
//                break;
//            }
//            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
//            System.out.println("Options for Agenda item " + optUm);
//            System.out.println("1-> Open chat");
//            System.out.println("0-> Back");
//            System.out.println("Choose an option: ");
//            opt2 = sc.nextInt();
//            if (opt2 == 0) {
//                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
//                break;
//            }
//            switch (opt2) {
//                case 1: {
//                    System.out.println("\n\n\n");
//                    System.out.println("Opening Chat... ");
//                    System.out.println("You will address Stannis the mannis by 'Your grace' or GFO!");
//                    System.out.println("Under construction... sorry :( \n\n");
//                    sc.next();
//                }
//                break;
//                default: {
//                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
//                    System.out.println("Wrong option");
//                }
//                break;
//            }
//        } while (true);
    }

    public static void SubMenuConsultAgendaItemsPM(DataInputStream in, DataOutputStream out, int opt) {
        int optUm, size, opt2;
        String options = requestAgendaItemsFromPastMeeting(in, out, opt);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all agenda items
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optUm = sc.nextInt();
        } while (optUm < 0 || optUm > size);
        System.out.println(resquestChatFromItemPastMeeting(in, out, optUm));
    }

    public static void SubMenuConsultAgendaItemsCM(DataInputStream in, DataOutputStream out, int optMeeting) {
        int optItem, opt2,size;
        String options = requestAgendaItemsFromCurrentMeetings(in, out, optMeeting);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optItem = sc.nextInt();
        } while (optItem < 0 || optItem > size);
        do {
            if (optItem == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("Options for Agenda item " + optItem);
            System.out.println("1-> Open chat");
            System.out.println("2-> Add key decsions");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            opt2 = sc.nextInt();
            if (opt2 == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (opt2) {
                case 1: {
                    System.out.println("\n\n\n");
                    System.out.println("Opening Chat... ");
                    System.out.println("You will address Stannis the mannis by 'Your grace' or GFO!");
                    System.out.println("Under construction... sorry :( \n\n");
                    sc.next();
                }
                break;
                case 2: {
                    System.out.println("Adding new ket decision");
                    addNewKeyDecisionToAgendaitem(in,out,optMeeting,optItem);
                }break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        } while (true);
    }

    public static void SubMenupPastMeetings(DataInputStream in, DataOutputStream out) {
        int size, optUm, optAi;
        System.out.println("All past meetings: ");
        String options = requestPastMeetings(in, out);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all past meetings
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optUm = sc.nextInt();
        } while (optUm < 0 || optUm > size);
        do {
            if (optUm == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("Resume from meeting " + optUm);

            System.out.println("\n" + requestResumePastMeeting(in, out, optUm) + "\n"); // resume of chosen meeting
            System.out.println("\nOptions from meeting " + optUm);
            System.out.println("1-> Consult Agenda Items");
            System.out.println("2-> Consult Action Items");
            System.out.println("0-> Back");
            System.out.println("Choose an option: ");
            optAi = sc.nextInt();
            if (optAi == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (optAi) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Consult Agenda Items: ");
                    //System.out.println("Under construction... sorry :( \n\n");
                    SubMenuConsultAgendaItemsPM(in, out, optUm);
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Consult Action Items: ");
                    System.out.println(requestActionItemsPastMeeting(in, out, optUm));
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

    public static void subMenuModifyAgendaItem(DataInputStream in, DataOutputStream out, int optMeeting) {
        int optItemtoModify, size;
        String options = requestAgendaItemsFromUpComingMeeting(in, out, optMeeting);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all agenda items
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optItemtoModify = sc.nextInt();
            if (optItemtoModify == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            } else if (optItemtoModify < 0 || optItemtoModify > size) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Wrong option, try again");
            }
        } while (optItemtoModify < 0 || optItemtoModify > size);

        int opt;
        do {
            System.out.println("1-> Modify name ");
            System.out.println("0-> Back");
            opt = sc.nextInt();
            if (opt == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (opt) {
                case 1: {
                    modifyNameFromAgendaItem(in, out, optMeeting, optItemtoModify);
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

    public static void subMenuTodo(DataInputStream in, DataOutputStream out) {
        int size, optActionItem, optAux;
        System.out.println("All my actions to be done: ");
        String options = requestActionItemsFromUser(in, out);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display all action items
            System.out.println("\n0-> Back");
            System.out.print("Choose an option: ");
            optActionItem = sc.nextInt();
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
            optAux = sc.nextInt();
            if (optAux == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            switch (optAux) {
                case 1: {
                    String dec;

                    do {
                        System.out.println("Mark as done? (y/n)");
                        dec = sc.next();
                        dec = dec.toLowerCase();
                        //reply
                    } while (dec != "y" && dec != "n");
                    if (dec.equals("y")) {
                        aux = requestMarkActionAsDone(in, out, optActionItem, true);
                    } else if (dec.equals("n")) {
                        aux = requestMarkActionAsDone(in, out, optActionItem, false);
                    }


                    //response
                    if (aux) {
                        System.out.println("Invite accept with success!");
                        break;
                    } else {
                        System.out.println("Invite not accepted...");
                        break;
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

    public static int requestNumberOfMessegesToRead(DataInputStream in, DataOutputStream out) {
        try {
            out.write(10);
            return in.read();
        } catch (IOException e) {
            return -1;
        }
    }

    public static boolean requestServerNewMeeting(DataInputStream in, DataOutputStream out, String request) {
        boolean aceptSignal;
        try {
            out.write(1);
        } catch (Exception e) {
            return false;
        }
        try {
            aceptSignal = in.readBoolean();
            out.writeUTF(request);
            return in.readBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    public static String requestUpcomingMeetings(DataInputStream in, DataOutputStream out) {
        String result = "";
        try {
            out.write(2);
            result = in.readUTF();
        } catch (Exception e) {
        }
        return result;
    }

    public static String requestPastMeetings(DataInputStream in, DataOutputStream out) {
        String result = "";
        try {
            out.write(3);
            result = in.readUTF();
        } catch (Exception e) {
        }
        return result;
    }

    public static String requestMessages(DataInputStream in, DataOutputStream out) {
        String result = "";
        try {
            out.write(8);
            result = in.readUTF();
        } catch (Exception e) {
        }
        return result;
    }

    public static String requestAgendaItemsFromUpComingMeeting(DataInputStream in, DataOutputStream out, int opt) {
        boolean aceptSignal;
        String result = "";
        try {
            out.write(6);
        } catch (Exception e) {
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;
    }

    public static String requestAgendaItemsFromPastMeeting(DataInputStream in, DataOutputStream out, int opt) {
        boolean aceptSignal;
        String result = "";
        try {
            out.write(7);
        } catch (Exception e) {
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;
    }
    
    public static String requestAgendaItemsFromCurrentMeetings(DataInputStream in, DataOutputStream out, int opt) {
        boolean aceptSignal;
        String result = "";
        try {
            out.write(21);
        } catch (Exception e) {
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;
    }

    public static String requestResumeUpcumingMeeting(DataInputStream in, DataOutputStream out, int opt) {
        String result = "";
        try {
            out.write(4);
        } catch (Exception e) {
        }
        try {
            in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;
    }

    public static String requestResumePastMeeting(DataInputStream in, DataOutputStream out, int opt) {
        String result = "";
        try {
            out.write(5);
        } catch (Exception e) {
        }
        try {
            in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;
    }

    public static String requestResumeMesage(DataInputStream in, DataOutputStream out, int opt) {
        String result = "";
        try {
            out.write(9);
        } catch (Exception e) {
        }
        try {
            in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;
    }

    public static String requestActionItemsPastMeeting(DataInputStream in, DataOutputStream out, int opt) {
        /*
        boolean aceptSignal;
        String result = "";
        try {
            out.write(6);
        } catch (Exception e) {
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;*/
        return "1->AI: Stannis will defend the real\n2-> AI: Davos and meloissandre will get layd";
    }

    public static String resquestChatFromItemPastMeeting(DataInputStream in, DataOutputStream out, int opt) {
        /*
        boolean aceptSignal;
        String result = "";
        try {
            out.write(6);
        } catch (Exception e) {
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;*/
        return "Conversation: \n Stannis-> Davos give me my magic sword! \n2-> Davos-> here yougo you're grace... melessiandre as bee excpteing you yoy're grace";
    }

    public static boolean replyInvite(DataInputStream in, DataOutputStream out, boolean decision) {
        try {
            out.writeBoolean(decision);
            return in.readBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean requestAddItemToAgenda(DataInputStream in, DataOutputStream out, int opt, String itemToadd) {
        boolean aceptSignal;
        try {
            out.write(11);
        } catch (Exception e) {
            return false;
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            aceptSignal = in.readBoolean();
            out.writeUTF(itemToadd);
            return in.readBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean requestDeleteItemToAgenda(DataInputStream in, DataOutputStream out, int optMeetenig, int itemToDelete) {
        boolean aceptSignal;
        try {
            out.write(12);
        } catch (Exception e) {
            return false;
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(optMeetenig);
            aceptSignal = in.readBoolean();
            out.write(itemToDelete);
            return in.readBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean requestMofifyItemToAgenda(DataInputStream in, DataOutputStream out, int optMeeting, int optItemToModify, String newAgendaItem) {
        boolean aceptSignal;
        try {
            out.write(13);
        } catch (Exception e) {
            return false;
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(optMeeting);
            aceptSignal = in.readBoolean();
            out.write(optItemToModify);
            aceptSignal = in.readBoolean();
            out.writeUTF(newAgendaItem);
            return in.readBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean requestAddKeyDecisionToAgendaItem(DataInputStream in, DataOutputStream out, int optMeeting,
                                                            int optItemToModify, String newKeyDecision) {
        boolean aceptSignal;
        try {
            out.write(14);
        } catch (Exception e) {
            return false;
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(optMeeting);
            aceptSignal = in.readBoolean();
            out.write(optItemToModify);
            aceptSignal = in.readBoolean();
            out.writeUTF(newKeyDecision);
            return in.readBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean requestAddNewAcionItem(DataInputStream in, DataOutputStream out, int opt, String newActionItem) {
        boolean aceptSignal;
        try {
            out.write(15);
        } catch (Exception e) {
            return false;
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            aceptSignal = in.readBoolean();
            out.writeUTF(newActionItem);
            return in.readBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    public static int requestSizeToDo(DataInputStream in, DataOutputStream out) {
        try {
            out.write(16);
            return in.read();
        } catch (IOException e) {
            return -1;
        }
    }

    public static String requestActionItemsFromUser(DataInputStream in, DataOutputStream out) {
        String result = "";
        try {
            out.write(17);
        } catch (Exception e) {
        }
        try {
            result = in.readUTF();
        } catch (IOException e) {
        }
        return result;
    }

    public static boolean requestMarkActionAsDone(DataInputStream in, DataOutputStream out, int optAction, boolean decision) {
        boolean success;
        try {
            out.write(18);
            success = in.readBoolean();
            out.write(optAction);
            success = in.readBoolean();
            out.writeBoolean(decision);
            return in.readBoolean();
        } catch (IOException e) {
            return false;
        }
    }

    public static String requestCurrentMeetings(DataInputStream in, DataOutputStream out) {
        String result = "";
        try {
            out.write(19);
            result = in.readUTF();
        } catch (Exception e) {
        }
        return result;
    }

    public static String requestResumeCurrentMeetings(DataInputStream in, DataOutputStream out, int optCurrentMeeting) {
        String result = "";
        try {
            out.write(20);
        } catch (Exception e) {
        }
        try {
            in.readBoolean();
            out.write(optCurrentMeeting);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;
    }


    //-------------------------------------- AUXILIAR FUNCTIONS MENU

    public static void chat(DataInputStream in, DataOutputStream out) throws IOException {
        System.out.print("\nPlease introduce some text: \n >> ");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader bfr = new BufferedReader(isr);
        new readingThread(in);
        String textRecived = "";
        while (true) {
            try {
                textRecived = bfr.readLine();
            } catch (Exception e) {
            }
            out.writeUTF(textRecived); //writing in the socket
        }
    }

    public static void creatNewMeeting(DataInputStream in, DataOutputStream out) {
        String responsible, desireOutCome, local, title, date, guests, agendaItems, request;
        int duration;
        responsible = admin.getUserName();
        System.out.print("Desire outcome: ");
        sc.nextLine();
        desireOutCome = sc.nextLine();
        System.out.print("Local: ");
        local = sc.nextLine();
        System.out.print("Title: ");
        title = sc.nextLine();
        System.out.print("Date (dd/mm/yy): ");
        date = sc.next();
        sc.nextLine();
        System.out.print("Guests (g1,g2,...): ");
        guests = sc.nextLine();
        System.out.print("agendaItems (ai1,ai2,...): ");
        agendaItems = sc.nextLine();
        System.out.print("Duration in minutes: ");
        duration = sc.nextInt();
        sc.nextLine();
        System.out.println();
        request = responsible + "-" + desireOutCome + "-" + local + "-" + title + "-" + date + "-" + guests + "-" + agendaItems + "-" + duration;
        boolean success = requestServerNewMeeting(in, out, request);
        if (success)
            System.out.println("Meeting successfully created!");
        else
            System.out.println("Error creating meeting...");
    }

    public static void addItemstoAgenda(DataInputStream in, DataOutputStream out, int opt) {
        String itemToDiscuss;
        System.out.println("Add items to agenda: ");
        System.out.println("Item to discuss: ");
        sc.nextLine();
        itemToDiscuss = sc.nextLine();
        boolean success = requestAddItemToAgenda(in, out, opt, itemToDiscuss);
        if (success)
            System.out.println("Agenda item was added successfully!!");
        else
            System.out.println("Error adding Item to Agenda....");
    }

    public static void DeleteItemstFromAgenda(DataInputStream in, DataOutputStream out, int optMeeting) {
        int optItemtoDelete, size;
        String options = requestAgendaItemsFromUpComingMeeting(in, out, optMeeting);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all agenda items
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optItemtoDelete = sc.nextInt();
            if (optItemtoDelete == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            } else if (optItemtoDelete < 0 || optItemtoDelete > size) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Wrong option, try again");
            }
        } while (optItemtoDelete < 0 || optItemtoDelete > size);

        boolean success = requestDeleteItemToAgenda(in, out, optMeeting, optItemtoDelete);
        if (success) {
            System.out.println("Agenda item was deleted successfully!!");
        } else {
            System.out.println("Error deleting Item from Agenda....");
        }


    }

    public static void modifyNameFromAgendaItem(DataInputStream in, DataOutputStream out, int optMeeting, int optItemtoModify) {
        String NewItemToDiscuss;
        System.out.println("New item to discuss: ");
        sc.nextLine();
        NewItemToDiscuss = sc.nextLine();
        boolean success = requestMofifyItemToAgenda(in, out, optMeeting, optItemtoModify, NewItemToDiscuss);
        if (success)
            System.out.println("Agenda item was modified successfully!!");
        else
            System.out.println("Error changing Item fom Agenda....");
    }

    public static void addNewKeyDecisionToAgendaitem(DataInputStream in, DataOutputStream out, int optMeeting, int optItemtoAddKeyDecision) {
        String NewKeyDecision;
        System.out.println("New key Decision: ");
        sc.nextLine();
        NewKeyDecision = sc.nextLine();
        boolean success = requestAddKeyDecisionToAgendaItem(in, out, optMeeting, optItemtoAddKeyDecision, NewKeyDecision);
        if (success)
            System.out.println("Key decision added successfully!!");
        else
            System.out.println("Error ading key decision....");
    }

    public static void addNewActionItem(DataInputStream in, DataOutputStream out, int optMeeting) {
        String newActionItem = "", responsableUser = "";
        sc.nextLine();
        System.out.println("New ation Item: ");
        newActionItem = sc.nextLine();
        System.out.println("Responsable user: ");
        responsableUser = sc.nextLine();
        boolean success = requestAddNewAcionItem(in, out, optMeeting, newActionItem + "-" + responsableUser);
        if (success)
            System.out.println("Agenda item was added successfully!!");
        else
            System.out.println("Error adding Item to Agenda....");

    }


    //-------------------------------------- TEST DATA INPUT
    public static boolean myDateTest(int dia, int mes, int ano) {
        Date actualDate = new Date();
        int ano2 = actualDate.getYear() + 1900;
        if (ano < (ano2 - 100) || ano > ano2) {
            return false;
        } else {
            if (mes < 1 || mes > 12) {
                return false;
            } else {
                if (dia < 1 || dia > 31) {
                    return false;
                } else if (dia > 28 && mes == 2 && isLeapYear(ano) == false) {
                    return false;
                } else if (dia > 29 && mes == 2 && isLeapYear(ano) == true) {
                    return false;
                } else if (dia > 30 && (mes == 4 || mes == 6 || mes == 9 || mes == 11)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isLeapYear(int ano) {
        return (ano % 4 == 0);
    }
}

class readingThread extends Thread {
    protected DataInputStream din;

    public readingThread(DataInputStream in) {
        this.din = in;
        this.start();
    }

    public void run() {
        try {
            while (true) {
                System.out.println("Server says: " + din.readUTF());
                System.out.println(">> ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
