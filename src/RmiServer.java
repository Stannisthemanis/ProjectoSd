import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Diogo on 16/10/2014.
 */
public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {

    protected RmiServer() throws RemoteException {
        super();
    }

    public static void main(String[] args) {

    }
}
