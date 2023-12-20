import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NonDecreasingSeq {
    static int count = 0;
    public static final int SIZE = 8;
    public static int[] vec = new int[SIZE];
    public static ArrayList<int[]> keys = new ArrayList<>();
    public static ArrayList<Integer> originalKeys = new ArrayList<>();
    //helper recursive method
    public static void printSeqUtil(int n,int k,int len,int[] arr)
    {
        // if length of current increasing sequence becomes k, print it
        if(len==k)
        {
            System.out.println(Arrays.toString(arr));
            keys.add(Arrays.copyOf(arr, arr.length));
            count++;
            return;
        }
        //set the starting number to put ar current position:
        //if length is 0 start with 1.
        //if length is not 0
        //then start from value of previous element
        int i = (len==0) ? 1 : arr[len-1];
        //increase Length
        len++;
        //put all number which are bigger/eq than the previous element at new position.
        while(i<=n)
        {
            arr[len-1] = i;
            printSeqUtil(n,k,len,arr);
            i++;
        }
        //this is important. Decrease len when while is done for backtracking
        len--;
    }
    static void printSeq(int n, int k)
    {

        //an shared array to generate all soulutions
        int[] arr = new int[k];
        //initial length of current sequence
        int len = 0;
        printSeqUtil(n,k,len,arr);
    }

    public static void main(String[] args) {
        int k = 3, n = 7;
        printSeq(n,k);
        System.out.println("Num of solutions: "+NonDecreasingSeq.count);
    }
    public static ArrayList<int[]> changeKey()
    {
        ArrayList<int[]> l1 = keys;
        for(int[] arrayItem : l1)
            for(int i=0;i< arrayItem.length;i++)
                arrayItem[i] = (int)Math.sqrt(arrayItem[i]);
        return  l1;
    }
    public static int switchBit(int data, int i,int randomKey,String type)
    {
        int[] arrayKey;
        switch (type) {
            case "easyEncrypt":
                arrayKey = keys.get(randomKey);
                for (int bitNum : arrayKey)
                    data = data ^ (1 << bitNum);
                return data;
            case "easyDecrypt":
                arrayKey = originalKeys.stream().mapToInt(Integer::intValue).toArray();
                for (int bitNum : arrayKey)
                    data = data ^ (1 << bitNum);
                return data;
        }
        return 0;
    }
    public static void convertFile(File inFile,String s){
        try {
            int temp, index = 0,key=0;
            Random rand = new Random();
            ArrayList<int[]> l1 = new ArrayList<>();
            FileInputStream in;
            FileOutputStream out;
            File outFile = new File(inFile.getParentFile(), s + inFile.getName());
            in = new FileInputStream(inFile);
            out = new FileOutputStream(outFile);
            int sizeToSkip = 0;
            switch (s) {
                case "easyEncrypt":
                    int numberOfElements = rand.nextInt(7 - 1) + 1;
                    int stopTill = rand.nextInt(6 - 2) + 3;
                    printSeq(numberOfElements, stopTill);
                    l1 = changeKey();
                    int randomListToEncrypt = rand.nextInt(NonDecreasingSeq.count - 1) + 1;
                    key = randomListToEncrypt;
                    int[] keyArray = l1.get(randomListToEncrypt);
                    sizeToSkip = 6 + 2 * keyArray.length;
                    out.write(98);
                    out.write(91);
                    for (int value : keyArray) //writes the keys // conver to assci
                    {
                        if(value > 10)
                        {
                            byte[] number =  String.valueOf(value/10).getBytes();
                            out.write(number);
                            out.write(45);
                            byte[] number2 =  String.valueOf(value%10).getBytes();
                            out.write(number2);

                        }
                        else {
                            byte[] number = String.valueOf(value).getBytes();
                            out.write(number);
                        }
                    }
                    out.write(93);
                    break;
                case "easyDecrypt":
                    sizeToSkip = 6 + 2 * originalKeys.size();

            }
            in.skip(sizeToSkip); //don't encrypt
            while ((temp = in.read()) != -1) {
                out.write(switchBit(temp, vec[index],key,s));
                index++;
                if (index == SIZE)
                    index = 0;
            }
            in.close();
            out.close();
            // insert the keys top file!
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch(Exception ex){
            System.out.println(ex);
        }
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
        applyPowRootToKeys(keys);
        originalKeys.clear(); // Clear existing elements from eulerPathDots
        originalKeys.addAll(keys); // Set eulerPathDots to the keys

        // Convert the file using the updated keys
        convertFile(inFile, s);
    }
    public static void encryptFile(File inFile, String s) {
        convertFile(inFile, s);
    }
    private static void applyPowRootToKeys(ArrayList<Integer> keys) {
        for (int i = 0; i < keys.size(); i++) {
            int key = keys.get(i);
            double sqrtKey = Math.sqrt(key);
            int roundedKey = (int) Math.round(sqrtKey);
            keys.set(i, roundedKey);
        }
    }
}
