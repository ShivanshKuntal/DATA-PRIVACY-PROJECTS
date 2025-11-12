import java.io.*;

public class ModifiedDES64 {

    // 48-bit Expansion Table (DES-style)
    static int[] E_TABLE = {
        32, 1, 2, 3, 4, 5, 4, 5,
        6, 7, 8, 9, 8, 9, 10, 11,
        12, 13, 12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21, 20, 21,
        22, 23, 24, 25, 24, 25, 26, 27,
        28, 29, 28, 29, 30, 31, 32, 1
    };

    // S-Box: Two rows, 16 columns (for 6-bit input => 4-bit output)
    static int[][] S_BOX = {
        {14, 4, 13, 1, 2, 15, 11, 8,
         3, 10, 6, 12, 5, 9, 0, 7},
        {0, 15, 7, 4, 14, 2, 13, 1,
         10, 6, 12, 11, 9, 5, 3, 8}
    };

    // 32-bit P-permutation
    static int[] P_TABLE = {
        16, 7, 20, 21, 29, 12, 28, 17,
        1, 15, 23, 26, 5, 18, 31, 10,
        2, 8, 24, 14, 32, 27, 3, 9,
        19, 13, 30, 6, 22, 11, 4, 25
    };

    // Get 48-bit round key from 64-bit key
    static String getSubkey(String key64, int round) {
        // Simple key scheduling - rotate left by round number
        String rotated = key64.substring(round) + key64.substring(0, round);
        return rotated.substring(0, 48);
    }

    // Permutation function
    static String permute(String input, int[] table) {
        StringBuilder result = new StringBuilder();
        for (int i : table) {
            result.append(input.charAt(i - 1));
        }
        return result.toString();
    }

    // XOR operation on binary strings
    static String xor(String a, String b) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            res.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return res.toString();
    }

    // S-Box substitution for 6-bit input chunks
    static String sBoxSubstitute(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i += 6) {
            String chunk = input.substring(i, Math.min(i + 6, input.length()));
            // Pad if needed
            while (chunk.length() < 6) chunk = chunk + "0";
            int row = Integer.parseInt(chunk.charAt(0) + "" + chunk.charAt(5), 2);
            int col = Integer.parseInt(chunk.substring(1, 5), 2);
            int val = S_BOX[row % 2][col % 16];
            result.append(String.format("%4s", Integer.toBinaryString(val)).replace(' ', '0'));
        }
        return result.toString();
    }

    // f-function: E -> XOR -> S-box -> P
    static String feistel(String halfBlock, String subkey) {
        String expanded = permute(halfBlock, E_TABLE);
        String xored = xor(expanded, subkey);
        String substituted = sBoxSubstitute(xored);
        return permute(substituted, P_TABLE);
    }

    // Encrypt a 64-bit block
    static String encryptBlock(String block64, String key64, int rounds) {
        String L = block64.substring(0, 32);
        String R = block64.substring(32);
        for (int i = 0; i < rounds; i++) {
            String tempL = R;
            String subkey = getSubkey(key64, i);
            String tempR = xor(L, feistel(R, subkey));
            L = tempL;
            R = tempR;
        }
        return L + R;
    }

    // Decrypt a 64-bit block
    static String decryptBlock(String block64, String key64, int rounds) {
        String L = block64.substring(0, 32);
        String R = block64.substring(32);
        for (int i = rounds - 1; i >= 0; i--) {
            String subkey = getSubkey(key64, i);
            String tempR = L;
            String tempL = xor(R, feistel(L, subkey));
            R = tempR;
            L = tempL;
        }
        return L + R;
    }

    // Read file and convert to binary
    static String readBinaryFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            for (char c : line.toCharArray()) {
                sb.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
            }
        }
        br.close();
        return sb.toString();
    }

    // Write binary to file
    static void writeBinaryToFile(String filename, String binary, boolean asChars) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        if (asChars) {
            for (int i = 0; i < binary.length(); i += 8) {
                String byteStr = binary.substring(i, Math.min(i + 8, binary.length()));
                bw.write((char) Integer.parseInt(byteStr, 2));
            }
        } else {
            bw.write(binary);
        }
        bw.close();
    }

    // Convert binary string to readable text
    static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String byteStr = binary.substring(i, Math.min(i + 8, binary.length()));
            text.append((char) Integer.parseInt(byteStr, 2));
        }
        return text.toString();
    }

    public static void main(String[] args) {
        try {
            int rounds = 2;
            String key64 = "0111100110011001010110101100111100110011001100110011001100110011";

            // Read and pad plaintext
            String plaintextBinary = readBinaryFromFile("plaintext.txt");
            String originalPlaintext = binaryToText(plaintextBinary);
            System.out.println("Original Plaintext:\n" + originalPlaintext + "\n");

            while (plaintextBinary.length() % 64 != 0) {
                plaintextBinary += "0";
            }

            // Encrypt
            StringBuilder encryptedBinary = new StringBuilder();
            for (int i = 0; i < plaintextBinary.length(); i += 64) {
                String block = plaintextBinary.substring(i, Math.min(i + 64, plaintextBinary.length()));
                encryptedBinary.append(encryptBlock(block, key64, rounds));
            }
            writeBinaryToFile("encrypted.txt", encryptedBinary.toString(), false);
            System.out.println("Encrypted Binary:\n" + encryptedBinary.toString() + "\n");

            // Decrypt
            StringBuilder decryptedBinary = new StringBuilder();
            for (int i = 0; i < encryptedBinary.length(); i += 64) {
                String block = encryptedBinary.substring(i, Math.min(i + 64, encryptedBinary.length()));
                decryptedBinary.append(decryptBlock(block, key64, rounds));
            }
            writeBinaryToFile("decrypted.txt", decryptedBinary.toString(), true);
            
            // Remove padding from decrypted text
            String decryptedText = binaryToText(decryptedBinary.toString()).replaceAll("\0+$", "");
            System.out.println("Decrypted Text:\n" + decryptedText + "\n");

            System.out.println("64-bit Modified DES encryption & decryption complete.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}