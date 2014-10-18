import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class Meeting implements Serializable {
    protected String meetingTitle;
    protected String local;
    protected User responsibleUser;
    protected String desireOutcome;
    protected Date date;
    protected int duration; //hours
    protected ArrayList<User> usersAccepted;
    protected ArrayList<AgendaItem> agendaItems;

    public Meeting(String meetingTitle, String local, User responsibleUser, String desireOutcome, Date date, int duration,
                   ArrayList<AgendaItem> agendaItems) {
        this.meetingTitle = meetingTitle;
        this.local = local;
        this.responsibleUser = responsibleUser;
        this.desireOutcome = desireOutcome;
        this.date = date;
        this.duration = duration;
        this.usersAccepted = new ArrayList<User>();
        this.agendaItems = agendaItems;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public String getLocal() {
        return local;
    }

    public User getResponsibleUser() {
        return responsibleUser;
    }

    public String getDesireOutcome() {
        return desireOutcome;
    }

    public Date getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<User> getUsersInvited() {
        return usersAccepted;
    }

    public ArrayList<AgendaItem> getAgendaItems() {
        return agendaItems;
    }


    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setResponsibleUser(User responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public void setDesireOutcome(String desireOutcome) {
        this.desireOutcome = desireOutcome;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void addAgendaItem(AgendaItem agendaItem) {
        this.agendaItems.add(agendaItem);
    }


    public void addUser(User userAccepted) {
        this.usersAccepted.add(userAccepted);
    }


    public boolean isInvited(String username) {
        for (User i : usersAccepted) {
            if (i.getUserName().equals(username))
                return true;
        }
        return false;
    }

    private String printAcceptedUsers() {
        String outPut = "| ";
        for (User user : usersAccepted) {
            outPut += user.getUserName() + " | ";

        }
        return outPut;
    }

    public String printAgendaItems() {
        String outPut = "";
        for (int i = 0; i < this.agendaItems.size(); i++) {
            outPut += (i + 1) + "- " + this.agendaItems.get(i).getItemToDiscuss() + "\n";
        }
        return outPut;
    }

    private String printDate() {
        Calendar pDate = Calendar.getInstance();
        pDate.setTime(this.date);
        return pDate.get(Calendar.DAY_OF_MONTH) + "/" +
                pDate.get(Calendar.MONTH + 1) + "/" +
                pDate.get(Calendar.YEAR) + "\n";
    }


    @Override
    public String toString() {
        return "TITLE: " + meetingTitle + "\n" +
                "LOCAL: " + local + '\n' +
                "RESPONSIBLE USER: " + responsibleUser.getUserName() + '\n' +
                "DESIRE OUTCOME: " + desireOutcome + '\n' +
                "DATE: " + printDate() +
                "DURATION: " + duration + " minutes\n" +
                "INVITATIONS: " + printAcceptedUsers() + "\n" +
                "AGENDA ITEMS:\n" + printAgendaItems();
    }
}
