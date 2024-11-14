package team1.be.seamless.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team1.be.seamless.util.auth.AesEncrypt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AesEncryptTest {

    private final AesEncrypt aesEncrypt;

    @Autowired
    public AesEncryptTest(AesEncrypt aesEncrypt) {
        this.aesEncrypt = aesEncrypt;
    }

    @Test
    void 평문_암호화_후_복호화() {
//        given
        String plane = "평문 테스트";
//        when

        String encode = aesEncrypt.encrypt(plane);
        String decode = aesEncrypt.decrypt(encode);

//        then

        assertEquals(plane, decode);
    }
}