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
    protected ArrayList<Invite> invitations;
    protected ArrayList<AgendaItem> agendaItems;
    protected ArrayList<ActionItem> actionItems;

    public Meeting(String meetingTitle, String local, User responsibleUser, String desireOutcome, Date date, int duration,
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

    public int getDuration() {
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

    public void setDuration(int duration) {
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

    public boolean isInvited(String username) {
        for (Invite i : invitations) {
            i.getInvitedUser().getUserName().equals(username);
            return true;
        }
        return false;
    }

    private String printInvitations(ArrayList<Invite> invitations) {
        String outPut = "| ";
        for (Invite invitation : invitations) {
            outPut += invitation.getInvitedUser().getUserName() + " | ";
        }
        return outPut;
    }

    private String printAgendaItems(ArrayList<AgendaItem> agendaItems) {
        String outPut = "";
        for (int i = 0; i < agendaItems.size(); i++) {
            outPut += (i + 1) + "- " + agendaItems.get(i).getItemToDiscuss() + "\n";
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
                "INVITATIONS: " + printInvitations(this.invitations) + "\n" +
                "AGENDA ITEMS:\n" + printAgendaItems(this.agendaItems);
    }
}
