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
    private final byte[] delimiter = "¡¿., abdsfjkgfsdfdasgdagsdbfdhgndfjkgbaruhsdt¡¿".getBytes();
    public byte[][] divideIntoBlocks(List<Byte> inputList) {
        List<Byte> currentBlock = new ArrayList<>();
        List<List<Byte>> outputList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            currentBlock.add(inputList.get(i));
            if (i == inputList.size() - 1) {
                outputList.add(currentBlock);
                break;
            }
            if (currentBlock.size() >= size && inputList.get(i + 1) <= 0) {
                currentBlock.add(inputList.get(i + 1));
                i++;
            } else if (currentBlock.size() >= size && inputList.get(i + 1) > 0) {
                outputList.add(currentBlock);
                currentBlock = new ArrayList<>();
            }
        }
        byte[][] res = new byte[outputList.size()][];
        for (int i = 0; i < outputList.size(); i++) {
            res[i] = listToArray(outputList.get(i));
        }
        return res;
    }
    private byte[][] splitBytes(byte[] input, byte[] delimiter) {
        List<byte[]> result = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i] == delimiter[0] && i + delimiter.length <= input.length) {
                boolean found = true;
                for (int j = 1; j < delimiter.length; j++) {
                    if (input[i+j] != delimiter[j]) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    byte[] subarray = Arrays.copyOfRange(input, start, i);
                    result.add(subarray);
                    start = i + delimiter.length;
                    i += delimiter.length - 1;
                }
            }
        }
        byte[] finalSubarray = Arrays.copyOfRange(input, start, input.length);
        if (finalSubarray.length > 0) {
            result.add(finalSubarray);
        }
        byte[][] resultArray = new byte[result.size()][];
        for (int i = 0; i < result.size(); i++) {
            resultArray[i] = result.get(i);
        }
        return resultArray;
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
            result = new BigInteger(limit.bitLength() - 10, new Random());
        } while (result.gcd(limit).compareTo(BigInteger.ONE) != 0 || result.compareTo(limit) > 0);
        return result;
    }
    public BigInteger getEuler() {
        return euler;
    }
    private void generatePublicKey() {
        n = p.multiply(q);
        e = generateRandomRelativelyPrimeNumber(euler);
    }

    private void generatePrivateKey() {
        d = e.modInverse(euler);
    }

    public void generateKey() {
        do {
            p = generateRandomPrimeNumber(512);
            q = generateRandomPrimeNumber(512);
        } while (p.compareTo(q) == 0);
        euler = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
        generatePublicKey();
        generatePrivateKey();
    }

    public List<Byte> encrypt(List<Byte> msg) {
        List<Byte> encryptedMsg = new ArrayList<>();
        byte[][] arr = divideIntoBlocks(msg);
        for (byte[] a : arr) {
            BigInteger b = new BigInteger(a);
            if (b.compareTo(n) > 0) {
                return null;
            }
            byte[] enc = b.modPow(e, n).toByteArray();
            for (byte c : enc) {
                encryptedMsg.add(c);
            }
            for (byte c : delimiter) {
                encryptedMsg.add(c);
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

    public void loadKey(int type, byte[] key) {
        if (type == 0) {
            byte[][] publicKey = splitBytes(key, delimiter);
            n = new BigInteger(publicKey[0]);
            e = new BigInteger(publicKey[1]);
        } else {
            d = new BigInteger(key);
        }
    }

}
