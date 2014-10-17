import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Diogo on 16/10/2014.
 */
public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {
    ArrayList<Meeting> meetings;

    protected RmiServer() throws RemoteException {
        super();
        meetings = new ArrayList<Meeting>();
    }

    public String teste() throws RemoteException {
        return "a";
    }

    public static void main(String[] args) {

        try {
            RmiServer rmiServer = new RmiServer();
            LocateRegistry.createRegistry(1099).rebind("DataBase", rmiServer);
            System.out.println("RmiServer Ready");
        } catch (Exception e) {
            System.out.println("RmiServer: " + e.getMessage());
        }
    }
}
