package utils;

import java.io.File;
import java.util.*;

public class TxtUtils {

    private static final char DEFAULT_SEPARATOR = '|';

    public static HashMap<String, HashMap<String, ArrayList<String>>> populateElementDefinitions() throws Exception{
        HashMap<String, HashMap<String, ArrayList<String>>> txtValues = new HashMap<>();
        String txtFile = System.getenv("ELEMENT_DEFINITION");
        String currentPage = null;

        Scanner scanner = new Scanner(new File(txtFile));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }
            ArrayList<String> txtLine = parseLine(line);
            if (txtLine.size() == 1) {
                currentPage = txtLine.get(0);
                txtValues.put(currentPage, new HashMap<>());
            } else {
                if (txtLine.get(0).equalsIgnoreCase("uniqueId")) {
                    continue;
                }
                txtValues.get(currentPage).put(txtLine.get(0),
                        new ArrayList<>(Arrays.asList(txtLine.get(1), txtLine.get(2))));
            }
        }
        scanner.close();

        return txtValues;
    }

    private static ArrayList<String> parseLine (String line){
        ArrayList<String> result = new ArrayList<>();
        char[] lineCharacters = line.toCharArray();
        StringBuilder element = new StringBuilder();
        int lineLength = lineCharacters.length;
        for (int i=0; i < lineLength; i++){
            if (lineCharacters[i] == DEFAULT_SEPARATOR || i == lineLength-1){
                if (i == lineLength-1){
                    element.append(lineCharacters[i]);
                }
                result.add(element.toString().trim());
                element = new StringBuilder();
                continue;
            }
            element.append(lineCharacters[i]);
        }
        return result;
    }
}