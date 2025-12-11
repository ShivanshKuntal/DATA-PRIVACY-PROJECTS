import java.io.*;

public class HillCipher {
    public static void main(String[] args) throws Exception {
        // Read msg from file
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        String msg = br.readLine().replaceAll("[^A-Z]", "").toUpperCase();  // only uppercase letters
        br.close();

        // Pad msg with X if not multiple of 2
        while (msg.length() % 2 != 0)
            msg += "X";

        // Key: "HILL" → key matrix 2x2
        // H I
        // L L
        String key = "HILL";
        int[][] keyMatrix = new int[2][2];
        int k = 0;
        for (int i=0;i<2; i++)
            for (int j=0;j<2; j++)
                keyMatrix[i][j] = key.charAt(k++)-'A';

        // Encrypt
        StringBuilder cipherText = new StringBuilder();
        for (int i=0;i<msg.length();i+=2) {
            int[] vector = new int[2];
            for (int j = 0; j < 2; j++)
                vector[j] = msg.charAt(i + j) - 'A';

            for (int row = 0; row < 2; row++) {
                int sum = 0;
                for (int col = 0; col < 2; col++)
                    sum += keyMatrix[row][col] * vector[col];
                cipherText.append((char) ((sum % 26) + 'A'));
            }
        }

        // Write encrypted text to file
        BufferedWriter bw = new BufferedWriter(new FileWriter("encrypted.txt"));
        bw.write(cipherText.toString());
        bw.close();

    
        // Matrix:
        // [7  8]
        // [11 11]
        //
        // Determinant = (7*11 - 8*11) = -11 mod 26 = 15
        // Mod inverse of 15 mod 26 = 7
        //
        // Adjoint matrix:
        // [11 -8]
        // [-11 7] → mod 26 → [11 18]
        //                    [15 7]
        //
        // Final inverse matrix = (7 * adj) % 26 → 
        int[][] inverseKey = {
            {25, 22},
            {1, 23}
        };

        // Decrypt
        StringBuilder decrypted = new StringBuilder();
        String cipher = cipherText.toString();
        for (int i=0;i<cipher.length();i+=2) {
            int[] vec = new int[2];
            for (int j = 0; j < 2; j++)
                vec[j] = cipher.charAt(i + j) - 'A';

            for (int row=0; row<2;row++) {
                int sum = 0;
                for (int col=0;col<2; col++)
                    sum += inverseKey[row][col]*vec[col];
                decrypted.append((char) ((sum % 26 + 26) % 26 + 'A')); 
            }
        }

        // Write decrypted text to file
        BufferedWriter bw2 = new BufferedWriter(new FileWriter("decrypted.txt"));
        bw2.write(decrypted.toString());
        bw2.close();

        System.out.println("Encrypted text: "+cipherText);
        System.out.println("Decrypted text: "+decrypted);
    }
}
