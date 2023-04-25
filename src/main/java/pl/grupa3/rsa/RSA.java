package pl.grupa3.rsa;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private BigInteger p;
    private BigInteger q;

    private BigInteger n;

    private BigInteger euler;

    private BigInteger e;

    private BigInteger generateRandomPrimeNumber(int bitNumbers) {
        BigInteger number;
        do {
            number = new BigInteger(bitNumbers, new Random());
        } while (!number.isProbablePrime(100));
        return number;
    }

    private BigInteger generateRandomRelativelyPrimeNumber(BigInteger limit) {
        BigInteger result;
        do {
            result = new BigInteger(limit.bitLength(), new Random());
        } while (result.gcd(limit).compareTo(BigInteger.ONE) != 0 || result.compareTo(limit) > 0);
        return result;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getEuler() {
        return euler;
    }

    RSA() {
        do {
            p = generateRandomPrimeNumber(500);
            q = generateRandomPrimeNumber(510);
        } while (p.compareTo(q) == 0);
        n = p.multiply(q);
        euler = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
        e = generateRandomRelativelyPrimeNumber(euler);
//        System.out.println(n);
//        System.out.println(euler);
//        System.out.println(e);
    }
}
