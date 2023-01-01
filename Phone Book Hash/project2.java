
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayList;




/**
 * project2
 */
public class project2 {
    static LinkedList<Word>[] hashtable = new LinkedList[1000];
    public static void main(String[] args) throws FileNotFoundException{
        //Read file and create scanner
        File readFile = new File("words.txt");
        Scanner scan = new Scanner(readFile);

        //For every line in text file, read word and store in hash table
        while(scan.hasNextLine()){
            String tempWord = scan.nextLine();
            double tempNumber = strInt(tempWord);
            //Check word length
            if(tempWord.length() == 10 || tempWord.length() == 7 || tempWord.length() == 4 || tempWord.length() == 3){
                int hashIndex = myHash(tempNumber);
                Word newWord = new Word(tempNumber,tempWord);
                if(hashtable[hashIndex] == null){
                    hashtable[hashIndex] = new LinkedList<Word>();
                    hashtable[hashIndex].add(newWord);
                }
                else{
                    hashtable[hashIndex].add(newWord);
                }
            }


            

        }
        takeInput();
        scan.close();
        
    }


    public static void takeInput(){
        System.out.printf("Enter a 10-digit phone number without any space or character (Enter 0 to escape): ");
        Scanner sc = new Scanner(System.in);
        double input = sc.nextDouble();
    
        //While input doesn't equal zero, still accept inputs (use zero for escape)
        while(input != 0){
            //Format input to be easy to compare values
            String inputFormatted = String.format("%.0f", input);
            // Make sure length of input is ten
            if(inputFormatted.length() == 10){
                //Find index for ten
                int index = myHash(input);

                //Keep record of words in their own arrays
                ArrayList<Word> tens = new ArrayList<Word>();
                ArrayList<Word> sevens = new ArrayList<Word>();
                ArrayList<Word> fours = new ArrayList<Word>();
                ArrayList<Word> threes = new ArrayList<Word>();

                //Tags 
                boolean hasTens = false;
                boolean hasSevens = false;
                boolean hasFours = false;
                boolean hasThrees = false;
               
               
                //String representation for seven, four, and three 
                double sevenRep = Double.parseDouble(inputFormatted.substring(3));
                double fourRep = Double.parseDouble(inputFormatted.substring(6));
                double threeRep = Double.parseDouble(inputFormatted.substring(3,6));

                //Find ten implementation if any
                if(hashtable[index] == null){
                    
                }
                else{
                    Iterator<Word> tensTarget = hashtable[index].iterator();
                    while(tensTarget.hasNext()){
                    Word targetWord = tensTarget.next();
                    if(targetWord.getWordLength() == 10 && targetWord.getNumber() == input){
                        tens.add(targetWord);
                        hasTens = true;
                    }
                    
                    }
                }
                



                //Find sevens implementation if any
                int sevensIndex = myHash(sevenRep);
                if(hashtable[sevensIndex] == null){
                    
                }
                else{
                    Iterator<Word> sevensTarget = hashtable[(int) sevensIndex].iterator();
                    while(sevensTarget.hasNext()){
                        Word targetWord = sevensTarget.next();
                        if(targetWord.getWordLength() == 7 && targetWord.getNumber() == sevenRep){
                            sevens.add(targetWord);
                            hasSevens = true;
                        }
                    }
                }

                //Find fours implementation if any
                
                int foursIndex = myHash(fourRep);
                if(hashtable[foursIndex] == null){

                }
                else{
                    Iterator<Word> foursTarget = hashtable[(int) foursIndex].iterator();
                    while(foursTarget.hasNext() && hasSevens == false && hasTens == false){
                        Word targetWord = foursTarget.next();
                        if(targetWord.getWordLength() == 4 && targetWord.getNumber() == fourRep){
                            fours.add(targetWord);
                            hasFours = true;
                        }
                    }
                }
               

                //Find Threes implementation if any
                int threesIndex = myHash(threeRep);
                if(hashtable[threesIndex] == null){

                }
                else{
                    Iterator<Word> threesTarget = hashtable[(int) threesIndex].iterator();
                    while(threesTarget.hasNext() && hasSevens == false && hasTens == false){
                        Word targetWord = threesTarget.next();
                        if(targetWord.getWordLength() == 3 && targetWord.getNumber() == threeRep){
                            threes.add(targetWord);
                            hasThrees = true;
                        }
                    }
                }

                //Print tens if any were found
                if(hasTens == true){
                        printTen(tens);
                }
                else if(hasSevens == true){
                        printSeven(sevens, inputFormatted);
                }
                else if(hasThrees == true && hasFours == true){
                    printCombo(fours, threes, inputFormatted);
                }
                else if(hasThrees == true && hasFours == false){
                    printThrees(threes, inputFormatted);
                }
                else if(hasFours == true && hasThrees == false){
                    printFours(fours, inputFormatted);
                }
                else{
                    printDefault(inputFormatted);
                }



            
            }
            //Take more input if you want to
            input = sc.nextDouble();
         }

         sc.close();
           

            
            

    }

    //Format output for ten length word
    public static void printTen(ArrayList<Word> tens){
        for(int i = 0; i < tens.size(); i++){
            System.out.printf("1-%s\n",tens.get(i).getWord());
        }
        
    }

    //Format output for seven length word
    public static void printSeven(ArrayList<Word> sevens, String inputFormatted){
        String area = inputFormatted.substring(0,3);
        for(int i = 0; i < sevens.size(); i++){
            System.out.printf("1-%s-%s\n",area,sevens.get(i).getWord());
        }
    }

    //Format output for combination of threes and fours
    public static void printCombo(ArrayList<Word> fours, ArrayList<Word> threes, String inputFormatted){
        String area = inputFormatted.substring(0,3);
        for(int i = 0; i < threes.size(); i++){
            for(int j = 0; j < fours.size(); j++){
                System.out.printf("1-%s-%s-%s\n",area,threes.get(i).getWord(),fours.get(j).getWord());
            }
        }
    }

    //Format output for threes
    public static void printThrees(ArrayList<Word> threes, String inputFormatted){
        String area = inputFormatted.substring(0,3);
        String number = inputFormatted.substring(6);
        for(int i = 0; i < threes.size(); i++){
            System.out.printf("1-%s-%s-%s\n",area,threes.get(i).getWord(),number);
        }
    }

    //Format ouput for fours
    public static void printFours(ArrayList<Word> fours, String inputFormatted){
        String area = inputFormatted.substring(0,3);
        String exchange = inputFormatted.substring(3,6);
        for(int i = 0; i < fours.size(); i++){
            System.out.printf("1-%s-%s-%s\n",area,exchange,fours.get(i).getWord());
        }
    }

    public static void printDefault(String inputFormatted){
        String area = inputFormatted.substring(0,3);
        String exchange = inputFormatted.substring(3,6);
        String number = inputFormatted.substring(6);
        System.out.printf("1-%s-%s-%s\n",area,exchange,number);
    }

    

    



    //Provided string to int code
    public static double strInt(String str){
        int i;
        String newStr; //to store string of digits
        newStr="";
        for(i=0;i<str.length();i++){
           switch(str.charAt(i)){
           case 'a':
           case 'b':
           case 'c': newStr+='2';
                     break;
           case 'd':
           case 'e':
           case 'f':newStr+='3';
                    break;
           case 'g':
           case 'h':
           case 'i':newStr+='4';
                    break;
           case 'j':
           case 'k':
           case 'l':newStr+='5';
                    break;
           case 'm':
           case 'n':
           case 'o':newStr+='6';
                    break;
           case 'p':
           case 'q':
           case 'r':
           case 's':newStr+='7';
                    break;
           case 't':
           case 'u':
           case 'v':newStr+='8';
                    break;
           case 'w':
           case 'x':
           case 'y':
           case 'z':newStr+='9';
           }//end of case
        } //end of loop
        return Double.parseDouble(newStr);
    } 
    //provided hash code
    public static int myHash(double d){
        double a = ((Math.sqrt(5)-1)/2);
        double hashed_value = Math.floor(994*(d*a % 1));
        return (int) hashed_value;
    }
    
}

//Word class for input in linked list
class Word{
    double numberRep;
    String word;

    public Word(double numberRep, String word){
        this.numberRep = numberRep;
        this.word = word;
    }

    int getWordLength(){
        return this.word.length();
    }

    String getWord(){
        return this.word.toUpperCase();
    }

    double getNumber(){
        return this.numberRep;
    }

}


    
   