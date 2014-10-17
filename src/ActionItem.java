import java.io.Serializable;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class ActionItem implements Serializable {
    protected String name;
    protected String userResponsible;
    protected AgendaItem agendaItem; // ecah actionItem is related to one agendaItem

    public ActionItem(String name, String userResponsible, AgendaItem agendaItem) {
        this.name = name;
        this.userResponsible = userResponsible;
        this.agendaItem = agendaItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserResponsible() {
        return userResponsible;
    }

    public void setUserResponsible(String userResponsible) {
        this.userResponsible = userResponsible;
    }

    public AgendaItem getAgendaItem() {
        return agendaItem;
    }

    public void setAgendaItem(AgendaItem agendaItem) {
        this.agendaItem = agendaItem;
    }

    @Override
    public String toString() {
        return "ActionItem: " +
                "Action item name: " + name + '\n' +
                "Responsible User: " + userResponsible + '\n' +
                "Related Agenda Item: " + agendaItem;
    }
}
