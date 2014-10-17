import java.io.Serializable;
import java.util.ArrayList;
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
    protected Date duration; //hours
    protected ArrayList<Invite> invitations;
    protected ArrayList<AgendaItem> agendaItems;
    protected ArrayList<ActionItem> actionItems;

    public Meeting(String meetingTitle, String local, User responsibleUser, String desireOutcome, Date date, Date duration,
                   ArrayList<AgendaItem> agendaItems) {
        this.meetingTitle = meetingTitle;
        this.local = local;
        this.responsibleUser = responsibleUser;
        this.desireOutcome = desireOutcome;
        this.date = date;
        this.duration = duration;
        this.invitations = new ArrayList<Invite>();
        this.agendaItems = agendaItems;
        this.actionItems = new ArrayList<ActionItem>();
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

    public Date getDuration() {
        return duration;
    }

    public ArrayList<Invite> getUsersInvited() {
        return invitations;
    }

    public ArrayList<AgendaItem> getAgendaItems() {
        return agendaItems;
    }

    public ArrayList<ActionItem> getActionItems() {
        return actionItems;
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

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public void setUsersInvited(ArrayList<Invite> invitations) {
        this.invitations = invitations;
    }

    public void setAgendaItems(ArrayList<AgendaItem> agendaItems) {
        this.agendaItems = agendaItems;
    }

    public void setActionItems(ArrayList<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }

    public void addInvite(Invite invite) {
        this.invitations.add(invite);
    }

    public void addActionItem(ActionItem actionItem) {
        this.actionItems.add(actionItem);
    }

    public String printInvitations(ArrayList<Invite> invitations) {
        String outPut = "";
        for (Invite invitation : invitations) {
            outPut += invitation.toString() + '\n';
        }
        return outPut;
    }

    public String printAgendaItems(ArrayList<AgendaItem> agendaItems) {
        String outPut = "";
        for (AgendaItem agendaItem : agendaItems) {
            outPut += agendaItem.toString() + '\n';
        }
        return outPut;
    }

    public String printActionItems(ArrayList<ActionItem> actionItems) {
        String outPut = "";
        for (ActionItem actionItem : actionItems) {
            outPut += actionItem.toString();
        }
        return outPut;
    }

    @Override
    public String toString() {
        return "Meeting info: " + "\n" +
                "Title: " + meetingTitle + "\n" +
                "Local: " + local + '\n' +
                "Responsible User=: " + responsibleUser + '\n' +
                "Desire Outcome: " + desireOutcome + '\n' +
                "Date: " + date + "\n" +
                "Duration: " + duration + "\n" +
                "Invitations: " + printInvitations(this.invitations) + "\n" +
                "Agenda Items: " + printAgendaItems(this.agendaItems) + "\n" +
                "Action Items: " + printActionItems(this.actionItems);
    }
}
