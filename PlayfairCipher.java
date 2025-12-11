import java.io.*;
import java.util.*;

public class PlayfairCipher {

    private char[][] matrix = new char[5][5];
    private String key;

    public PlayfairCipher(String key) {
        this.key = prepareKey(key);
        createMatrix();
    }

    private String prepareKey(String key) {
        key = key.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder sb = new StringBuilder();
        Set<Character> seen = new HashSet<>();
        for (char c : key.toCharArray()) {
            if (!seen.contains(c)) {
                seen.add(c);
                sb.append(c);
            }
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') continue;
            if (!seen.contains(c)) {
                seen.add(c);
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void createMatrix() {
        int index = 0;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                matrix[i][j] = key.charAt(index++);
    }

    private String preprocessText(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder sb = new StringBuilder(text);
        for (int i = 0; i < sb.length() - 1; i += 2) {
            if (sb.charAt(i) == sb.charAt(i + 1))
                sb.insert(i + 1, 'X');
        }
        if (sb.length() % 2 != 0)
            sb.append('X');
        return sb.toString();
    }

    private String process(String text, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        text = preprocessText(text);
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i), b = text.charAt(i + 1);
            int[] posA = findPosition(a), posB = findPosition(b);
            if (posA[0] == posB[0]) {
                result.append(matrix[posA[0]][(posA[1] + (encrypt ? 1 : 4)) % 5]);
                result.append(matrix[posB[0]][(posB[1] + (encrypt ? 1 : 4)) % 5]);
            } else if (posA[1] == posB[1]) {
                result.append(matrix[(posA[0] + (encrypt ? 1 : 4)) % 5][posA[1]]);
                result.append(matrix[(posB[0] + (encrypt ? 1 : 4)) % 5][posB[1]]);
            } else {
                result.append(matrix[posA[0]][posB[1]]);
                result.append(matrix[posB[0]][posA[1]]);
            }
        }
        return result.toString();
    }

    private int[] findPosition(char c) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (matrix[i][j] == c)
                    return new int[]{i, j};
        return null;
    }

    public String encrypt(String plaintext) {
        return process(plaintext, true);
    }

    public String decrypt(String ciphertext) {
        return process(ciphertext, false);
    }

    // File handling methods
    public static String readFile(String filename) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filename)));
    }

    public static void writeFile(String filename, String content) throws IOException {
        java.nio.file.Files.write(java.nio.file.Paths.get(filename), content.getBytes());
    }

    public static void main(String[] args) {
        try {
            String key = "MONARCHY"; // You can change the key
            PlayfairCipher cipher = new PlayfairCipher(key);

            String plaintext = readFile("input.txt");
            String encrypted = cipher.encrypt(plaintext);
            String decrypted = cipher.decrypt(encrypted);

            writeFile("encrypted.txt", encrypted);
            writeFile("decrypted.txt", decrypted);

            System.out.println("Original: " + plaintext);
            System.out.println("Encrypted: " + encrypted);
            System.out.println("Decrypted: " + decrypted);

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
