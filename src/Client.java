import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Diogo on 14/10/2014.
 */
public class Client {
    public static void main(String[] args) {

        String username, password;


        Socket socket = null;
        int ServerSocket = 6000;
        String host = "localhost";

        int tries = 0;
        while ((username = login()) == null) {
            System.out.println("Invalid username/password please try again (" + (2 - tries) + " left)");
            tries++;

            if (tries == 3) {
                System.out.println("You execeded maximum number of tries(3)");
                System.exit(0);
            }
        }

        try {
            socket = new Socket(host, ServerSocket);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF(username);
            System.out.println("Server: " + in.readUTF());
            System.out.println("\nYou're in! Please introduce some text: \n >> ");

            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader bfr = new BufferedReader(isr);

            // reading thread
            new readingThread(in);

            String text = "";
            while (true) {
                try {
                    text = bfr.readLine();
                } catch (Exception e) {
                }
                out.writeUTF(text); //writing in the socket
            }
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

}

class readingThread extends Thread {
    protected DataInputStream din;

    public readingThread(DataInputStream in){
        this.din=in;
        this.start();
    }
    public void run(){
        try {
            while (true){
                System.out.println("Server says: "+din.readUTF());
                System.out.println(">> ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
