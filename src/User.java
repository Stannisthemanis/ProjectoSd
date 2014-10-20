import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class User implements Serializable {
    protected String userName; // chave prime
    protected String passWord;
    protected String address;
    protected Date dob;
    protected Number phoneNumber;
    protected String mail;
    protected ArrayList<ActionItem> actionItems;

    public User(String userName, String passWord, String address,
                Date dob, Number phoneNumber, String mail) {
        this.userName = userName;
        this.passWord = passWord;
        this.address = address;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.actionItems = new ArrayList<ActionItem>();
    }


    public boolean testUserName(String un) { // test if UserName already exists
        return (this.userName == un);
    }

    public boolean testLogIn(String un, String pw) {
        return ((this.userName == un) && (this.passWord == pw));
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getAddress() {
        return address;
    }

    public Date getDob() {
        return dob;
    }

    public Number getPhoneNumber() {
        return phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setPhoneNumber(Number phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void addActionItem(ActionItem actionItem) {
        this.actionItems.add(actionItem);
    }

    public ArrayList<ActionItem> getActionItems() {
        return actionItems;
    }

    public String printActionItens() {
        String outPut = "You dont have any action to do";
        int i = 1;
        if (this.actionItems.size() > 0) {
            outPut = "ACTION ITENS:\n";
            for (ActionItem aItem : actionItems) {
                outPut += (i++) + aItem.toString();
            }
        }
        return outPut;
    }


    @Override
    public String toString() {
        return "\nUsername: " + userName +
                "\nPassword: " + passWord +
                "\nAddress: " + address +
                "\nDate of Birthday: " + dob +
                "\nPhone number: " + phoneNumber +
                "\nMail: " + mail;
    }
}
