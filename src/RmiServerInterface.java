import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Diogo on 16/10/2014.
 */
public interface RmiServerInterface extends Remote {

    public boolean addNewMeeting(String newMeeting) throws RemoteException;

    public User findUser(String username) throws RemoteException;

    public boolean checkLogin(String username, String password) throws RemoteException;

    public String getUpcumingMeetings(String user) throws RemoteException;

    public String getPassedMeetings(String user) throws RemoteException;

    public String getCurrentMeetings(String user) throws RemoteException;

    public String getMeetingInfo(int flag, int nMeeting, String user) throws RemoteException;

    public String getAgendaItemFromMeeting(int flag, int nMeeting, String user) throws RemoteException;

    public String getMessagesByUser(String user) throws RemoteException;

    public int getNumberOfMessages(String user) throws RemoteException;

    public String getResumeOfMessage(String user, int message) throws RemoteException;

    public boolean setReplyOfInvite(String user, int message, boolean decision) throws RemoteException;

    public boolean addAgendaItem(int nMeeting, String newAgendaItem, String user) throws RemoteException;

    public boolean removeAgendaItem(int nMeeting, int nAgenda, String user) throws RemoteException;

    public boolean modifyTitleAgendaItem(int nMeeting, int nAgenda, String mAgenda, String user) throws RemoteException;

    public boolean addKeyDecisionToAgendaItem(int nMeeting, int nAgenda, String keyDecision, String user) throws RemoteException;

    public boolean addActionItem(int nMeeting, String actionItem, String user) throws RemoteException;

    public String getActionItemFromUser(String user) throws RemoteException;

    public int getSizeOfTodo(String user) throws RemoteException;

    public boolean setActionAsCompleted(String user, int n) throws RemoteException;

    public String getActionItensFromMeeting(int nMeeting, String user) throws RemoteException;

    public String getMessagesFromAgendaItem(int nMeeting, int nAgenda, String user) throws RemoteException;

    public boolean addMessage(int nMeeting, int nAgenda, String user, String message) throws RemoteException;

    public void addClientToChat(int nMeeting, int nAgenda, String user) throws RemoteException;

    public boolean removeClientFromChat(int nMeeting, int nAgenda, String user) throws RemoteException;

    public boolean userOnChat(int nMeeting, int nAgenda, String user) throws RemoteException;


}
