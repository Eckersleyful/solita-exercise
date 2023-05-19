package solita.citybike.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;
import solita.citybike.models.BikeJourney;
import solita.citybike.models.BikeStation;
import solita.citybike.repositories.JourneyRepository;
import solita.citybike.repositories.StationRepository;
import solita.citybike.utils.Unzipper;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;


@Service
public class PopulatingService {



    //Drop all rows with duration less than 10
    private static final int MINIMUM_DURATION = 5;

    //Drop all rows with distance less than 10
    private static final double MINIMUM_DISTANCE = 10.0;

    //After splitting the csv rows, this is the correct amount of elements there should be
    private static final int CORRECT_ENTRY_FIELDS = 8;

    //The size of the SQL batch inserts, has to correspond to spring.jpa.properties.hibernate.jdbc.batch_size in application.properties
    private static final int BATCH_SIZE = 25000;

    //The index of the departure station ID in CSV entry
    private static final int DEPARTURE_STATION_ID_INDEX = 2;

    //The index of the departure station name in CSV entry
    private static final int DEPARTURE_STATION_NAME_INDEX = 3;

    //The index of the return station id in CSV entry
    private static final int RETURN_STATION_ID_INDEX = 4;

    //The index of the return station NAME in CSV entry
    private static final int RETURN_STATION_NAME_INDEX = 5;


    @Autowired
    private JourneyRepository repository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private JourneyService journeyService;


    /**
     * The most bloated function in the application.
     * Responsible for clearing the DB and re-populating it
     * with the .csv data found in /resources/csv
     *
     * @return True when all the files have been iterated through
     */
    public boolean populateData() {



        Logger.info("Clearing tables for fresh data");

        //Clear the tables "journeys" and "stations"
        this.repository.deleteAllInBatch();

        this.stationRepository.deleteAll();

        //Clear the sequence table of journeys
        //and insert the starting sequence index
        this.journeyService.initializeSequenceTable();


        /*
        Unzip the .csv and read their content into an ArrayList of String arrays.
        Each file's content is in one array.
         */
        Unzipper unzipper = new Unzipper();
        ArrayList<String[]> csvBikeJourneys = unzipper.getJourneysFromCsv();

        ArrayList<BikeJourney> validJourneys = new ArrayList<>();

        //We keep track of unique stations in a HashMap and then compare a new
        //found station against it
        HashMap<Integer, BikeStation> allStations = new HashMap<>();

        for(String [] s : csvBikeJourneys){

            boolean skipFirst = true;

            Logger.info("Iterating and inserting contents of a file to DB");

            for(String journeyEntry : s){

                //Awkward way to skip the headers of the .csv
                if(skipFirst){
                    skipFirst = false;
                    continue;
                }

                //Try creating a BikeJourney object from one csv line, returns null if invalid line
                BikeJourney journey = createJourneyFromEntry(journeyEntry);


                //Do the same for the station data found in the line, also returns null if invalid data
                BikeStation departStation = createStationFromEntry(journeyEntry, true);

                BikeStation returnStation = createStationFromEntry(journeyEntry, false);

                if(departStation == null || returnStation == null || journey == null){
                    continue;
                }

                //Check if the created station is not present in the DB, otherwise, query for it
                departStation = handleNewStation(departStation, allStations);
                returnStation = handleNewStation(returnStation, allStations);

                //Set the station entity references to the created BikeJourney object
                //So Journey HAS Station, but Station is unaware of Journeys
                journey.setDepartureStation(departStation);
                journey.setReturnStation(returnStation);

                /*
                Add the ready BikeJourney to a list to be batch inserted after the whole
                file has been processed
                 */
                validJourneys.add(journey);

                if(validJourneys.size() > BATCH_SIZE){
                    this.repository.saveAll(validJourneys);
                    validJourneys.clear();
                }

            }
            Logger.info("Inserting file done");

            //Most likely there will be left over data in the list, so we insert it and clear again
            this.repository.saveAll(validJourneys);
            validJourneys.clear();

        }
        return true;

    }

    /**
     * Deducts if the new station has already been encountered in the iteration process or not.
     *
     * @param station The station to be added
     * @param allStations The HashMap of all stations found so far
     * @return True if the station is not present in the map, false otherwise
     */
    private boolean isStationNew(BikeStation station, HashMap<Integer, BikeStation> allStations) {

        return !allStations.containsKey(station.getStationId());

    }

    /**
     * Either saves a station if its not yet encountered, or
     * then queries for it if it has been encountered to save the reference
     * to the Journey object
     * @param newStation  The station to be processed
     * @param allStations The HashMap containing all encountered stations
     * @return
     */
    private BikeStation handleNewStation(BikeStation newStation, HashMap<Integer, BikeStation> allStations){

        /*
        If the new station is not yet in the DB, save it
        and put the reference to the saved object into a HashMap to save DB lookups
        for the next inserts
        */
        if(isStationNew(newStation, allStations)){
            newStation = this.stationRepository.save(newStation);
            allStations.put(newStation.getStationId(), newStation);
        }
        else{
            newStation = this.stationRepository.findByStationId(newStation.getStationId()).get(0);
        }

        return newStation;
    }

    /**
     * Creates a station object from a line of the csv file.
     * Creates the object either from departure or return parameters.
     * @param journeyEntry The String that represents one line from the .csv file
     * @param departure    True if you want to create the station based on departure
     *                     parameters, false if return.
     * @return             The new created Station
     */

    private BikeStation createStationFromEntry(String journeyEntry, boolean departure) {

        String [] entryValues = journeyEntry.split(",");

        if(entryValues.length != CORRECT_ENTRY_FIELDS){
            return null;
        }

        if(departure) {
            return deriveStationFromEntry(entryValues, DEPARTURE_STATION_ID_INDEX, DEPARTURE_STATION_NAME_INDEX);
        }

        return deriveStationFromEntry(entryValues, RETURN_STATION_ID_INDEX, RETURN_STATION_NAME_INDEX);
    }


    /**
     * Helper function to create a BikeStation from the given line of .csv file
     * and index of the id and name.
     * @param entryValues  One line of the .csv file split from "," to an array
     * @param idIndex      The index from which the id of the station is derived
     * @param nameIndex    The index from which the name of the station is derived
     * @return             A new BikeStation created from the found id and name
     */
    private BikeStation deriveStationFromEntry(String [] entryValues, int idIndex, int nameIndex){

        if(entryValues[idIndex] == null || entryValues[nameIndex] == null){
            return null;
        }

        return new BikeStation(Integer.valueOf(entryValues[idIndex]), entryValues[nameIndex]);
    }


    /**
     * Creates a JourneyStation object from one line of the .csv
     *
     * @param  journeyEntry String representing one line of the csv
     * @return              The newly created object
     */
    private BikeJourney createJourneyFromEntry(String journeyEntry) {


        String [] entryValues = journeyEntry.strip().split(",");

        if(entryValues.length != CORRECT_ENTRY_FIELDS){
            return null;
        }


        //Try to parse the dates and numeral values from the line,
        //returning a null object if the line has invalid data
        LocalDateTime departureDate;
        LocalDateTime returnDate;

        try {
            departureDate = LocalDateTime.parse(entryValues[0]);
            returnDate = LocalDateTime.parse(entryValues[1]);
        }
        catch(DateTimeParseException e){
            return null;
        }

        double coveredDistance;

        int journeyDuration;


        try{
            coveredDistance = Double.parseDouble(entryValues[6]);

            journeyDuration = Integer.parseInt(entryValues[7]);

        }
        catch(NumberFormatException e){
            return null;
        }

        //Skip all journeys with less distance and duration than minimum constants
        if(coveredDistance < MINIMUM_DISTANCE || journeyDuration < MINIMUM_DURATION) {
            return null;
        }

        return new BikeJourney(departureDate, returnDate, coveredDistance, journeyDuration);

    }

}
