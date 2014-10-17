import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Diogo on 16/10/2014.
 */
public interface RmiServerInterface extends Remote {

    public Meeting addNewMeeting(String newMeeting) throws RemoteException;

    public User findUser(String username) throws RemoteException;

    public boolean checkLogin(String username, String password) throws RemoteException;

    public String getUpcumingMeetings(User user) throws RemoteException;

    public String getPassedMeetings(User user) throws RemoteException;

}
