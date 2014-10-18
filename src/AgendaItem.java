import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class AgendaItem implements Serializable {
    protected String ItemToDiscuss;
    protected ArrayList<String> chat;
    protected ActionItem actionItem;

    public AgendaItem(String itemToDiscuss) {
        ItemToDiscuss = itemToDiscuss;
        chat = new ArrayList<String>();
        actionItem = null;
    }

    public String getItemToDiscuss() {
        return ItemToDiscuss;
    }

    public void setItemToDiscuss(String itemToDiscuss) {
        ItemToDiscuss = itemToDiscuss;
    }

    public ArrayList<String> getChat() {
        return chat;
    }

    public void setChat(ArrayList<String> chat) {
        this.chat = chat;
    }

    public ActionItem getActionItem() {
        return actionItem;
    }

    public void setActionItem(ActionItem actionItem) {
        this.actionItem = actionItem;
    }

    @Override
    public String toString() {
        return "AgendaItem: " + '\n' +
                "Item to Discuss: " + ItemToDiscuss;
    }
}
