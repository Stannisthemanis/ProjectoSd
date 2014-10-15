import java.util.Date;

/**
 * Created by Ricardo on 15/10/2014.
 */
public class User {
    protected String userName; // chave prime
    protected String passWord;
    protected String address;
    protected Date dob;
    protected Number phoneNumber;
    protected String mail;

    public User(String userName, String passWord, String address,
                Date dob, Number phoneNumber, String mail) {
        this.userName = userName;
        this.passWord = passWord;
        this.address = address;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
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

    @Override
    public String toString() {
        return "User Info: " +
                "Username: " + userName + '\n' +
                "Password: " + passWord + '\n' +
                "Address: "  + address + '\n' +
                "Date of Birthday: " + dob +
                "Phone number: " + phoneNumber +
                "Mail: " + mail;
    }
}
