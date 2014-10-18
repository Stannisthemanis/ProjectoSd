import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Diogo on 16/10/2014.
 */
public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {
    public static ArrayList<Meeting> meetings = new ArrayList<Meeting>();
    public static ArrayList<User> users = new ArrayList<User>();
    public static ArrayList<Invite> invitations = new ArrayList<Invite>();

    protected RmiServer() throws RemoteException {
        super();
//        meetings = new ArrayList<Meeting>();
//        users = new ArrayList<User>();
//        invitations = new ArrayList<Invite>();
  /*      for (User i : users) {
            System.out.println(i);
        }*/
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
//        Date duration = new Date(tokenizer[7]);
        Date duration = new Date();

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

    public static void firstUse() {

        users.add(new User("Stannis Baratheon", "root", "Dragonstone/Wall", new Date("10/10/1000"), 912345678, "stannisthemannis@therightfullking@wes"));
        users.add(new User("Jon Snow", "root", "Winterfell/Wall/The North", new Date("10/10/1000"), 912345678, "JonSnow@bastard.wall"));
        users.add(new User("Daenerys Targaryen", "root", "Westeros/Pentos/Dothraki_sea/Qarth/Astapor/Yunkai/Meree", new Date("10/10/1000"),
                912345678, "Daenerys_Stormborn_of_the_House_Targaryen,_the_First_of_Her_Name,_the_Unburnt,_Queen_of_Meereen,_Queen_of_the_Andals_and_the_" +
                "Rhoynar_and_the_First_Men,_Khaleesi_of_the_Great_Grass_Sea,_Breaker_of_Chains,_and_Mother_of_Dragons@mesedUpGirl.essos"));
        users.add(new User("Reek", "root", "DreadFort/Winterfell", new Date("10/10/1000"), 912345678, "theycutofmydick@theon.varys"));
        users.add(new User("manel", "root", "santaterriola", new Date("12/1/2110"), 212233, "manel@tenhodemijar.ja"));

//        users.add(new User());
//        users.add(new User());
//        users.add(new User());
//        users.add(new User());
//        users.add(new User());

        meetings.add(new Meeting("bastard", "wall", users.get(1), "kill the bastard in the wal", new Date(), new Date()));
        meetings.add(new Meeting("montain", "Dorne", users.get(2), "Delay Montain's head delivey", new Date(), new Date()));
        meetings.add(new Meeting("alayne", "eary", users.get(1), "get layd with sansa", new Date(), new Date()));

        invitations.add(new Invite(meetings.get(2), 1, users.get(1)));
        invitations.add(new Invite(meetings.get(1), 0, users.get(3)));
        invitations.add(new Invite(meetings.get(0), 1, users.get(3)));
        invitations.add(new Invite(meetings.get(1), 1, users.get(4)));
        invitations.add(new Invite(meetings.get(2), 1, users.get(0)));
        invitations.add(new Invite(meetings.get(1), 1, users.get(1)));
    }

    public static void main(String[] args) {
        try {
            RmiServer rmiServer = new RmiServer();

            try { //load from files
//        firstUse();
                Save.loadForAL();
                displayAllAL(); // all info in the files
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            }

            LocateRegistry.createRegistry(1099).rebind("DataBase", rmiServer);
            System.out.println("RmiServer Ready");
        } catch (Exception e) {
            System.out.println("RmiServer: " + e.getMessage());
        }

        try { //store in files
            Save.storeInFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayAllAL() {
        Scanner sc = new Scanner(System.in);
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("------------------------------");
//         sc.next();
//        for (Meeting meeting : meetings) {
//            System.out.println(meeting);
//        }
//        System.out.println("------------------------------");
//        System.out.println("!!!!2"); sc.next();
//        for (Invite invitation : invitations) {
//            System.out.println(invitation);
//        }
//        System.out.println("!!!!3" +
//                ""); sc.next();
    }

}

class Save {

    public static void loadForAL() throws IOException, ClassNotFoundException {
        if (new File("meetings.dat").exists()) {
            FileInputStream fis = new FileInputStream("meetings.dat");
            ObjectInputStream oos = new ObjectInputStream(fis);
            RmiServer.meetings = (ArrayList<Meeting>) oos.readObject();
            oos.close();
        }
        if (new File("users.dat").exists()) {
            FileInputStream fis = new FileInputStream("users.dat");
            ObjectInputStream oos = new ObjectInputStream(fis);
            RmiServer.users = (ArrayList<User>) oos.readObject();
            oos.close();
        }
        if (new File("invitations.dat").exists()) {
            FileInputStream fis = new FileInputStream("invitations.dat");
            ObjectInputStream oos = new ObjectInputStream(fis);
            RmiServer.invitations = (ArrayList<Invite>) oos.readObject();
            oos.close();
        }
    }

    public static void storeInFiles() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("meetings.dat"));
        oos.writeObject(RmiServer.meetings);
        oos.close();
        ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("users.dat"));
        oos1.writeObject(RmiServer.users);
        oos1.close();
        ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("invitations.dat"));
        oos2.writeObject(RmiServer.invitations);
        oos2.close();
        System.out.println("Files saved!");
    }
}
