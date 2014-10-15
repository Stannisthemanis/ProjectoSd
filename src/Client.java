import java.net.Socket;

/**
 * Created by Diogo on 14/10/2014.
 */
public class Client {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Sintax: java Client hostname");
            System.exit(0);
        }

        Socket socket = null;
        int ServerSocket = 6000;

//        ServerSocket = new Socket(args[0] , ServerSocket);
    }

}
