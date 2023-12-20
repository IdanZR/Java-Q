    //  update 25-2-2023 MS

    import java.io.*;
    import java.util.ArrayList;
    import java.util.Random;

    // An Undirected graph using adjacency list representation

    public class Graph
    {
        public static final int SIZE = 8;
        public static int[] vec = new int[SIZE];
        private int  numOfVertices; // No. of vertices
        private ArrayList<Integer> [] adj; // Array of adjacency lists
        private static ArrayList<Integer> eulerPathDots = new ArrayList<>();
        private static int[][] keyArray;
        // Constructor
        public Graph(int numOfVertices)
        {
            this.numOfVertices = numOfVertices;
            adj = new ArrayList [numOfVertices];
            keyArray = new int[numOfVertices][numOfVertices];
            for (int i=0; i<numOfVertices; i++)
                adj[i] = new ArrayList<Integer>();
        }
        public static void buildGraphList()
        {
            Random rand = new Random();
           // int randNum = rand.nextInt(5 - 1) + 1;
            switch(4){
                case 1:
                    Graph g1 = new Graph(4);
                    g1.addEdge(0, 1);
                    g1.addEdge(0, 2);
                    g1.addEdge(1, 2);
                    g1.addEdge(2, 3);
                    g1.printEulerPath();
                    break;
                case 2:
                    Graph g2 = new Graph(3);
                    g2.addEdge(0, 1);
                    g2.addEdge(1, 2);
                    g2.addEdge(2, 0);
                    g2.printEulerPath();
                    break;
                case 3:
                    Graph g3 = new Graph(5);
                    g3.addEdge(1, 0);
                    g3.addEdge(0, 2);
                    g3.addEdge(2, 1);
                    g3.addEdge(0, 3);
                    g3.addEdge(3, 4);
                    g3.addEdge(3, 2);
                    g3.addEdge(3, 1);
                    g3.addEdge(2, 4);
                    g3.printEulerPath();
                    break;
                case 4:
                    Graph g4 =new Graph(6);
                    g4.addEdge(0, 1);
                    g4.addEdge(0, 5);
                    g4.addEdge(1, 2);
                    g4.addEdge(1, 3);
                    g4.addEdge(2, 3);
                    g4.addEdge(3, 4);
                    g4.addEdge(4, 5);
                    g4.printEulerPath();
                    break;
                case 5:
                    // Euler circuit in K5:
                    Graph g5 =new Graph(5);
                    g5.addEdge(0, 1);
                    g5.addEdge(0, 2);
                    g5.addEdge(0, 3);
                    g5.addEdge(0, 4);
                    g5.addEdge(1, 2);
                    g5.addEdge(1, 3);
                    g5.addEdge(1, 4);
                    g5.addEdge(2, 3);
                    g5.addEdge(2, 4);
                    g5.addEdge(3, 4);
                    g5.printEulerPath();
                    break;
            }
        }
        // add edge u---v
        private void addEdge(Integer u, Integer v)
        {
            adj[u].add(v);
            adj[v].add(u);
        }

        // remove edge u---v from graph.
        private void removeEdge(Integer u, Integer v)
        {
            adj[u].remove(v);
            adj[v].remove(u);
        }

        private int countOddDegreeVertices()
        {
            int count = 0;
            for (int i=0; i<numOfVertices; i++)
                if (adj[i].size()%2==1)
                    count++;

            return count;
        }

        private boolean existsEulerPath()
        {
            if ( countOddDegreeVertices()==2)
                return true;

            return false;
        }

        private boolean existsEulerCircuit()
        {
            if ( countOddDegreeVertices()==0)
                return true;

            return false;
        }


        private void printEulerPath()
        {
    //        // Find a vertex with odd degree
    //        Integer u = 0;
    //        for (int i = 0; i < numOfVertices; i++) {
    //            if (adj[i].size() % 2 == 1) {
    //                u = i;
    //                break;
    //            }
    //        }

            int u=0;
            int i=0;

            if(existsEulerPath())
            {
                while( i< numOfVertices && adj[i].size()%2==0)
                    i++;

                if(adj[i].size()%2==1)
                    u=i;
            }

            if(existsEulerCircuit())
                u=0; // start circuit in vertex 0

            generateEulerUtil(u);
            System.out.println();
        }

        // Recursive method to print Euler path starting from vertex u
        private void generateEulerUtil(Integer u)
        {
            for (int i=0; i < adj[u].size(); i++) {
                Integer v = adj[u].get(i);
                if (isValidNextEdge(u, v)) {
                    eulerPathDots.add(u);
                    // This edge is in Eular path so remove it from graph
                    removeEdge(u, v);
                    generateEulerUtil(v);
                }

            }
        }
        private static void printList()
        {
            System.out.println(eulerPathDots);
        }
        // This method checks if  edge u---v can be
        // the next valid edge in the Euler path.
        private boolean isValidNextEdge(Integer u, Integer v)
        {
            // The edge u---v is valid only in one of the
            // following two cases:

            // case 1:  u---v is not a bridge
            if( !isBridge(u,v))
                return true;

            // case 2:  v is the only adjacent vertex of u
            if (adj[u].size() == 1)
                return true;

            return false;
        }

        // A DFS based method to count number
        // of reachable vertices from source
        // MS
        private int dfsCount(Integer source, boolean[] isVisited)
        {
            // Mark the current node as visited
            isVisited[source] = true;
            int count = 1;

            for (int v : adj[source])
                if (!isVisited[v])
                    count = count + dfsCount(v, isVisited);

            return count;
        }

        // MS 25-2-2023
        private boolean isBridge(Integer u, Integer v)
        {
            //   count number of vertices reachable from u
            boolean[] isVisited = new boolean[numOfVertices];
            int count1 = dfsCount(u, isVisited);

            //  remove edge (u, v) and then
            //  count again number of vertices reachable from u
            removeEdge(u, v);
            isVisited = new boolean[numOfVertices];
            int count2 = dfsCount(u, isVisited);

            // add the edge back  again to the graph
            addEdge(u, v);
            return (count1 > count2) ? true : false;
        }
        public static int switchBit(int data, int i)
        {
            for(int bitNum : eulerPathDots)
                data = data ^ (1<<bitNum);
            return data;
        }
        public static ArrayList<Integer> changeKey()
        {
             ArrayList<Integer> l1 = eulerPathDots;
             for(int i=0;i<l1.size();i++)
              {
                  l1.set(i,(int)Math.pow(l1.get(i),2));
              }
        return  l1;
        }
        public static void convertFile(File inFile,String s){
            try{
                int sizeToSkip = 0;
                int temp, index=0;
                FileInputStream in;
                FileOutputStream out;
                File outFile=new File(inFile.getParentFile(),s + inFile.getName());
                in= new FileInputStream(inFile);
                out= new FileOutputStream(outFile);
                switch(s) {
                    case "easyEncrypt":
                        buildGraphList();
                        ArrayList<Integer> l1;
                         l1 = changeKey();
                         sizeToSkip = 6 + 2 * l1.size();
                        out.write(97);
                        out.write(91);
                        for(Integer num : l1)
                        {
                            if(num > 10)
                            {
                                byte[] number =  String.valueOf(num/10).getBytes();
                                out.write(number);
                                out.write(45);
                                byte[] number2 =  String.valueOf(num%10).getBytes();
                                out.write(number2);
                            }
                            else {
                                byte[] number = String.valueOf(num).getBytes();
                                out.write(number);
                            }

                            //out.write(45);
                        }
                        out.write(93);
                        break;
                    case "easyDecrypt":
                        sizeToSkip = 6 + 2 *eulerPathDots.size();
                        break;
                }
                in.skip(sizeToSkip); //don't encrypt
                while((temp=in.read()) != -1){
                    out.write(switchBit(temp, vec[index]));
                    index++;
                    if(index == SIZE)
                        index=0;
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
        public static void encryptFile(File inFile,String s)
        {
            convertFile(inFile,s);
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
            applySquareRootToKeys(keys);
            eulerPathDots.clear(); // Clear existing elements from eulerPathDots
            eulerPathDots.addAll(keys); // Set eulerPathDots to the keys

            // Convert the file using the updated keys
            convertFile(inFile, s);
        }
        private static void applySquareRootToKeys(ArrayList<Integer> keys) {
            for (int i = 0; i < keys.size(); i++) {
                int key = keys.get(i);
                double sqrtKey = Math.sqrt(key);
                int roundedKey = (int) Math.round(sqrtKey);
                keys.set(i, roundedKey);
            }
        }


        public static void main(String[] args) {
        buildGraphList();
        printList();
    }
    }
