import java.io.Serializable;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class ActionItem implements Serializable {
    protected String name;
    protected String userResponsible;
    protected String keyDecision;
    protected boolean completed;

    public ActionItem(String name, String userResponsible, String keyDecision) {
        this.name = name;
        this.userResponsible = userResponsible;
        this.keyDecision = keyDecision;
        completed = false;
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

    public String getKeyDecision() {
        return keyDecision;
    }

    public void setKeyDecision(String keyDecision) {
        this.keyDecision = keyDecision;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "ActionItem: " +
                "Action item name: " + name + '\n' +
                "Responsible User: " + userResponsible + '\n';
    }
}
