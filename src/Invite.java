/**
 * Created by Ricardo on 15/10/2014.
 */
public class Invite {
    protected Meeting meeting;
    protected boolean decision;
    protected User invitedUser;

    public Invite(Meeting meeting, boolean decision, User invitedUser) {
        this.meeting = meeting;
        this.decision = decision;
        this.invitedUser = invitedUser;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    @Override
    public String toString() {
        return "Invite: " +'\n'+
                "Meeting: " + meeting +'\n'+
                "Decision: " + decision +'\n'+
                "Invited User: " + invitedUser;
    }
}
