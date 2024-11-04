package team1.BE.seamless.util.auth;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesEncrypt {

    private SecretKey secretKey;
    private String IV_STRING;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public AesEncrypt(@Value("${code.secretKey}") String secretString,
        @Value("${code.vector}") String vector
        ) {
        secretKey = new SecretKeySpec(secretString.getBytes(), "AES");
        IV_STRING=vector;
    }

    // 암호화
    public String encrypt(String data) {
        byte[] encryptedBytes = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(IV_STRING.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,iv);
            encryptedBytes = cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 복호화
    public String decrypt(String encryptedData) {
        byte[] decryptedBytes = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec iv = new IvParameterSpec(IV_STRING.getBytes()); // IV 설정
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decryptedBytes);
    }
}
