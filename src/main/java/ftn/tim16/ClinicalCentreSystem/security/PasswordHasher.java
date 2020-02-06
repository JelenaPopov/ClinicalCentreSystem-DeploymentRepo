package ftn.tim16.ClinicalCentreSystem.security;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordHasher {

    public static String encodeBCrypt(String plaintextPassword) {
        return BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
    }

    public static boolean doesPasswordMatch(String plaintextPassword, String hashedPassword) {
        return (BCrypt.checkpw(plaintextPassword, hashedPassword));
    }

}
