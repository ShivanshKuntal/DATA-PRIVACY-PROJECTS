import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;

public class TripleDesLab3{

    // DES encryption/decryption
    public static byte[] desOperation(byte[] data, byte[] key, int mode) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(mode, secretKey);
        return cipher.doFinal(data);
    }

    // Triple DES Encrypt: EDE mode (Encrypt-Decrypt-Encrypt)
    public static byte[] tripleDESEncrypt(byte[] data, byte[] key1, byte[] key2, byte[] key3) throws Exception {
        byte[] step1 = desOperation(data, key1, Cipher.ENCRYPT_MODE);
        byte[] step2 = desOperation(step1, key2, Cipher.DECRYPT_MODE);
        return desOperation(step2, key3, Cipher.ENCRYPT_MODE);
    }

    // Triple DES Decrypt: DED mode (Decrypt-Encrypt-Decrypt)
    public static byte[] tripleDESDecrypt(byte[] data, byte[] key1, byte[] key2, byte[] key3) throws Exception {
        byte[] step1 = desOperation(data, key3, Cipher.DECRYPT_MODE);
        byte[] step2 = desOperation(step1, key2, Cipher.ENCRYPT_MODE);
        return desOperation(step2, key1, Cipher.DECRYPT_MODE);
    }

    public static void main(String[] args) throws Exception {
        // Input image
        File inputFile = new File("passport.jpg");
        byte[] imageBytes = Files.readAllBytes(inputFile.toPath());

        // 3 separate 8-byte DES keys
        byte[] key1 = "12345678".getBytes();  // 8 bytes
        byte[] key2 = "abcdefgh".getBytes();
        byte[] key3 = "87654321".getBytes();

        // Encrypt
        byte[] encryptedBytes = tripleDESEncrypt(imageBytes, key1, key2, key3);
        FileOutputStream encOut = new FileOutputStream("encrypted_passport.jpg");
        encOut.write(encryptedBytes);
        encOut.close();
        System.out.println("Image encrypted and saved as: encrypted_passport.jpg");

        // Decrypt
        byte[] decryptedBytes = tripleDESDecrypt(encryptedBytes, key1, key2, key3);
        FileOutputStream decOut = new FileOutputStream("decrypted_passport.jpg");
        decOut.write(decryptedBytes);
        decOut.close();
        System.out.println("Image decrypted and saved as: decrypted_passport.jpg");

        // Metadata
        System.out.println("\n--- Encryption Metadata ---");
        System.out.println("Input File: " + inputFile.getName());
        System.out.println("Encrypted File: encrypted_passport.jpg");
        System.out.println("Decrypted File: decrypted_passport.jpg");
        System.out.println("3DES Keys used:");
        System.out.println("Key1: " + new String(key1));
        System.out.println("Key2: " + new String(key2));
        System.out.println("Key3: " + new String(key3));
    }
}
