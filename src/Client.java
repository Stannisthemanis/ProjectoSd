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

    public static void main(String[] args) {
        String username = "", password;

        //args[0] = hostname
        if (args.length != 1) {
            System.out.println("Sintax: java Client hostname");
            System.exit(0);
        }

        Socket socket = null;
        int ServerSocket = 6000;
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
        System.out.println("Welcome " + username);


        try {
            socket = new Socket(args[0], ServerSocket);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            mainMenu(in,out);
            //chat(in, out);
        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
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
        int optionMainMenu, optionMenu1, optionMenu2;
        do {
            System.out.println("Main Menu");
            System.out.println("1-> Meetings");
            System.out.println("2-> Messages");
            System.out.print("Choose option: ");
            optionMainMenu = sc.nextInt();
            switch (optionMainMenu) {
                case 1: {
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                    //Submenu meetings
                    do {
                        System.out.println("Main Meetings");
                        System.out.println("1-> Create new meeting");
                        System.out.println("2-> Check upcomming meetings");
                        System.out.println("3-> Check past meetings");
                        System.out.println("4-> Modify items from the agenda");
                        System.out.print("Choose option: ");
                        optionMenu1 = sc.nextInt();
                        switch (optionMenu1) {
                            case 1: {
                                System.out.println("Creat new meeting");
                                creatNewMeeting(in, out);
                                System.exit(0);
                            }
                            break;
                            case 2: {
                                System.out.println("option 2");
                                System.exit(0);
                            }
                            break;
                            case 3: {
                                System.out.println("option 3");
                                System.exit(0);
                            }
                            break;
                            case 4: {
                                System.out.println("option 4");
                                System.exit(0);
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
                    System.exit(0);
                }
                break;
                default: {
                    System.out.println("Wrong option");
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
                }
                break;
            }
        } while (optionMainMenu < 1 || optionMainMenu > 2);
    }

    public static void chat(DataInputStream in, DataOutputStream out) throws IOException{
        String textSent="test";
        out.writeUTF(textSent);
        System.out.println("Server: " + in.readUTF());
        System.out.println("\nPlease introduce some text: \n >> ");
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

    public static void creatNewMeeting(DataInputStream in, DataOutputStream out){
        String responsible, desireOutCome, local, title, date, guests, agendaItems, duration, request;
        System.out.print("Responsile: "); responsible=sc.next();
        System.out.print("Desire outcome: "); desireOutCome=sc.next();
        System.out.print("Local: "); local=sc.next();
        System.out.print("Title: "); title=sc.next();
        System.out.print("Date (dd/mm/yy): "); date=sc.next();
        System.out.print("Guests (g1,g2,...): "); guests=sc.next();
        System.out.print("agendaItems (ai1,ai2,...): "); agendaItems=sc.next();
        System.out.print("Duration: (dd:hh:mm) "); duration=sc.next();
        System.out.println();
        request = responsible+","+desireOutCome;
        boolean success=requestServerNewMeeting(in, out);
        if(success)
            System.out.println("Meeting successfully created!");
        else
            System.out.println("Error creating meeting...");
    }

    public static boolean requestServerNewMeeting(DataInputStream in, DataOutputStream out){
        //out.writeUtf;
        return true;
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
