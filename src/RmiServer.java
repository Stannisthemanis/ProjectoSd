import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Diogo on 16/10/2014.
 */
public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {
    public static ArrayList<Meeting> meetings = new ArrayList<Meeting>();
    public static ArrayList<User> users = new ArrayList<User>();
    public static ArrayList<Invite> invitations = new ArrayList<Invite>();


    protected RmiServer() throws RemoteException {
        super();
//        try {
//            Save.loadForAL();
//            displayAllAL(); // all info in the files
//        } catch (IOException e) {
//        } catch (ClassNotFoundException e) {
//        }
        try {
            this.firstUse();
        } catch (RemoteException e) {
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

    public boolean addNewMeeting(String newMeeting) throws RemoteException {
        String[] tokenizer = newMeeting.split("-");
        User responsibleUser = findUser(tokenizer[0]);
        String desireOutcome = tokenizer[1];
        String local = tokenizer[2];
        String meetingTitle = tokenizer[3];
        Calendar date = Calendar.getInstance();
        date.set(Integer.parseInt(tokenizer[4].split("/")[2]), Integer.parseInt(tokenizer[4].split("/")[1]),
                Integer.parseInt(tokenizer[4].split("/")[0]));
        ArrayList<AgendaItem> agendaItems = new ArrayList<AgendaItem>();
        for (String s : tokenizer[6].split(",")) {
            agendaItems.add(new AgendaItem(s));
        }
        agendaItems.add(new AgendaItem("Any other business"));
        int duration = Integer.parseInt(tokenizer[7]);

        Meeting meeting = new Meeting(meetingTitle, local, responsibleUser, desireOutcome, date, duration, agendaItems);
        meetings.add(meeting);
        Invite newInvite = null;
        for (String s : tokenizer[5].split(",")) {
            newInvite = new Invite(meeting, 0, findUser(s));
            invitations.add(newInvite);
        }
        return true;

    }

    public String getUpcumingMeetings(User user) throws RemoteException {
        String meeting = "";
        int i = 1;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (Meeting m : meetings) {
            if (m.getDate().after(today)) {
                if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                    meeting += i + "- " + m.getMeetingTitle() + "\n";
                    i++;
                }
            }
        }
        return meeting;
    }

    public String getPassedMeetings(User user) throws RemoteException {
        String meeting = "";
        int i = 1;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (Meeting m : meetings) {
            if (m.getDate().before(today)) {
                if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                    meeting += i + "- " + m.getMeetingTitle() + "\n";
                    i++;
                }
            }
        }
        System.out.println(meeting);
        return meeting;
    }

    public String getMeetingInfo(int flag, int nMeeting, User user) throws RemoteException {
        //flag 1- future meeting 2- passed meeting
        int i = 0;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        if (flag == 1) {
            for (Meeting m : meetings) {
                if (m.getDate().after(today)) {
                    if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                        i++;
                        if (i == nMeeting)
                            return m.toString();
                    }
                }
            }
        } else {
            for (Meeting m : meetings) {
                if (m.getDate().before(today)) {
                    if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                        i++;
                        if (i == nMeeting)
                            return m.toString();
                    }
                }
            }
        }
        return "Meeting not found.. ";
    }

    public String getAgendaItemFromMeeting(int flag, int nMeeting, User user) throws RemoteException {
        //flag 1- future meeting 2- passed meeting
        int i = 0;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        if (flag == 1) {
            for (Meeting m : meetings) {
                if (m.getDate().after(today)) {
                    if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                        i++;
                        if (i == nMeeting)
                            return m.printAgendaItems();
                    }
                }
            }
        } else {
            for (Meeting m : meetings) {
                if (m.getDate().before(today)) {
                    if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                        i++;
                        if (i == nMeeting)
                            return m.printAgendaItems();
                    }
                }
            }
        }
        return "Meeting not found.. ";
    }

    public String getMessagesByUser(User user) throws RemoteException {
        int j = 1;
        String output = null;
        for (Invite i : invitations) {
            if (i.getInvitedUser().getUserName().equals(user.getUserName())) {
                if (output == null)
                    output = "";
                output += j + "- Meeting: " + i.getMeeting().getMeetingTitle() + "| Created by: " + i.getMeeting().getResponsibleUser().getUserName() + "\n";
                j++;
            }
        }
        if (output == null)
            output = "You have no messages";
        return output;
    }

    public int getNumberOfMessages(User user) throws RemoteException {
        int i = 0;
        for (Invite invitation : invitations) {
            if (invitation.getInvitedUser().getUserName().equals(user.getUserName()))
                i++;
        }
        return i;
    }

    public String getResumeOfMessage(User user, int message) throws RemoteException {
        int j = 0;
        for (Invite i : invitations) {
            if (i.getInvitedUser().getUserName().equals(user.getUserName()))
                j++;
            if (j == message) {
                return i.getMeeting().toString();
            }
        }
        return "Error!!";
    }

    public boolean setReplyOfInvite(User user, int message, boolean decision) throws RemoteException {
        int j = 0;
        for (Invite i : invitations) {
            if (i.getInvitedUser().getUserName().equals(user.getUserName()))
                j++;
            if (j == message) {
                if (decision == true) {
                    i.getMeeting().addUser(findUser(user.getUserName()));
                }
                invitations.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean addAgendaItem(int nMeeting, String newAgendaItem, User user) throws RemoteException {
        AgendaItem newItem = new AgendaItem(newAgendaItem);
        int i = 0;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (Meeting m : meetings) {
            if (m.getDate().after(today)) {
                if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                    i++;
                }
                if (i == nMeeting) {
                    m.addAgendaItem(newItem);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeAgendaItem(int nMeeting, int nAgenda, User user) throws RemoteException {
        int i = 0;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (Meeting m : meetings) {
            if (m.getDate().after(today)) {
                if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                    i++;
                }
                if (i == nMeeting) {
                    m.removerAgendaItem(nAgenda - 1);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean modifyTitleAgendaItem(int nMeeting, int nAgenda, String mAgenda, User user) throws RemoteException {
        int i = 0;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (Meeting m : meetings) {
            if (m.getDate().after(today)) {
                if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                    i++;
                }
                if (i == nMeeting) {
                    m.getAgendaItems().get(i - 1).setItemToDiscuss(mAgenda);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addKeyDecisionToAgendaItem(int nMeeting, int nAgenda, String keyDecision, User user) throws RemoteException {
        int i = 0;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (Meeting m : meetings) {
            if (m.getDate().after(today)) {
                if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                    i++;
                }
                if (i == nMeeting) {
                    m.getAgendaItems().get(i - 1).setKeyDecision(keyDecision);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addActionItem(int nMeeting, String newActionItem, User user) throws RemoteException {
        ActionItem actionItem = new ActionItem(newActionItem.split("-")[0], findUser(newActionItem.split("-")[1]).getUserName());
        int i = 0;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        for (Meeting m : meetings) {
            if (m.getDate().after(today)) {
                if (m.getResponsibleUser().getUserName().equals(user.getUserName()) || m.isInvited(user.getUserName())) {
                    i++;
                }
                if (i == nMeeting) {
                    m.addActionItem(actionItem);
                    findUser(actionItem.getUserResponsible()).addActionItem(actionItem);
                    return true;
                }
            }
        }
        return false;

    }

    public String getActionItemFromUser(User user) throws RemoteException {
        int j = 1;
        String output = null;
        for (ActionItem aItem : user.getActionItems()) {
            if (!aItem.isCompleted()) {
                if (output == null)
                    output = "";
                output += j + " - " + aItem.getName() + "\n";
                j++;
            }
        }
        if (output == null)
            output = "You have no action to be done";
        return output;
    }

    public int getSizeOfTodo(User user) throws RemoteException {
        int todoSize = 0;
        if (user.getActionItems().size() > 0) {
            for (ActionItem aItem : user.getActionItems()) {
                if (aItem.completed == false)
                    todoSize++;
            }
        }
        return todoSize;
    }

    public boolean setActionAsCompleted(User user, int n) throws RemoteException {
        int i = 0;
        for (ActionItem aItem : user.getActionItems()) {
            if (!aItem.isCompleted())
                i++;
            if (i == n) {
                aItem.setCompleted(true);
                return true;
            }
        }
        return false;
    }

    public void firstUse() throws RemoteException {

        users.add(new User("Stannis Baratheon", "root", "Dragonstone/Wall", new Date("10/10/1000"), 912345678, "stannisthemannis@therightfullking@wes"));
        users.add(new User("Jon Snow", "root", "Winterfell/Wall/The North", new Date("10/10/1000"), 912345678, "JonSnow@bastard.wall"));
        users.add(new User("Daenerys Targaryen", "root", "Westeros/Pentos/Dothraki_sea/Qarth/Astapor/Yunkai/Meree", new Date("10/10/1000"),
                912345678, "Daenerys_Stormborn_of_the_House_Targaryen,_the_First_of_Her_Name,_the_Unburnt,_Queen_of_Meereen,_Queen_of_the_Andals_and_the_" +
                "Rhoynar_and_the_First_Men,_Khaleesi_of_the_Great_Grass_Sea,_Breaker_of_Chains,_and_Mother_of_Dragons@mesedUpGirl.essos"));
        users.add(new User("Reek", "root", "DreadFort/Winterfell", new Date("10/10/1000"), 912345678, "theycutofmydick@theon.varys"));
        users.add(new User("manel", "root", "santaterriola", new Date("12/1/2110"), 212233, "manel@tenhodemijar.ja"));

        addNewMeeting("manel-talk about stannis-wall-stannisthemannis-8/2/2010-Stannis Baratheon,Jon Snow-Ai1,Ai2-120");
        addNewMeeting("Stannis Baratheon-talk about mellissandre-wall-mellissandrethemannis-9/2/2011-manel,Jon Snow-Ai3,Ai4-120");
        addNewMeeting("manel-talk about Jon-wall-jonthemannis-10/2/2015-Stannis Baratheon,Jon Snow-Ai5,Ai6-120");
        addNewMeeting("manel-talk about Robert-wall-robertthemannis-11/2/2016-Stannis Baratheon,Jon Snow-Ai7,Ai8-120");
    }

    public static void main(String[] args) {
        try {
            RmiServer rmiServer = new RmiServer();
            LocateRegistry.createRegistry(1099).rebind("DataBase", rmiServer);
            System.out.println("RmiServer Ready");
        } catch (RemoteException e) {
            System.out.println("*** RmiServer: " + e.getMessage());
        }

        try { //store in files
            Save.storeInFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayAllAL() {
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("------------------------------");
        for (Meeting m : meetings) {
            System.out.println(m);
        }
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
