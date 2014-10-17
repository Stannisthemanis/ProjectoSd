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
        admin = new User("manel","root","dragonstone",new Date("12/1/2110"),212233,"stannisthemannis@kingoftheandals.wes");
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

    public static void mmainMenu(DataInputStream in, DataOutputStream out) {
        int option;
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
                case 1:{
                    subMenuMeetings(in,out);
                }break;
                case 2: {
                    subMenuMessages(in,out);
                }break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }

            }
        while (true);
    }

    public static void subMenuMeetings(DataInputStream in, DataOutputStream out){
        int option;
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        do {
            System.out.println("Menu Meetings");
            System.out.println("1-> Create new meeting");
            System.out.println("2-> Check upcoming meetings");
            System.out.println("3-> Check past meetings");
            System.out.println("0-> Back");
            System.out.print("Choose option: ");
            option = sc.nextInt();
            if(option==0) break;
            switch (option) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("\nCreate new meeting: ");
                    creatNewMeeting(in, out);
                }
                break;
                case 2:{
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    SubMenuUpcomingMeetings(in, out);
                }break;
                case 3:{
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    SubMenupPastMeetings(in, out);
                }break;
                default: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("Wrong option");
                }
                break;
            }
        }while(true);
    }

    public static void subMenuMessages(DataInputStream in, DataOutputStream out){
        System.out.println("All Messages: ");
        System.out.println("Under construction... sorry :( \n\n");
    }

    public static void SubMenuUpcomingMeetings(DataInputStream in, DataOutputStream out){
        System.out.println("All upcoming meetings: ");
        System.out.println("Under construction... sorry :( \n\n");
    }

    public static void SubMenupPastMeetings(DataInputStream in, DataOutputStream out){
        System.out.println("All Past meetings: ");
        System.out.println("Under construction... sorry :( \n\n");
    }

    public static void mainMenu(DataInputStream in, DataOutputStream out) throws IOException {
        int optionMainMenu, optionMenu1, optionMenu2, optCai;
        do {
            System.out.println("Main Menu");
            System.out.println("1-> Meetings");
            System.out.println("2-> Messages");
            System.out.println("0-> Leave");
            System.out.print("Choose option: ");
            optionMainMenu = sc.nextInt();
            switch (optionMainMenu) {
                case 0:
                    System.exit(0);
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    //Submenu meetings
                    do {
                        System.out.println("Main Meetings");
                        System.out.println("1-> Create new meeting");
                        System.out.println("2-> Check upcoming meetings");
                        System.out.println("3-> Check past meetings");
                        System.out.println("0-> Back");
                        System.out.print("Choose option: ");
                        optionMenu1 = sc.nextInt();
                        switch (optionMenu1) {
                            case 0:
                                break;
                            case 1: {
                                System.out.println("Create new meeting: ");
                                creatNewMeeting(in, out);
                            }
                            break;
                            case 2: {
                                int optAux, size, optUm, optUmCa;
                                System.out.println("\n Check upcoming meetings");
                                String options = requestUpcomingMeetings(in, out);
                                String[] countOptions = options.split("\n");
                                size = countOptions.length;
                                do {
                                    System.out.println(options); //display name of all upcoming meetings
                                    System.out.print("Choose an option: ");
                                    optAux = sc.nextInt();
                                } while (optAux < 1 || optAux > size);
                                do {
                                    System.out.println("\nOptions from meeting " + optAux);
                                    System.out.println("1-> Consult Agenda Items");
                                    System.out.println("0-> Back");
                                    System.out.print("Choose an option: ");
                                    optUm = sc.nextInt();
                                    switch (optUm) {
                                        case 0:
                                            break;
                                        case 1: {
                                            //display agenda items
                                            String agendaItems = requestAgendaItems(in, out);
                                            System.out.println(agendaItems);
                                            String[] countOptionsAi = agendaItems.split("\n");
                                            size = countOptionsAi.length;
                                            System.out.print("\n Choose an option: ");
                                            optAux = sc.nextInt();
                                            do {
                                                System.out.println("1-> Add items");
                                                System.out.println("2-> Modify items");
                                                System.out.println("3-> Delete items");
                                                System.out.println("0-> Back");
                                                System.out.println("Choose an option: ");
                                                optCai = sc.nextInt();


                                            } while (optCai < 0 || optCai > 3);

                                        }
                                        break;
                                    }

                                } while (optUm < 0 || optUm > 3);
                            }
                            break;
                            case 3: {
                                System.out.println("Check past meetings");
                                requestPastMeetings(in, out);
                            }
                            break;
                            case 4: {
                                System.out.println("option 4");
                            }
                            break;
                            default: {
                                System.out.println("Wrong option");
                                System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                            }
                            break;
                        }
                    } while (optionMenu1 < 1 || optionMenu1 > 3);
                    //Submenu meetings end
                }
                break;
                case 2: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    System.out.println("All messages: ");
                }
                break;
                default: {
                    System.out.println("Wrong option");
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
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
//        System.out.print("Desire outcome: ");
        sc.nextLine();
//        desireOutCome = sc.nextLine();
//        System.out.print("Local: ");
//        local = sc.nextLine();
        System.out.print("Title: ");
        title = sc.nextLine();
        System.out.print("Date (dd/mm/yy): ");
        date = sc.next();
//        sc.nextLine();
//        System.out.print("Guests (g1,g2,...): ");
//        guests = sc.nextLine();
//        System.out.print("agendaItems (ai1,ai2,...): ");
//        agendaItems = sc.nextLine();
//        System.out.print("Duration: (dd:hh:mm) ");
//        duration = sc.next();
//        sc.nextLine();
//        System.out.println();
//        request = responsible + "-" + desireOutCome + "-" + local + "-" + title + "-" + date + "-" + guests + "-" + agendaItems + "-" + duration;
        request = responsible + "-" + "desireOutCome" + "-" + "local" + "-" + title + "-" + date + "-" + "Stannis,Jon Snow" + "-" + "agendaItems" + "-" + "1:1:1".replaceAll(":", "/");

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

    public static void requestPastMeetings(DataInputStream in, DataOutputStream out) {
        String result = "";
        try {
            out.write(3);
            result = in.readUTF();
        } catch (Exception e) {
            System.out.println("there gjagjaj");
        }
        System.out.println(result);
    }

    public static String requestAgendaItems(DataInputStream in, DataOutputStream out) {
        return "Stannis king of your mother";
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
