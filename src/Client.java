import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Diogo on 14/10/2014.
 */
public class Client {
    public static void main(String[] args) {

        String username, password;

        //args[0] = hostname
        if (args.length != 1) {
            System.out.println("Sintax: java Client hostname");
            System.exit(0);
        }

        Socket socket = null;
        int ServerSocket = 6000;

        int tries = 0;
        while ((username = login()) == null) {
            System.out.println("Invalid username/password please try again");
            tries++;

            if (tries == 3) {
                System.out.println("You execeded maximum number of tries(3)");
                System.exit(0);
            }
        }

        try {
            //TODO meter while para escrever e fazer thread para leitura, init thread
            socket = new Socket(args[0], ServerSocket);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            out.writeUTF(username);

            System.out.println("Server: " + in.readUTF());

        } catch (IOException e) {
            System.out.println("Conection: " + e.getMessage());
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
