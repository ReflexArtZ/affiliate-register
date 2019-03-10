package de.reflex.chaturbate.fakeCredentials;

import de.reflex.chaturbate.ChaturbateControl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class FakeCredsSupport {
    private ArrayList<ChaturbateAccount> chaturbateAccounts;


    private ArrayList<String> passwords;
    private ArrayList<String> userNames;

    public FakeCredsSupport(Path userFilePath, Path passFilePath) throws IOException {

        chaturbateAccounts = new ArrayList<>();

        userNames = loadUsersNames(userFilePath);
        passwords = loadPasswords(passFilePath);
        loadAccounts();
    }

    public ChaturbateAccount getRandomAccount() {
        ChaturbateAccount account = chaturbateAccounts.get(new Random().nextInt(chaturbateAccounts.size()));
        chaturbateAccounts.remove(account);
        return account;
    }

    private ArrayList<String> loadUsersNames(Path filePath) throws IOException {
        ArrayList<String> users = new ArrayList<>();
        Files.lines(filePath).forEach(users::add);
        return users;
    }

    private ArrayList<String> loadPasswords(Path filePath) throws IOException {
        ArrayList<String> pass = new ArrayList<>();
        Files.lines(filePath).forEach(pass::add);
        return pass;
    }

    private void loadAccounts() {
        for (int z = 0; z < Math.min(userNames.size(), passwords.size()); z++) {
            Random random = new Random();
            final String genderTypes = "msf";

            String pass = passwords.get(random.nextInt(passwords.size()));
            passwords.remove(pass);
            String user = userNames.get(random.nextInt(userNames.size()));
            passwords.remove(user);
            int birthday_day = random.nextInt(29) + 1;
            int birthday_month = random.nextInt(11) + 1;
            int birthday_year = random.nextInt(50) + 1950;
            char gender = genderTypes.charAt(random.nextInt(3));

            int userLength = user.length();

            for (int i = user.length(); i < userLength + random.nextInt(5); i++) {
                if (random.nextInt(2) == 0) {   // 33.3% chance
                    user += String.valueOf(birthday_day) + String.valueOf(birthday_month) + String.valueOf(birthday_year) + String.valueOf(random.nextInt(9));
                } else {
                    char c = (char) (random.nextInt(26) + 'a');
                    char c1 = (char) (random.nextInt(26) + 'a');
                    user += String.valueOf(c1) + String.valueOf(c) + String.valueOf(random.nextInt(9));
                }
            }

            int add = 0;

            if (pass.length() < 10) add = 10 - pass.length();
            add += random.nextInt(5);
            int l = pass.length();

            for (int i = pass.length(); i < l + add; i++) {
                char c = (char) (random.nextInt(26) + 'a');
                char c1 = (char) (random.nextInt(26) + 'a');
                pass += String.valueOf(c1) + String.valueOf(c) + String.valueOf(random.nextInt(9));
            }


            chaturbateAccounts.add(new ChaturbateAccount(user, pass, null, birthday_day, birthday_month, birthday_year, gender));
        }
    }
}
