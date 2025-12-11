import java.util.Scanner;

public class ElGamalSignatureTable {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input prime P and generator g
        System.out.print("Enter a large prime P: ");
        int P = scanner.nextInt();

        System.out.print("Enter a generator g: ");
        int g = scanner.nextInt();

        int Hm = 4;  // Fixed hash of message
        int K = 5;   // Random nonce, must be coprime with P-1

        // Validate that K and (P-1) are coprime
        if (gcd(K, P - 1) != 1) {
            System.out.println("K and P-1 must be coprime. Choose another value of K.");
            return;
        }

        int r = modExp(g, K, P);              // r = g^K mod P
        int Kinv = modInverse(K, P - 1);      // K inverse mod (P-1)

        // Table header
        System.out.printf("%-18s %-28s %-18s %-18s%n",
                "Private Key (x)",
                "Public Key (y = g^x mod P)",
                "Signature (r)",
                "Signature (s)");
        System.out.println("--------------------------------------------------------------------------------------");

        // Loop through all valid private keys
        for (int x = 2; x < P - 1; x++) {
            int y = modExp(g, x, P);  // y = g^x mod P

            // s = (K^-1 * (H(m) - x * r)) mod (P-1)
            int temp = (Hm - (x * r)) % (P - 1);
            if (temp < 0) temp += (P - 1);
            int s = (Kinv * temp) % (P - 1);

            // Display row
            System.out.printf("%-18d %-28d %-18d %-18d%n", x, y, r, s);
        }

        scanner.close();
    }

    // Fast modular exponentiation: (base^exp) mod mod
    public static int modExp(int base, int exp, int mod) {
        int result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1)
                result = (result * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }

    // Modular inverse using extended Euclidean algorithm
    public static int modInverse(int a, int m) {
        int m0 = m, x0 = 0, x1 = 1;
        while (a > 1) {
            int q = a / m;
            int t = m;
            m = a % m;
            a = t;

            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }
        return (x1 + m0) % m0;
    }

    // Greatest common divisor
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
