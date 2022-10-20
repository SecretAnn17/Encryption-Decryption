package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;

public class Main {

    private static boolean isDataSet = false;

    public static void main(String[] args) throws IOException {
        String mode = "enc";
        int key = 0;
        String data = "";
        String fileInPath = null;
        String fileOutPath = null;
        String alg = "shift";

        for (int i = 0; i < args.length; i = i + 2) {
            if (args[i].equals("-mode")) {
                mode = args[i + 1];
            } else if (args[i].equals("-key")) {
                key = Integer.valueOf(args[i + 1]);
            } else if (args[i].equals("-data")) {
                data = args[i + 1];
                isDataSet = true;
            } else if (args[i].equals("-alg")) {
                alg = args[i + 1];
            } else if (args[i].equals("-out")) {
                fileOutPath = args[i+1];
            } else if (args[i].equals("-in")&& !isDataSet){
                fileInPath = args[i+1];
            }
        }

        Algorithm algorithm = AlgorithmFactory.create(alg);

        switch (mode){
            case "enc" :
                AlgorithmFactory.saveEncryptedDecryptedText(fileOutPath, algorithm.encryptText(AlgorithmFactory.getStringToManipulate(data, fileInPath, isDataSet), key));
                break;
            case "dec" :
                AlgorithmFactory.saveEncryptedDecryptedText(fileOutPath, algorithm.decryptText(AlgorithmFactory.getStringToManipulate(data, fileInPath, isDataSet), key));
                break;
        }
    }
}

class AlgorithmFactory {

    public static Algorithm create(String type) {

        switch (type) {
            case "unicode": return new UnicodeAlgorithm();
            case "shift": return new ShiftAlgorithm();
        }
        return null;
    }

    public static void saveEncryptedDecryptedText(String fileOutPath, String textAfterChanges) throws IOException {

        if (fileOutPath != null) {

            File file = new File(fileOutPath);

            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(textAfterChanges);
            } catch (FileNotFoundException e) {
                System.out.println(format("An error occurred: %s", e.getMessage()));
            }
        } else {
            System.out.println(textAfterChanges);
        }
    }

    public static String getStringToManipulate(String data, String fileInPath, boolean isDataSet){

        String stringData = "";

        if(isDataSet) {
            return data;
        } else if (fileInPath != null) {
            try {
                File file = new File(fileInPath);
                Scanner newFile = new Scanner(file);
                while (newFile.hasNextLine()) {
                    stringData = newFile.nextLine();
                }
                newFile.close();
            } catch (FileNotFoundException e) {
                System.out.println(format("An error occurred: %s", e.getMessage()));
            }
        }
        return stringData;
    }
}


abstract class Algorithm {

    String textAfterChanges = "";
    abstract String encryptText(String text, int key);
    abstract String decryptText(String text, int ke);
}

class UnicodeAlgorithm extends Algorithm {

    @Override
    String encryptText(String text, int key) {
        for(int i = 0; i < text.length(); i++)  {
            textAfterChanges = textAfterChanges + (char)(Integer.valueOf(text.charAt(i)) + key);
        }
        return textAfterChanges;
    }

    @Override
    String decryptText(String text, int key) {
        for(int i = 0; i < text.length(); i++)  {
            textAfterChanges = textAfterChanges + (char)(Integer.valueOf(text.charAt(i)) - key);
        }
        return textAfterChanges;
    }
}

class ShiftAlgorithm extends Algorithm {

    int letterPosition=0;

    final static Character FIRST_UPPERCASE_LETTER  = 'A';
    final static Character LAST_UPPERCASE_LETTER  = 'Z';
    final static Character FIRST_LOWERCASE_LETTER  = 'a';
    final static Character LAST_LOWERCASE_LETTER  = 'z';

    final static Integer FIRST_UPPERCASE_LETTER_POSSITION  = Integer.valueOf(FIRST_UPPERCASE_LETTER);
    final static Integer LAST_UPPERCASE_LETTER_POSSITION  = Integer.valueOf(LAST_UPPERCASE_LETTER);
    final static Integer FIRST_LOWERCASE_LETTER_POSSITION  = Integer.valueOf(FIRST_LOWERCASE_LETTER);
    final static Integer LAST_LOWERCASE_LETTER_POSSITION  = Integer.valueOf(LAST_LOWERCASE_LETTER);


    @Override
    String encryptText(String text, int key) {
        for(int i = 0; i < text.length(); i++)  {
            if (text.charAt(i) >= FIRST_LOWERCASE_LETTER && text.charAt(i) <= LAST_LOWERCASE_LETTER) {
                if(Integer.valueOf(text.charAt(i))+key>LAST_LOWERCASE_LETTER_POSSITION) {
                    letterPosition = Integer.valueOf(text.charAt(i)) + key - LAST_LOWERCASE_LETTER_POSSITION
                            + FIRST_LOWERCASE_LETTER_POSSITION - 1;
                    textAfterChanges = textAfterChanges + (char)(letterPosition);
                } else {
                    textAfterChanges = textAfterChanges + (char) (text.charAt(i) + key);
                }
            } else if (text.charAt(i) >= FIRST_UPPERCASE_LETTER && text.charAt(i) <= LAST_UPPERCASE_LETTER) {
                if(Integer.valueOf(text.charAt(i))+key>LAST_UPPERCASE_LETTER_POSSITION) {
                    letterPosition = Integer.valueOf(text.charAt(i))+key - LAST_UPPERCASE_LETTER_POSSITION
                            + FIRST_LOWERCASE_LETTER_POSSITION - 1;
                    textAfterChanges = textAfterChanges + (char)(letterPosition);
                } else {
                    textAfterChanges = textAfterChanges + (char) (text.charAt(i) + key);
                }
            }
            else {
                textAfterChanges = textAfterChanges + text.charAt(i);
            }
        }
        return textAfterChanges;
    }

    @Override
    String decryptText(String text, int key) {
        for(int i = 0; i < text.length(); i++)  {
            if (text.charAt(i) >= FIRST_LOWERCASE_LETTER && text.charAt(i) <= LAST_LOWERCASE_LETTER) {
                if(Integer.valueOf(text.charAt(i)) - key < FIRST_LOWERCASE_LETTER) {
                    letterPosition = LAST_LOWERCASE_LETTER - (FIRST_LOWERCASE_LETTER - 1
                            - (Integer.valueOf(text.charAt(i))-key));
                    textAfterChanges = textAfterChanges + (char)(letterPosition);
                } else {
                    textAfterChanges = textAfterChanges + (char) (text.charAt(i) - key);
                }
            } else if (text.charAt(i) >= FIRST_UPPERCASE_LETTER && text.charAt(i) <= LAST_UPPERCASE_LETTER) {
                if(Integer.valueOf(text.charAt(i)) - key < FIRST_UPPERCASE_LETTER) {
                    letterPosition = LAST_UPPERCASE_LETTER - (FIRST_UPPERCASE_LETTER - 1
                            - (Integer.valueOf(text.charAt(i))-key));
                    textAfterChanges = textAfterChanges + (char)(letterPosition);
                } else {
                    textAfterChanges = textAfterChanges + (char) (text.charAt(i) - key);
                }
            }
            else {
                textAfterChanges = textAfterChanges + text.charAt(i);
            }
        }
        return textAfterChanges;
    }
}

