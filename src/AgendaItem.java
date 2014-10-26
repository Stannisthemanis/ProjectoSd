import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class AgendaItem implements Serializable {
    protected String ItemToDiscuss;
    protected ArrayList<String> chat;
    protected ArrayList<String> clientsOnChat;
    protected String keyDecision;

    public AgendaItem(String itemToDiscuss) {
        ItemToDiscuss = itemToDiscuss;
        chat = new ArrayList<String>();
        keyDecision = null;
        clientsOnChat = new ArrayList<String>();
    }

    public String getItemToDiscuss() {
        return ItemToDiscuss;
    }

    public String getUsersOnChat() {
        String aux = "";
        for (String s : clientsOnChat) {
            aux += s + "\n";
        }
        return aux;
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

    public String getKeyDecision() {
        return keyDecision;
    }

    public void setKeyDecision(String keyDecision) {
        this.keyDecision = keyDecision;
    }

    public String getMessages() {
        String chatHistory = null;
        for (String s : this.chat) {
            if (chatHistory == null)
                chatHistory = "";
            chatHistory += s + "\n";
        }
        if (chatHistory == null)
            chatHistory = "There is no messages in this agenda item";
        return chatHistory;
    }

    public void addMessage(String message) {
        this.chat.add(message);
    }

    public boolean isOnChat(String user) {
        for (String s : clientsOnChat) {
            if (user.equals(s))
                return true;
        }
        return false;
    }

    public int addClientToChat(String newClient) {
        for (String s : clientsOnChat) {
            if (s.equals(newClient))
                return 0;
        }
        this.clientsOnChat.add(newClient);
        return 1;
    }

    public void removeClientFromChat(String newClient) {
        this.clientsOnChat.remove(newClient);
    }

    @Override
    public String toString() {
        return "AgendaItem: " + '\n' +
                "Item to Discuss: " + ItemToDiscuss;
    }
}
