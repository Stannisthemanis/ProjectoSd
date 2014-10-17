import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Diogo on 16/10/2014.
 */
public interface RmiServerInterface extends Remote {

    public boolean addNewMeeting(String newMeeting) throws RemoteException;
//    public String getAllUpcumingMeetings(User user);
//    public String getAllPassedMeetings(User user);

}
