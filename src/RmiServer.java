import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Diogo on 16/10/2014.
 */
public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {
    ArrayList<Meeting> meetings;

    protected RmiServer() throws RemoteException {
        super();
        meetings = new ArrayList<Meeting>();
    }

    public boolean addNewMeeting(String newMeeting) throws RemoteException {
        String meetingTitle;
        String local;
        String responsibleUser;
        String desireOutcome;
        Date date;
        Date duration; //hours
        ArrayList<Invite> invitations = new ArrayList<Invite>();
        ArrayList<AgendaItem> agendaItems = new ArrayList<AgendaItem>();
        ArrayList<ActionItem> actionItems = new ArrayList<ActionItem>();

        for (String s : newMeeting.split("/")) {
            System.out.println(s);
        }

        return true;

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
