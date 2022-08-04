import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Main {
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
    private static boolean isboardSet = false;
    private static int countToWin;
    private static int chances;
    private static Date endDate;
    private static String[][] splitCombined;
    private static int[][] scores;
    private static String[][] newScores;

    //////////////////////// Loading words form the provided text file/////////////////////////////
    public static void main(String[] args) throws IOException, InterruptedException {
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
        //////////////////////// difficulty level ////////////////////////
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
            }
        }else if(diff.equals("hard")){
            chances = 15;
            String[] arrayWords = words.toArray(new String[0]);
            Random r=new Random();
            for (int i = 1; i <= 8; i++) {
                int randomNumber=r.nextInt(arrayWords.length);
                selectedArrayWords.add(arrayWords[randomNumber]);
            }
        }

        String[] arrayWords = selectedArrayWords.toArray(new String[0]);
        int c = arrayWords.length;

        //////////////////////// Regex pattern to check if string provided by the user is acceptable ////////////////////////
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
        cardsArray = createMap(c, cards);
        board(c, cardsArray, updatedPg);
        System.out.println(" board(c, cardsArray, updatedPg);");
        Date startDate = new Date();
        for(int ii = 0; ii< chances; ii++) {
            choosingCord(cardsArray, arrayWords, p, c);
            if(checkW(updatedPg)){
                endDate = new Date();
                break;
            }
        }
        int numSeconds = (int)((endDate.getTime() - startDate.getTime()) / 1000);

        System.out.println("You solved the memory game after "+countToWin+" chances. It took you "+numSeconds+" seconds");
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

    }
    public static boolean isValidCode (String code, Pattern p) {
        boolean check = p.matcher(code).matches();
        return p.matcher(code).matches();
    }
    static void rand( int array[], int a)
    {
        Random rd = new Random();
        for (int i = a-1; i > 0; i--) {
            int j = rd.nextInt(i+1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    public static int[][] createMap(int c, int[][] cards){

        cards = new int[2][c];
        for(int i = 0; i< 2; i++){
            for(int j = 0; j < c; j++){
                cards[i][j] = j;
            }
        }
        return cards;

    }
    public static void choosingCord(int[][] cards, String[] arrayWords, Pattern p, int c) throws InterruptedException {
        boolean correctAnswer = false;
        int intValone = 0;
        int intAbvalone = 0;
        while (!isChosenfirst) {
            Scanner input = new Scanner(System.in);
            String firstcor = input.next();

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

            if (isValidCode(secondcor, p)) {
                isChosensecond = true;
                String strValtwo = secondcor.substring(0,1);
                if(strValtwo.equals("A")){
                    intAbvaltwo = 0;
                }else if(strValtwo.equals("B")){
                    intAbvaltwo = 1;
                }
                intValtwo = Integer.parseInt(secondcor.substring(1));
            } else {
                System.out.println("Please provide correct coordinates - example: A4, B10");
                continue;
            }

        }
        if (cards[intAbvalone][intValone-1] == cards[intAbvaltwo][intValtwo-1]){
            correctAnswer = true;
            if(isChosenfirst && isChosensecond){
                isChosenfirst= false;
                isChosensecond = false;
            }
            System.out.println("correct");
        }else{
            correctAnswer = false;
            if(isChosenfirst && isChosensecond){
                isChosenfirst= false;
                isChosensecond = false;
            }
            System.out.println("not correct");
        }
        updatingPg(intAbvaltwo,intValtwo,arrayWords,c,cards,correctAnswer);
    }
    public static void updatingPg(int cor1,int cor2, String[] arrayWords,int c,int[][] cards,boolean correctAnswer) throws InterruptedException {
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
            cor3 = cor1;
            cor4 = cor2-1;

            updatedPg[cor3][cor4] = arrayWords[cards[cor3][cor4]];
        }

        updatedPg[cor1][cor2-1] = arrayWords[cards[cor1][cor2-1]];
        if(!chosenFirst) {
            board(c, cardsArray, updatedPg);
            TimeUnit.SECONDS.sleep(1);
        }
        if(correctAnswer==true||!correctAnswer && !chosenFirst) {
            if (correctAnswer == true) {
                System.out.println("Congratulations!"+System.lineSeparator());
                chosenFirst = true;
            }
            if (!correctAnswer && !chosenFirst) {
                System.out.println("Wrong answer"+System.lineSeparator());
                updatedPg[cor1][cor2 - 1] = "x";
                updatedPg[cor3][cor4] = "x";
                chosenFirst = true;
            }
        }else{
            chosenFirst = false;
        }
        board(c, cardsArray, updatedPg);
    }
    public static boolean checkW(String[][] updatedPg){
        boolean allNonX=false;
        countToWin = 0;

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < updatedPg[1].length; ++j) {
                if (!updatedPg[i][j].equals("x")) {
                    countToWin=countToWin+1;
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
            for (int k = 0; k <= playingGround[0].length-1; k++) {
                for (int kk = 0; kk <= playingGround[1].length-1; kk++) {
                    playingGround[k][kk] = "x";
                }
            }
            isboardSet=true;
        }else if(isboardSet) {
            playingGround = updatedPg;
        }
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
    public static void resultsOfTheGame() throws IOException {
        File fileResults = new File("results.txt");
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileResults))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultsStrArr.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] resultsArray = resultsStrArr.toArray(new String[0]);
        splitCombined = new String[resultsArray.length][8];
        for (int row = 0; row < resultsArray.length; row++) {
            split = resultsArray[row].split("\\s+");
            splitCombined[row] = split;
        }
        scores = new int[splitCombined.length][2];
        for(int i = 0; i<splitCombined.length; i++) {
            scores[i][1]=i;
            scores[i][0]=Integer.parseInt(splitCombined[i][8]);
        }
        Arrays.sort(scores, (a, b) -> Integer.compare(b[0],a[0]));
        newScores = new String[10][splitCombined[1].length];
        for(int i = 0; i<scores.length; i++) {
            newScores[i]=splitCombined[scores[i][1]];
        }
            for(int i = 0; i<10; i++) {
                System.out.println(Arrays.toString(newScores[i]));
                File f2 = new File("src/topscore.txt");
                if(!f2.exists()) {
                    f2.createNewFile();
                }
                FileWriter fileWritter = new FileWriter(f2.getName(),true);
                BufferedWriter out = new BufferedWriter(fileWritter);
                out.write(Arrays.toString(newScores[i])+"\r\n");
                out.close();
            }

    }
}