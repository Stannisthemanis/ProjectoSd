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
    ArrayList<User> users;
    ArrayList<Invite> invitations;


    protected RmiServer() throws RemoteException {
        super();
        meetings = new ArrayList<Meeting>();
        users = new ArrayList<User>();
        invitations = new ArrayList<Invite>();
        users.add(new User("Stannis", "Stannis", "Dragonstone/Wall", new Date("10/10/1000"), 912345678, "stannisthemannis@therightfullking@westeros"));
        users.add(new User("Jon Snow", "Jon Snow", "Wall", new Date("10/10/1000"), 912345678, "JonSnow@bastard.wall"));
        users.add(new User("manel", "root", "dragonstone", new Date("12/1/2110"), 212233, "stannisthemannis@kingoftheandals.wes"));
        for (User i : users) {
            System.out.println(i);
        }
    }

    public User findUser(String username) throws RemoteException {
        for (User i : users) {
            if (i.getUserName().equals(username))
                return i;
        }
        return null;
    }

    public boolean checkLogin(String username, String password) throws RemoteException {
        for (User i : users) {
            if (i.getPassWord().equals(password) && i.getUserName().equals(username))
                return true;
        }
        return false;
    }

    public Meeting addNewMeeting(String newMeeting) throws RemoteException {
        String[] tokenizer = newMeeting.split("-");
        System.out.println("name");
        User responsibleUser = findUser(tokenizer[0]);
        System.out.println("out");
        String desireOutcome = tokenizer[1];
        System.out.println("local");
        String local = tokenizer[2];
        System.out.println("title");
        String meetingTitle = tokenizer[3];
        System.out.println("data");
        Date date = new Date(tokenizer[4]);
        System.out.println("agenda");
        ArrayList<AgendaItem> agendaItems = new ArrayList<AgendaItem>();
        for (String s : tokenizer[6].split(",")) {
            agendaItems.add(new AgendaItem(s));
        }
        System.out.println("duration");
        Date duration = new Date(tokenizer[7]);

        Meeting meeting = new Meeting(meetingTitle, local, responsibleUser, desireOutcome, date, duration, agendaItems);
        meetings.add(meeting);
        Invite newInvite = null;
        for (String s : tokenizer[5].split(",")) {
            newInvite = new Invite(meeting, 0, findUser(s));
            invitations.add(newInvite);
            meeting.addInvite(newInvite);
        }


        return meeting;

    }

    public String getUpcumingMeetings(User user) throws RemoteException {
        String meeting = "";
        int i = 1;
        for (Meeting m : meetings) {
            if ((m.getResponsibleUser().equals(user) || m.getUsersInvited().contains(user)) && m.getDate().after(new Date())) {
                meeting += i + "- " + m.getMeetingTitle() + "\n";
                i++;
            }
        }
        System.out.println(meeting);
        return meeting;
    }

    public String getPassedMeetings(User user) throws RemoteException {
        String meeting = "";
        int i = 1;
        for (Meeting m : meetings) {
//            if ((m.getResponsibleUser().equals(user) || m.getUsersInvited().contains(user)) && m.getDate().before(new Date())) {
            meeting += i + "- " + m.getMeetingTitle() + "\n";
            i++;
//            }
        }
        System.out.println(meeting);
        return meeting;
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
