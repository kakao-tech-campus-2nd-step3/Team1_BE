package team1.BE.seamless.util.auth;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesEncrypt {

    private SecretKey secretKey;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public AesEncrypt(@Value("${code.secretKey}") String secretString) {
        secretKey = new SecretKeySpec(secretString.getBytes(), "AES");
    }

    // 암호화
    public String encrypt(String data) {
        byte[] encryptedBytes = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedBytes = cipher.doFinal(data.getBytes());
        } catch (Exception e) {

        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 복호화
    public String decrypt(String encryptedData) {
        byte[] decryptedBytes = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        } catch (Exception e) {

        }

        return new String(decryptedBytes);
    }
}
