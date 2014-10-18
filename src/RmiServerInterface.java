import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Diogo on 16/10/2014.
 */
public interface RmiServerInterface extends Remote {

    public boolean addNewMeeting(String newMeeting) throws RemoteException;

    public User findUser(String username) throws RemoteException;

    public boolean checkLogin(String username, String password) throws RemoteException;

    public String getUpcumingMeetings(User user) throws RemoteException;

    public String getPassedMeetings(User user) throws RemoteException;

    public String getMeetingInfo(int flag, int nMeeting, User user) throws RemoteException;

    public String getAgendaItemFromMeeting(int flag, int nMeeting, User user) throws RemoteException;

    public String getMessagesByUser(User user) throws RemoteException;

    public int getNumberOfMessages(User user) throws RemoteException;

    public String getResumeOfMessage(User user, int message) throws RemoteException;

    public boolean setReplyOfInvite(User user, int message, boolean decision) throws RemoteException;

}
