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

    @Autowired
    private JourneyRepository repository;

    @Autowired
    private StationRepository stationRepository;

    public boolean populateData() {

        this.repository.deleteAll();

        this.stationRepository.deleteAll();

        Unzipper unzipper = new Unzipper();
        ArrayList<String[]> csvBikeJourneys = unzipper.getJourneysFromCsv();

        ArrayList<BikeJourney> validJourneys = new ArrayList<>();

        HashMap<Integer, BikeStation> allStations = new HashMap<>();

        for(String [] s : csvBikeJourneys){

            boolean skipFirst = true;

            for(String journeyEntry : s){


                //Awkward way to skip the headers of the .csv
                if(skipFirst){
                    skipFirst = false;
                    continue;
                }

                //Try creating a BikeJourney object from one csv line, returns null if invalid line
                BikeJourney journey = createJourneyFromEntry(journeyEntry);

                if(journey == null){
                    continue;
                }

                //Do the same for the station data found in the line, also returns null if invalid data
                BikeStation departStation = createStationFromEntry(journeyEntry, true);

                BikeStation returnStation = createStationFromEntry(journeyEntry, false);

                if(departStation == null || returnStation == null){
                    continue;
                }

                //Check if the created station is not present in the DB, otherwise, query for it
                departStation = handleNewStation(departStation, allStations);
                returnStation = handleNewStation(returnStation, allStations);

                //Set the station entity references to the created BikeJourney object
                //So Journey HAS Station, but Station is unaware of Journeys
                journey.setDepartureStation(departStation);
                journey.setReturnStation(returnStation);

                /*Add the ready BikeJourney to a list to be batch inserted after the whole
                file has been processed
                 */
                validJourneys.add(journey);

                if(validJourneys.size() > BATCH_SIZE){
                    this.repository.saveAll(validJourneys);
                    validJourneys.clear();
                }

            }
            Logger.info("Dumping file");

            //Batch insert the list to speed it up and clear the list for next files
            this.repository.saveAll(validJourneys);
            validJourneys.clear();

        }

        return true;

    }

    private boolean isStationNew(BikeStation station, HashMap<Integer, BikeStation> allStations) {

        return !allStations.containsKey(station.getStationId());

    }

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

    private BikeStation createStationFromEntry(String journeyEntry, boolean departure) {

        String [] entryValues = journeyEntry.split(",");

        if(entryValues.length != CORRECT_ENTRY_FIELDS){
            return null;
        }



        Integer stationId;

        String stationName;

        if(departure) {

            if(entryValues[2] == null || entryValues[3] == null){
                return null;
            }

            stationId = Integer.valueOf(entryValues[2]);
            stationName = entryValues[3];

            return new BikeStation(stationId, stationName);


        }


        if(entryValues[4] == null || entryValues[5] == null){
            return null;
        }

        stationId = Integer.valueOf(entryValues[4]);
        stationName = entryValues[5];

        return new BikeStation(stationId, stationName);
    }


    private BikeJourney createJourneyFromEntry(String journeyEntry) {


        String [] entryValues = journeyEntry.strip().split(",");

        if(entryValues.length != CORRECT_ENTRY_FIELDS){
            return null;
        }

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


        if(coveredDistance < MINIMUM_DISTANCE || journeyDuration < MINIMUM_DURATION) {
            return null;
        }



        return new BikeJourney(departureDate, returnDate, coveredDistance, journeyDuration);


    }
}
