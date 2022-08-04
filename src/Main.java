import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Main {
    //public static String [][] cards = new String[0][0];
    public static int[][] cards;
    public static int[][] cardsArray;
    public static String[][] playingGround;
    public static String[][] updatedPg;
    public static String[] split;
    public static List<String> selectedArrayWords = new ArrayList<String>();
    public static List<String> words = new ArrayList<String>();
    public static List<String> resultsStrArr = new ArrayList<String>();
    public static int countWords = 0;
    public static boolean isChosensecond = false;
    public static boolean isChosenfirst = false;
    public static boolean once = true;
    private static int cor4;
    private static int cor3;
    private static boolean chosenFirst= true;
    private static boolean isboardSet;
    private static int countToWin;
    private static int chances;
    private static Date endDate;
    private static String[][] splitCombined;
    private static int[][] scores;
    private static String[][] newScores;


    public static void main(String[] args) throws IOException, InterruptedException {

        ////////////////
        File file = new File("src\\Words.txt");
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                    words.add(line);
                    countWords++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ////////////////////
        //////////////////////////////////////////////////////////////// difficulty level ////////
        System.out.println("Choose difficulty level: easy or hard");
        Scanner inputt = new Scanner(System.in);
        String diff = inputt.next();
        Pattern p = Pattern.compile("easy||hard");
        while(!isValidCode(diff,p)){
            System.out.println("Please provide correct answer: easy or hard");
            System.out.println("Choose difficulty level: easy or hard");
            inputt = new Scanner(System.in);
            diff = inputt.next();
            isValidCode(diff,p);
        }


        if(diff.equals("easy")){
            chances = 10;
            String[] arrayWords = words.toArray(new String[0]);
            Random r=new Random();
            for (int i = 1; i <= 4; i++) {
                int randomNumber=r.nextInt(arrayWords.length);
                selectedArrayWords.add(arrayWords[randomNumber]);
                System.out.println(selectedArrayWords);
            }
        }else if(diff.equals("hard")){
            chances = 15;
            String[] arrayWords = words.toArray(new String[0]);
            Random r=new Random();
            for (int i = 1; i <= 8; i++) {
                int randomNumber=r.nextInt(arrayWords.length);
                selectedArrayWords.add(arrayWords[randomNumber]);
                System.out.println(selectedArrayWords);
            }
        }
//////////////////////////////////////////////////////////////////////////////////////////
        //String[] arrayWords = words.toArray(new String[0]);
        String[] arrayWords = selectedArrayWords.toArray(new String[0]);
        int c = arrayWords.length;

        System.out.println(c);

        int b = 1;
        int bb = 1;
        int dL = 0;

        p = Pattern.compile("^A[0-9]$|^A[1-" + bb + "][0-9]$|^A[" + b + "][" + dL + "]$|");;
        if (c >= 10) {
            b = c / 10;
            bb = b - 1;
            dL = c - (b * 10);
            if(c<100){
                p = Pattern.compile("^A[0-9]$|^A[1-" + bb + "][0-9]$|^A[" + b + "][0-" + dL + "]$|^B[0-9]$|^B[1-" + bb + "][0-9]$|^B[" + b + "][0-" + dL + "]$");
            }else {
                p = Pattern.compile("^A[0-9]$|^A[0-9][0-9]$|^A(100)$|^B[0-9]$|^B[0-9][0-9]$|^B(100)$");
            }
        }
        else{
            p = Pattern.compile("^A[0-9]$|^B[0-9]$");
        }
        System.out.println("Hello world!");
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Code");
        String code = input.next();
        System.out.println(isValidCode(code, p));
        cardsArray = createMap(c, cards);
        board(c, cardsArray, updatedPg);
        Date startDate = new Date();
        for(int ii = 0; ii< chances; ii++) {
            choosingCord(cardsArray, arrayWords, p, c);
            if(checkW(updatedPg)){
                endDate = new Date();
                break;
            }

//        String[] strArray = code.split("A|B");
//        int[] intArray = new int[strArray.length];
//        for(int i = 0; i < strArray.length; i++) {
//            intArray[i] = Integer.parseInt(strArray[i]);
//            if(intArray[i]<=c){
//                System.out.println("ok");
//            }
//            System.out.println("not ok");
//        }
        }

        int numSeconds = (int)((endDate.getTime() - startDate.getTime()) / 1000);
        System.out.println("You solved the memory game after "+countToWin+" chances. It took you "+numSeconds+" seconds");
/////////////////////////
        System.out.println("Do you want to save the results? (Y/N)");
        Scanner resultsCode = new Scanner(System.in);
        String resultsAnswer = resultsCode.next();
        p = Pattern.compile("^Y$|^N$");


        while(!isValidCode(resultsAnswer,p)){
            System.out.println("Do you want to save the results? (Y/N)");
            resultsCode = new Scanner(System.in);
            resultsAnswer = resultsCode.next();
            p = Pattern.compile("^Y$|^N$");
            isValidCode(resultsAnswer,p);
        }
        if(resultsAnswer.equals("Y")){
            System.out.println("What is your your name?");
            resultsCode = new Scanner(System.in);
            String name = resultsCode.next();


            File f1 = new File("src/results.txt");
            if(!f1.exists()) {
                f1.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(f1.getName(),true);
            BufferedWriter out = new BufferedWriter(fileWritter);
//
//            PrintWriter out = new PrintWriter("src/results.txt");
            long millis = System.currentTimeMillis();
            java.util.Date dateDay = new java.util.Date(millis);
            out.write(name+" "+dateDay+" "+numSeconds+" "+countToWin+ "\r\n");
            out.close();
        }else if(resultsAnswer.equals("N")){

        }
        System.out.println("THANK YOU FOR PLAYING :)");
        System.out.println("---------------------TOP SCORES---------------------");
        resultsOfTheGame();

  ///////////////////////////////////

    }
    public static boolean isValidCode (String code, Pattern p) {
        boolean check = p.matcher(code).matches();
//
//        if(check){
//            System.out.println("Correct coordinates");
//        }else{
//            System.out.println("Please provide correct coordinates - example: A4, B10");
//        }
        return p.matcher(code).matches();
    }
    static void rand( int array[], int a)
    {
        // Creating object for Random class
        Random rd = new Random();

        // Starting from the last element and swapping one by one.
        for (int i = a-1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = rd.nextInt(i+1);

            // Swap array[i] with the element at random index
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        // Printing the random generated array
        System.out.println(Arrays.toString(array));
    }
    public static int[][] createMap(int c, int[][] cards){
        System.out.println("createMap"+c);
        //int[][] cards = new int[2][c];
        cards = new int[2][c];
        for(int i = 0; i< 2; i++){
            for(int j = 0; j < c; j++){
                cards[i][j] = j;
            }
        }
//        ////////////////Shuflowanie/////////////////////
//        for (int[] row : cards) {
//            rand (row, c);
//            //Arrays.fill(cards, "-");
//        }
//        ///////////////////////////////////////////////////
        System.out.println(Arrays.deepToString(cards));
        return cards;

    }
    public static void choosingCord(int[][] cards, String[] arrayWords, Pattern p, int c) throws InterruptedException {
        boolean correctAnswer = false;
        System.out.println(Arrays.deepToString(cards));
        int intValone = 0;
        int intAbvalone = 0;
        while (!isChosenfirst) {
            System.out.println("Choose first coordinates");
            Scanner input = new Scanner(System.in);
            String firstcor = input.next();

            //System.out.println(isValidCode(firstcor, p));

            if (isValidCode(firstcor, p)) {
                isChosenfirst = true;
                System.out.println("Correct coordinates");
                String strValone = firstcor.substring(0,1);
                if(strValone.equals("A")){
                    intAbvalone = 0;
                }else if(strValone.equals("B")){
                    intAbvalone = 1;
                }
                intValone = Integer.parseInt(firstcor.substring(1));
            } else {
                System.out.println("Please provide correct coordinates - example: A4, B10");
                continue;
            }
            updatingPg(intAbvalone,intValone,arrayWords,c,cards,correctAnswer);
        }
        int intValtwo = 0;
        int intAbvaltwo = 0;
        while (!isChosensecond) {
            System.out.println("Choose second coordinates");
            Scanner input = new Scanner(System.in);
            String secondcor = input.next();
            //System.out.println(isValidCode(secondcor, p));

            if (isValidCode(secondcor, p)) {
                isChosensecond = true;
                System.out.println("Correct coordinates");
                String strValtwo = secondcor.substring(0,1);
                System.out.println("substring 0,1");
                //System.out.println(strValtwo);
                if(strValtwo.equals("A")){
                    intAbvaltwo = 0;
                }else if(strValtwo.equals("B")){
                    intAbvaltwo = 1;
                }
                intValtwo = Integer.parseInt(secondcor.substring(1));
                System.out.println("substring 1, 3");
                //System.out.println(intValtwo);
            } else {
                System.out.println("Please provide correct coordinates - example: A4, B10");
                continue;
            }

        }
        if (cards[intAbvalone][intValone-1] == cards[intAbvaltwo][intValtwo-1]){
            System.out.println("intAbvalone "+intAbvalone);
            System.out.println("intValone "+intValone);
            System.out.println("intAbvaltwo "+intAbvaltwo);
            System.out.println("intValtwo "+intValtwo);
//
//            System.out.println(arrayWords[cards[intAbvalone][intValone-1]]);
//            System.out.println(arrayWords[cards[intAbvaltwo][intValtwo-1]]);
            correctAnswer = true;
            if(isChosenfirst && isChosensecond){
                isChosenfirst= false;
                isChosensecond = false;
            }

            System.out.println("correct");
        }else{
            correctAnswer = false;
            System.out.println("intAbvalone "+intAbvalone);
            System.out.println("intValone "+intValone);
            System.out.println("intAbvaltwo "+intAbvaltwo);
            System.out.println("intValtwo "+intValtwo);
            if(isChosenfirst && isChosensecond){
                isChosenfirst= false;
                isChosensecond = false;
            }
//
//            System.out.println(arrayWords[cards[intAbvaltwo][intValtwo-1]]);
//            System.out.println(arrayWords[cards[intAbvaltwo][intValtwo-1]]);

            System.out.println("not hehe");
        }
        updatingPg(intAbvaltwo,intValtwo,arrayWords,c,cards,correctAnswer);
    }
    public static void updatingPg(int cor1,int cor2, String[] arrayWords,int c,int[][] cards,boolean correctAnswer) throws InterruptedException {
        System.out.print("updatingPg!");
        if(once){
            updatedPg = new String[cardsArray[0].length][cardsArray[1].length];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < c; j++) {
                    updatedPg[i][j]="x";
                }
            }
            once=false;
        }
        if(chosenFirst==true){
            System.out.println("     chosenFirst==true    ");
            cor3 = cor1;
            cor4 = cor2-1;

            updatedPg[cor3][cor4] = arrayWords[cards[cor3][cor4]];
            System.out.print(updatedPg);
        }

        updatedPg[cor1][cor2-1] = arrayWords[cards[cor1][cor2-1]];
        if(!chosenFirst) {
            board(c, cardsArray, updatedPg);
            TimeUnit.SECONDS.sleep(1);
        }
//        if(chosenFirst){
        if(correctAnswer==true||!correctAnswer && !chosenFirst) {
            if (correctAnswer == true) {
                System.out.print("Congratulations!");
                chosenFirst = true;
            }
            if (!correctAnswer && !chosenFirst) {
                System.out.print("Wrong answer");
                updatedPg[cor1][cor2 - 1] = "x";
                updatedPg[cor3][cor4] = "x";
                chosenFirst = true;
            }
        }else{
            chosenFirst = false;
        }
//        }
        board(c, cardsArray, updatedPg);
    }
    public static boolean checkW(String[][] updatedPg){
        boolean allNonX=false;
        countToWin = 0;
        System.out.print((updatedPg[1].length)+(updatedPg[0].length));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < updatedPg[1].length; ++j) {
                if (!updatedPg[i][j].equals("x")) {
                    countToWin=countToWin+1;
                    System.out.print(countToWin);
                }
                if (countToWin == (updatedPg[1].length+updatedPg[0].length)){
                    allNonX = true;
                }
            }
        }
        return allNonX;
    }
    public static void board(int c, int[][] cardsArray, String[][] updatedPg){
        if(!isboardSet){
            playingGround = new String[cardsArray[0].length][cardsArray[1].length];

            isboardSet=true;
        }else if(isboardSet){
            playingGround=updatedPg;
            for (int k = 0; k <= c; k++) {
                if (k < 1) {
                    System.out.print(" ");
                    System.out.print(" ");
                    continue;
                }
                System.out.print(k);
                System.out.print(" ");
            }
            System.out.println();
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    System.out.print("A");
                } else if (i == 1) {
                    System.out.print("B");
                }
                System.out.print("|");
                for (int j = 0; j < c; j++) {
                    System.out.print(playingGround[i][j]);
                    System.out.print("|");
                }
                System.out.println();
            }
        }
    }
    public static void resultsOfTheGame(){
        File fileResults = new File("results.txt");
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileResults))) {
                String line;
                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                    resultsStrArr.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] resultsArray = resultsStrArr.toArray(new String[0]);
        System.out.println(resultsArray.length);
        splitCombined = new String[resultsArray.length][8];
        for (int row = 0; row < resultsArray.length; row++) {
            split = resultsArray[row].split("\\s+");
            splitCombined[row] = split;

            //System.out.println(Arrays.toString(splitCombined[row]));
        }
        scores = new int[splitCombined.length][2];
        for(int i = 0; i<splitCombined.length; i++) {
            scores[i][1]=i;
            scores[i][0]=Integer.parseInt(splitCombined[i][8]);
            //System.out.println(Arrays.toString(scores[i]));
        }
        //System.out.println(Arrays.toString(scores));
        Arrays.sort(scores, (a, b) -> Integer.compare(b[0],a[0]));
        //System.out.println(Arrays.toString(scores[2]));
        newScores = new String[10][splitCombined[1].length];
        for(int i = 0; i<scores.length; i++) {
            newScores[i]=splitCombined[scores[i][1]];
            //System.out.println(Arrays.toString(scores[i]));
            //System.out.println(Arrays.toString(newScores[0]));
        }
            for(int i = 0; i<10; i++) {
                System.out.println(Arrays.toString(newScores[i]));
            }

    }
}