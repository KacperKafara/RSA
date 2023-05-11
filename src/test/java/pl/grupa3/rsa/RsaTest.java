package pl.grupa3.rsa;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RsaTest {

    private byte[] listToArray(List<Byte> list) {
        byte[] arr = new byte[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    @Test
    void eNumberTest() {
        for (int i = 0; i < 100; i++) {
            RSA rsa = new RSA();
            rsa.generateKey();
            assertTrue(rsa.getEuler().compareTo(rsa.getE()) > 0);
            assertEquals(0, rsa.getEuler().gcd(rsa.getE()).compareTo(BigInteger.ONE));
        }
    }

    @Test
    void encryptionAndDecryptionTest() {
        RSA rsa = new RSA();
        rsa.generateKey();
        byte[] file = "wiadomosc".getBytes();
        List<Byte> fileList = new ArrayList<>();
        for (byte b : file) {
            fileList.add(b);
        }
        List<Byte> encrypted = rsa.encrypt(fileList);
        List<Byte> decrypted = rsa.decrypt(encrypted);
        assertEquals(new String(listToArray(decrypted)), new String(file));
    }
}
