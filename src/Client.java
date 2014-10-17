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
        admin = new User("manel", "root", "dragonstone", new Date("12/1/2110"), 212233, "stannisthemannis@kingoftheandals.wes");
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
                out.writeUTF("teste");
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

    public static void mainMenu(DataInputStream in, DataOutputStream out) {
        int option;
        System.out.println("\n\n\n\n\n\n\n\n\n");
        do {
            System.out.println("Main Menu");
            System.out.println("1-> Meetings");
            System.out.println("2-> Messages");
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
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }

        }
        while (true);
    }

    public static void subMenuMeetings(DataInputStream in, DataOutputStream out) {
        int option;
        do {
            System.out.println("\n\n\n");
            System.out.println("Menu Meetings");
            System.out.println("1-> Create new meeting");
            System.out.println("2-> Check upcoming meetings");
            System.out.println("3-> Check past meetings");
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

    public static void subMenuMessages(DataInputStream in, DataOutputStream out) {
        System.out.println("All Messages: ");
        System.out.println("Under construction... sorry :( \n\n");
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
        System.out.println("Resume from meeting "+optUm);
        System.out.println("\n" + requestResumeMeeting(in, out, optUm) + "\n"); // resume of chosen meeting
        do {
            if (optUm == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
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
                    SubMenuConsultAgendaItems(in, out, optUm);
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Add items to agenda: ");
                    System.out.println("Under construction... sorry :( \n\n");
                }
                break;
                case 3: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Modify items in agenda: ");
                    System.out.println("Under construction... sorry :( \n\n");
                }
                break;
                case 4: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Delete items from agenda: ");
                    System.out.println("Under construction... sorry :( \n\n");
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

    public static void SubMenuConsultAgendaItems(DataInputStream in, DataOutputStream out, int opt) {
        int optUm, size, opt2;
        String options = requestAgendaItems(in, out, opt);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all agenda items
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optUm = sc.nextInt();
        } while (optUm < 0 || optUm > size);
        do {
            if (optUm == 0) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                break;
            }
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("Options for Agenda item " + optUm);
            System.out.println("1-> Open chat");
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
                    System.out.println("You will adress Stannis the mannis by 'Your grace' or GFO!");
                    System.out.println("Under construction... sorry :( \n\n");
                    sc.next();
                }break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        }while (true);
    }

    public static void SubMenuConsultAgendaItemsPM(DataInputStream in, DataOutputStream out, int opt) {
        int optUm, size, opt2;
        String options = requestAgendaItems(in, out, opt);
        String[] countOptions = options.split("\n");
        size = countOptions.length;
        do {
            System.out.println(options); //display name of all agenda items
            System.out.println("0-> Back");
            System.out.print("Choose an option: ");
            optUm = sc.nextInt();
        } while (optUm < 0 || optUm > size);
        System.out.println(resquestChatFromItemPastMeeting(in,out,optUm));
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
                }break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        } while (true);
    }

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
        String responsible, desireOutCome, local, title, date, guests, agendaItems, duration, request;
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
        System.out.print("Duration: (dd:hh:mm) ");
        duration = sc.next();
        sc.nextLine();
        System.out.println();
        request = responsible + "-" + desireOutCome + "-" + local + "-" + title + "-" + date + "-" + guests + "-" + agendaItems + "-" + duration;
        boolean success = requestServerNewMeeting(in, out, request);
        if (success)
            System.out.println("Meeting successfully created!");
        else
            System.out.println("Error creating meeting...");
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

    public static String requestAgendaItems(DataInputStream in, DataOutputStream out, int opt) {
/*        boolean aceptSignal;
        String result = "";
        try {
            out.write(5);
        } catch (Exception e) {
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;*/
        return "1->Item: Stannis the mannis\n2-> Item: Stannis protextor of the realm";
    }

    public static String requestResumeMeeting(DataInputStream in, DataOutputStream out, int opt) {
    /*
        boolean aceptSignal;
        String result = "";
        try {
            out.write(4);
        } catch (Exception e) {
        }
        try {
            aceptSignal = in.readBoolean();
            out.write(opt);
            result = in.readUTF(in);
        } catch (IOException e) {
        }
        return result;*/
        return "Resume: Stannis choosen of R'llhor and king of you're mother";
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
