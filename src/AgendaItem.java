/**
 * Created by Ricardo on 15/10/2014.
 */
public class AgendaItem {
    protected String ItemToDiscuss;

    public AgendaItem(String itemToDiscuss) {
        ItemToDiscuss = itemToDiscuss;
    }

    public String getItemToDiscuss() {
        return ItemToDiscuss;
    }

    public void setItemToDiscuss(String itemToDiscuss) {
        ItemToDiscuss = itemToDiscuss;
    }

    @Override
    public String toString() {
        return "AgendaItem: " + '\n' +
                "Item to Discuss: " + ItemToDiscuss;
    }
}
