import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class AESImage {

    private static final int BLOCK_SIZE = 16;
    private static final long SEED = 12345L;
    private static final int ROUNDS = 3;

    public static void main(String[] args) throws IOException {
        BufferedImage input = ImageIO.read(new File("passport.jpg"));

        BufferedImage encrypted = deepScramble(input, SEED, ROUNDS);
        ImageIO.write(encrypted, "jpg", new File("encrypted.jpg"));

        BufferedImage decrypted = deepUnscramble(encrypted, SEED, ROUNDS);
        ImageIO.write(decrypted, "jpg", new File("decrypted.jpg"));

        System.out.println("Advanced scrambling done.");
    }

    public static BufferedImage deepScramble(BufferedImage image, long seed, int rounds) {
        BufferedImage result = image;
        for (int i = 0; i < rounds; i++) {
            result = scrambleOnce(result, seed + i);
        }
        return result;
    }

    public static BufferedImage deepUnscramble(BufferedImage image, long seed, int rounds) {
        BufferedImage result = image;
        for (int i = rounds - 1; i >= 0; i--) {
            result = unscrambleOnce(result, seed + i);
        }
        return result;
    }

    private static BufferedImage scrambleOnce(BufferedImage image, long seed) {
        int width = image.getWidth();
        int height = image.getHeight();
        int blocksX = width / BLOCK_SIZE;
        int blocksY = height / BLOCK_SIZE;
        int totalBlocks = blocksX * blocksY;

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalBlocks; i++) indices.add(i);
        Collections.shuffle(indices, new Random(seed));

        Random rand = new Random(seed);
        BufferedImage scrambled = new BufferedImage(width, height, image.getType());

        for (int i = 0; i < totalBlocks; i++) {
            int srcIndex = i;
            int dstIndex = indices.get(i);

            int srcX = (srcIndex % blocksX) * BLOCK_SIZE;
            int srcY = (srcIndex / blocksX) * BLOCK_SIZE;
            int dstX = (dstIndex % blocksX) * BLOCK_SIZE;
            int dstY = (dstIndex / blocksX) * BLOCK_SIZE;

            boolean flipH = rand.nextBoolean();
            boolean flipV = rand.nextBoolean();

            int[][] block = getBlock(image, srcX, srcY);
            shuffleBlockPixels(block, rand);
            if (flipH) flipBlockHorizontal(block);
            if (flipV) flipBlockVertical(block);

            writeBlock(scrambled, dstX, dstY, block);
        }

        return scrambled;
    }

    private static BufferedImage unscrambleOnce(BufferedImage image, long seed) {
        int width = image.getWidth();
        int height = image.getHeight();
        int blocksX = width / BLOCK_SIZE;
        int blocksY = height / BLOCK_SIZE;
        int totalBlocks = blocksX * blocksY;

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalBlocks; i++) indices.add(i);
        List<Integer> shuffled = new ArrayList<>(indices);
        Collections.shuffle(shuffled, new Random(seed));

        Random rand = new Random(seed);
        BufferedImage unscrambled = new BufferedImage(width, height, image.getType());

        for (int i = 0; i < totalBlocks; i++) {
            int dstIndex = i;
            int srcIndex = shuffled.get(i);

            int srcX = (srcIndex % blocksX) * BLOCK_SIZE;
            int srcY = (srcIndex / blocksX) * BLOCK_SIZE;
            int dstX = (dstIndex % blocksX) * BLOCK_SIZE;
            int dstY = (dstIndex / blocksX) * BLOCK_SIZE;

            boolean flipH = rand.nextBoolean();
            boolean flipV = rand.nextBoolean();

            int[][] block = getBlock(image, srcX, srcY);
            if (flipV) flipBlockVertical(block);
            if (flipH) flipBlockHorizontal(block);
            shuffleBlockPixels(block, rand); // reverse order of shuffle

            writeBlock(unscrambled, dstX, dstY, block);
        }

        return unscrambled;
    }

    private static int[][] getBlock(BufferedImage image, int x, int y) {
        int[][] block = new int[BLOCK_SIZE][BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                block[i][j] = image.getRGB(x + j, y + i);
            }
        }
        return block;
    }

    private static void writeBlock(BufferedImage image, int x, int y, int[][] block) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                image.setRGB(x + j, y + i, block[i][j]);
            }
        }
    }

    private static void shuffleBlockPixels(int[][] block, Random rand) {
        List<int[]> pixels = new ArrayList<>();
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                pixels.add(new int[]{i, j, block[i][j]});
            }
        }
        Collections.shuffle(pixels, rand);
        for (int i = 0; i < BLOCK_SIZE * BLOCK_SIZE; i++) {
            int x = i / BLOCK_SIZE;
            int y = i % BLOCK_SIZE;
            block[x][y] = pixels.get(i)[2];
        }
    }

    private static void flipBlockHorizontal(int[][] block) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            for (int j = 0; j < BLOCK_SIZE / 2; j++) {
                int temp = block[i][j];
                block[i][j] = block[i][BLOCK_SIZE - 1 - j];
                block[i][BLOCK_SIZE - 1 - j] = temp;
            }
        }
    }

    private static void flipBlockVertical(int[][] block) {
        for (int i = 0; i < BLOCK_SIZE / 2; i++) {
            for (int j = 0; j < BLOCK_SIZE; j++) {
                int temp = block[i][j];
                block[i][j] = block[BLOCK_SIZE - 1 - i][j];
                block[BLOCK_SIZE - 1 - i][j] = temp;
            }
        }
    }
}
