import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class KnightTour2 {

    public static final int SIZE = 8;
    static ArrayList<Integer> Ikeys = new ArrayList<>();
    public static int[] vec = new int[SIZE];
    public static final int BOARD_SIZE = 8;
    private int[] xMoves = {2, 1, -1, -2, -2, -1, 1, 2};
    private int[] yMoves = {1, 2, 2, 1, -1, -2, -2, -1};
    private int[][] board;

    public KnightTour2() {

        board = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = -1;
            }
        }
    }

    public boolean solve() {
        // Random starting point
        Random rand = new Random();
        int startX = rand.nextInt(BOARD_SIZE);
        int startY = rand.nextInt(BOARD_SIZE);
        board[startX][startY] = 0;

        // Move the knight
        if (move(1, startX, startY)) {
            printSolution();
            return true;
        } else {
            System.out.println("No solution found");
            return false;
        }
    }

    private boolean move(int moveCount, int x, int y) {
        // Base case: all squares have been visited
        if (moveCount == BOARD_SIZE * BOARD_SIZE) {
            return true;
        }

        // Try each possible move
        int minMoves = Integer.MAX_VALUE;
        int nextX = -1;
        int nextY = -1;
        for (int i = 0; i < xMoves.length; i++) {
            int newX = x + xMoves[i];
            int newY = y + yMoves[i];
            if (isSafe(newX, newY) && onwardMoves(newX, newY) < minMoves) {
                minMoves = onwardMoves(newX, newY);
                nextX = newX;
                nextY = newY;
            }
        }

        // If a move was found
        if (nextX != -1 && nextY != -1) {
            board[nextX][nextY] = moveCount;
            if (move(moveCount + 1, nextX, nextY)) {
                return true;
            } else {
                board[nextX][nextY] = -1;
            }
        }

        return false;
    }

    private int onwardMoves(int x, int y) {
        int moves = 0;
        for (int i = 0; i < xMoves.length; i++) {
            int newX = x + xMoves[i];
            int newY = y + yMoves[i];
            if (isSafe(newX, newY)) {
                moves++;
            }
        }
        return moves;
    }

    private boolean isSafe(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && board[x][y] == -1;
    }

    private void printSolution() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            generateKeyFromKnightTour();
            System.out.println();
        }

    }
    public ArrayList<Integer> generateKeyFromKnightTour() {
        ArrayList<Integer> l1 = new ArrayList<>();
        StringBuilder keyBuilder = new StringBuilder();
        int[][] solution = board;
        for (int[] row : solution) {
            for (int cell : row) {
                l1.add(cell);
            }
        }
        return l1;
    }
    public static ArrayList<Integer> changeKey()
    {
        ArrayList<Integer> l1 = new ArrayList<>(Ikeys); //so they won't point to the same location in the memory so changing won't be for both of them.
        for(int i=0;i<l1.size();i++)
        {
            l1.set(i,(l1.get(i) + 1));
        }
        return  l1;
    }
    public static int switchBit(int data, int i) {
        long longData = data & 0xFFFFFFFFL; // Convert to long while preserving the lower 32 bits

        for (Integer bitPosition : Ikeys) {
            longData ^= (1L << bitPosition); // Toggle the bit at each position in the keyList
        }
        int intData = (int) longData; // Convert back to int, truncating any higher bits
        return intData;
    }
    public static void convertFile2(File inFile, String s) throws FileNotFoundException {
        try {
            int sizeToSkip = 0;
            int temp, index = 0;
            FileInputStream in;
            FileOutputStream out;

            File outFile = new File(inFile.getParentFile(), s + inFile.getName());
            in = new FileInputStream(inFile);
            out = new FileOutputStream(outFile);

            switch (s) {
                case "easyEncrypt":
                    KnightTour2 KN = new KnightTour2();
                    KN.solve();
                    Ikeys = KN.generateKeyFromKnightTour();
                    ArrayList<Integer> keyList = changeKey();
                    sizeToSkip = 6 + 2 * keyList.size();
                    out.write(97);
                    out.write(91);
                    for (Integer num : Ikeys) {
                        if (num > 10) {
                            byte[] number = String.valueOf(num / 10).getBytes();
                            out.write(number);
                            out.write(45);
                            byte[] number2 = String.valueOf(num % 10).getBytes();
                            out.write(number2);
                        } else {
                            byte[] number = String.valueOf(num).getBytes();
                            out.write(number);
                        }
                    }
                    out.write(93);
                    break;
                case "easyDecrypt":
                    sizeToSkip = 6 + 2 * Ikeys.size();
                    break;
            }

            //in.skip(sizeToSkip);

            while ((temp = in.read()) != -1) {
                out.write(switchBit(temp, vec[index]));
                index++;
                if (index == SIZE)
                    index = 0;
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void encryptFile(File inFile, String s) throws FileNotFoundException {
        convertFile2(inFile, s);
    }

    public static void decryptFile(File inFile, String s) throws IOException {
        // Read the file
        FileInputStream in = new FileInputStream(inFile);
        byte[] fileData = in.readAllBytes();
        in.close();

        // Getting the keys in an array
        ArrayList<Integer> keys = new ArrayList<>();
        boolean isKey = false;
        boolean isHyphen = false;
        int prevDigit = -1;
        for (byte data : fileData) {
            if (data == 91) {
                // Start of key section
                isKey = true;
                continue;
            }
            if (isKey) {
                char ch = (char) data;
                if (Character.isDigit(ch)) {
                    // If the character is a digit
                    int digit = Character.getNumericValue(ch);
                    if (isHyphen) {
                        // If there was a hyphen before the digit, combine it with the previous digit
                        int combinedDigit = prevDigit * 10 + digit;
                        keys.remove(keys.size() - 1); // Remove the last digit added
                        keys.add(combinedDigit);
                        isHyphen = false;
                    } else {
                        // Add the digit to the keys list
                        keys.add(digit);
                    }
                    prevDigit = digit;
                } else if (ch == '-') {
                    // Mark that a hyphen was encountered
                    isHyphen = true;
                }
            }
            if (data == 93) {
                // End of key section
                isKey = false;
                break;
            }
        }
        apply10ToKeys(keys);
        Ikeys.clear(); // Clear existing elements from eulerPathDots
        Ikeys.addAll(keys); // Set eulerPathDots to the keys

        // Convert the file using the updated keys
        convertFile2(inFile, s);
    }
    private static void apply10ToKeys(ArrayList<Integer> keys) {
        for (int i = 0; i < keys.size(); i++) {
            int key = keys.get(i);
            double sqrtKey = Math.sqrt(key);
            int roundedKey = (int) Math.round(sqrtKey);
            keys.set(i, roundedKey);
        }
    }
    public static void main(String[] args) {
      // convertFile();
        KnightTour2 knightTour2 = new KnightTour2();
        knightTour2.solve();

    }
}


