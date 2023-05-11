package pl.grupa3.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RSA {
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger euler;
    private BigInteger e;
    private BigInteger d;

    private final int size = 30;
    private final byte[] delimiter = "¡¿., asdt¡¿".getBytes();

    private byte[][] splitBytes(byte[] bytesToSplit, byte[] delimiters) {
        List<byte[]> byteList = new ArrayList<>();
        int startIndex = 0;
        int delimiterIndex = -1;
        while ((delimiterIndex = indexOfDelimiter(bytesToSplit, delimiters, startIndex)) != -1) {
            int length = delimiterIndex - startIndex;
            byte[] subArray = Arrays.copyOfRange(bytesToSplit, startIndex, startIndex + length);
            byteList.add(subArray);
            startIndex = delimiterIndex + delimiters.length;
        }
        byte[] lastSubArray = Arrays.copyOfRange(bytesToSplit, startIndex, bytesToSplit.length);
        if (lastSubArray.length > 0) {
            byteList.add(lastSubArray);
        }
        return byteList.toArray(new byte[0][0]);
    }
    private int indexOfDelimiter(byte[] bytes, byte[] delimiters, int startIndex) {
        for (int i = startIndex; i <= bytes.length - delimiters.length; i++) {
            boolean found = true;
            for (int j = 0; j < delimiters.length; j++) {
                if (bytes[i + j] != delimiters[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }
    private byte[] listToArray(List<Byte> list) {
        byte[] arr = new byte[list.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
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

    public BigInteger getEuler() {
        return euler;
    }

    private void generatePublicKey() {
        n = p.multiply(q);
//        e = new BigInteger("1699145693137051146996061");
        e = generateRandomRelativelyPrimeNumber(euler);
    }

    private void generatePrivateKey() {
        d = e.modInverse(euler);
    }

    public void generateKey() {
        do {
//            p = new BigInteger("1313131345364123");
//            q = new BigInteger("4294967291");
            p = generateRandomPrimeNumber(256);
            q = generateRandomPrimeNumber(256);
        } while (p.compareTo(q) == 0);
        euler = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
        generatePublicKey();
        generatePrivateKey();
    }

    public List<Byte> encrypt(List<Byte> msg) {
        List<Byte> encryptedMsg = new ArrayList<>();
        byte[] arr = new byte[size];
        while (msg.size() % size != 0) {
            msg.add((byte) 0);
        }
        int added = 0;
        for (Byte aByte : msg) {
            if (added < size) {
                arr[added] = aByte;
                added++;
            }
            if (added == size) {
                BigInteger msgBlock = new BigInteger(arr);
                if (msgBlock.compareTo(n) > 0) {
                    System.out.println("aaaaaaaaaaaa");
                    return null;
                }
                byte[] enc = msgBlock.modPow(e, n).toByteArray();
                for (byte b : enc) {
                    encryptedMsg.add(b);
                }
                for (byte b : delimiter) {
                    encryptedMsg.add(b);
                }
                added = 0;
            }
        }
        return encryptedMsg;
    }

    public List<Byte> decrypt(List<Byte> msg) {
        List<Byte> decryptedMsg = new ArrayList<>();
        byte[] msgTab = listToArray(msg);
        byte[][] arr = splitBytes(msgTab, delimiter);
        for (byte[] a : arr) {
            BigInteger b = new BigInteger(a);
            byte[] rsaDec = b.modPow(d, n).toByteArray();
            for (byte c : rsaDec) {
                decryptedMsg.add(c);
            }
        }
        for (int i = decryptedMsg.size() - 1; i >= 0; i--) {
            if (decryptedMsg.get(i) == (byte)0) {
                decryptedMsg.remove(i);
            }
        }
        return decryptedMsg;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getD() {
        return d;
    }

    public BigInteger getE() {
        return e;
    }
}
