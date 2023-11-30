package app;

import Entity.Position;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.WrongMethodTypeException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class LoadPositions {
    private static final String fileName = "loadPositionExample.txt";
    private static final int POSTION_NAME_LENGTH = 5;

    public List<String> rows = new ArrayList<>();
    public List<Position> finalPositions = new ArrayList<>();
    public LoadPositions() throws IOException, WrongStringFormatException {
        FileInputStream file = new FileInputStream(fileName);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                if(!line.equals("\n")) {
                    rows.add(line);
                }
                line = reader.readLine();
            }
        }

        addPositions();
    }

    private Position createPosition(){return null;}

    public int addPositions() throws WrongStringFormatException {
        int addedPosition = 0;
        for(String row : rows){
            List<String> splittedPositions = new ArrayList<>(List.of(row.split("-")));
            String isTall = splittedPositions.remove(0);
            if(isTall.length() > 1){
                throw new WrongStringFormatException();
            }
            if (! (isTall.equals("n") || isTall.equals("v")) ){
                throw new WrongStringFormatException(row);
            }

            boolean tall = false;
            if(isTall.equals("v")){
                tall = true;
            }
            for(String postionName : splittedPositions){
                if(savePosition(postionName, tall)){
                    addedPosition++;
                }
                else{
                    throw new WrongStringFormatException(postionName);
                }
            }
        }
        return addedPosition;
    }

    public boolean savePosition(String name, boolean isTall) throws WrongStringFormatException {
        if(!checkName(name)){
            return false;
        }
        Position position = new Position(name, isTall);
        finalPositions.add(position);
        return true;
    }

    public boolean checkName(String name){
        if(name.length() != POSTION_NAME_LENGTH){
            return false;
        }

        char firstLetter = name.charAt(0);
        if(firstLetter < 'A' || firstLetter > 'F'){    //v rozmedzi A a F
            return false;
        }

        try{
            Integer.parseInt(name.substring(1));
        }
        catch (NumberFormatException e ){
            return false;
        }
        return true;
    }
    public static void main(String[] args) {
        try{
            LoadPositions load = new LoadPositions();
            load.addPositions();
        }
     catch (IOException | WrongStringFormatException e) {
        System.err.println(e);
    }

    }
}
