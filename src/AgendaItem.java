import java.util.ArrayList;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class AgendaItem {
    protected String ItemToDiscuss;
    protected ArrayList<String> chat;

    public AgendaItem(String itemToDiscuss) {
        ItemToDiscuss = itemToDiscuss;
        chat = new ArrayList<String>();
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

    @Override
    public String toString() {
        return "AgendaItem: " + '\n' +
                "Item to Discuss: " + ItemToDiscuss;
    }
}
