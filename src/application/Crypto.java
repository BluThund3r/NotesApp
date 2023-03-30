package application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Crypto {
    private Crypto() {
    }

    public static String encryptPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());

            byte[] resultByteArray = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : resultByteArray) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static boolean compare(String hashPassword, String plainPassword) {
        String resultHashPassword = Crypto.encryptPassword(plainPassword);
        return hashPassword.equals(resultHashPassword);
    }
}
