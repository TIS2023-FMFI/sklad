package app;

import Entity.Position;
import Exceptions.FileNotFound;
import Exceptions.WrongStringFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class LoadPositions {

    private static final int POSTION_NAME_LENGTH = 5;

    /***
     * List of rows from the file.
     */
    public List<String> rows = new ArrayList<>();
    private List<Position> finalPositions = new ArrayList<>();


    /***
     * Constructor, that loads data from a file into memory.
     * @throws FileNotFound when file could not be found.
     */
    public LoadPositions(String fileName) throws FileNotFound {

        try (FileInputStream file = new FileInputStream(fileName);) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while (line != null) {
                if(!line.equals("\n")) {
                    rows.add(line);
                }
                line = reader.readLine();
            }

        }
        catch (IOException e){
            throw new FileNotFound(fileName);
        }
    }

    public List<Position> getFinalPositions() {
        return finalPositions;
    }

    /***
     * Method, that checks loaded data. When data are checked, it creates new Position
     * and add it to the finalPositions where all created positions are stored.
     * @throws WrongStringFormat when name of the position has wrong format or first letter in the row isn't 'n' or 'v'
     */
    public int addPositions() throws WrongStringFormat {
        int addedPosition = 0;
        for(String row : rows){
            List<String> splittedPositions = new ArrayList<>(List.of(row.split("-")));
            String isTall = splittedPositions.remove(0);
            if (!(isTall.equals("n") || isTall.equals("v")) ){
                throw new WrongStringFormat(row);
            }

            boolean tall = isTall.equals("v");

            for(String postionName : splittedPositions){
                if(savePosition(postionName, tall)){
                    addedPosition++;
                }
                else{
                    throw new WrongStringFormat(postionName);
                }
            }
        }
        return addedPosition;
    }

    /***
     * Method that saves the finalPositions to the database.
     * @return true if the positions were saved to the database, false otherwise.
     */
    public boolean saveToDB(){
        DatabaseHandler databaseHandler = Warehouse.getInstance().getDatabaseHandler();
        if(databaseHandler.savePositionsToDB(finalPositions)){
            return true;
        }
        System.out.println("CHYBA PRI UPLOADOVANI UDAJOV");
        return false;
    }

    /***
     * Method that saves the position to the finalPositions list.
     * @param name Name of the position.
     * @param isTall True if the position is tall, false otherwise.
     * @return true if the position was saved, false otherwise.
     */
    protected boolean savePosition(String name, boolean isTall) {
        if(!checkName(name)){
            return false;
        }
        Position position = new Position(name, isTall);
        if(finalPositions.contains(position)){
            return false;
        }
        finalPositions.add(position);
        return true;
    }

    /***
     * Method that checks if the name of the position is in the right format.
     * @param name Name of the position.
     * @return true if the name is in the right format, false otherwise.
     */
    protected boolean checkName(String name){
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
    
}
