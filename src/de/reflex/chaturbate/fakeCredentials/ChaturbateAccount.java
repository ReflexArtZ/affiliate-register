package de.reflex.chaturbate.fakeCredentials;

public class ChaturbateAccount {
    private String username;
    private String password;
    private String email;       // ToDo
    private int birthday_day;
    private int birthday_month;
    private int birthday_year;  // 2000 and below
    private char gender;    // m, f, s

    public ChaturbateAccount(String username, String password, String email, int birthday_day, int birthday_month, int birthday_year, char gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday_day = birthday_day;
        this.birthday_month = birthday_month;
        this.birthday_year = birthday_year;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getBirthday_day() {
        return birthday_day;
    }

    public int getBirthday_month() {
        return birthday_month;
    }

    public int getBirthday_year() {
        return birthday_year;
    }

    public char getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "ChaturbateAccount{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", birthday_day=" + birthday_day +
                ", birthday_month=" + birthday_month +
                ", birthday_year=" + birthday_year +
                ", gender=" + gender +
                '}';
    }
}
