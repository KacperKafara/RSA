package pl.grupa3.rsa;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RsaTest {

    @Test
    void eNumberTest() {
        for (int i = 0; i < 100; i++) {
            RSA rsa = new RSA();
            assertTrue(rsa.getEuler().compareTo(rsa.getE()) > 0);
            assertEquals(0, rsa.getEuler().gcd(rsa.getE()).compareTo(BigInteger.ONE));
        }
    }
}
