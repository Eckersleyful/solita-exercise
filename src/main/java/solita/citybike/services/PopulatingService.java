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
import java.util.List;


@Service
public class PopulatingService {



    //Drop all rows with duration less than 10
    private static final int MINIMUM_DURATION = 5;

    //Drop all rows with distance less than 10
    private static final double MINIMUM_DISTANCE = 10.0;

    //After splitting the csv rows, this is the correct amount of elements there should be
    private final int CORRECT_ENTRY_FIELDS = 8;

    //The size of the SQL batch inserts, has to correspond to spring.jpa.properties.hibernate.jdbc.batch_size in application.properties
    private final int BATCH_SIZE = 30;

    @Autowired
    private JourneyRepository repository;

    @Autowired
    private StationRepository stationRepository;

    public boolean populateData() {


        Unzipper unzipper = new Unzipper();
        ArrayList<String[]> csvBikeJourneys = unzipper.getJourneysFromCsv();

        ArrayList<BikeJourney> validJourneys = new ArrayList<>();

        HashMap<Integer, BikeStation> allStations = new HashMap<>();



        for(String [] s : csvBikeJourneys){

            boolean skipFirst = true;

            for(String journeyEntry : s){

                if(skipFirst){
                    skipFirst = false;
                    continue;
                }

                BikeJourney journey = createJourneyFromEntry(journeyEntry);

                BikeStation departStation = createStationFromEntry(journeyEntry, true);

                BikeStation returnStation = createStationFromEntry(journeyEntry, false);

                if(departStation != null && isStationNew(departStation, allStations)){
                    allStations.put(departStation.getStationId(), departStation);
                }

                if(returnStation != null && isStationNew(returnStation, allStations)){
                    allStations.put(returnStation.getStationId(), returnStation);
                }


                if(journey != null){

                    validJourneys.add(journey);

                }

            }
            Logger.info("Dumping file");
            saveInBatch(validJourneys);
            validJourneys.clear();
        }

        this.stationRepository.saveAll(allStations.values());


        return true;

    }

    private void saveInBatch(ArrayList<BikeJourney> validJourneys){
        for (int i = 0; i < validJourneys.size(); i = i + BATCH_SIZE) {

            if( i + BATCH_SIZE > validJourneys.size()){
                List<BikeJourney> batch = validJourneys.subList(i, validJourneys.size() - 1);
                repository.saveAll(batch);
                break;
            }

            List<BikeJourney> batch = validJourneys.subList(i, i + BATCH_SIZE);
            repository.saveAll(batch);
        }
    }

    private boolean isStationNew(BikeStation station, HashMap<Integer, BikeStation> allStations) {

        return !allStations.containsKey(station.getStationId());

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

        String departStationId = entryValues[2];

        String returnStationid = entryValues[4];

        return new BikeJourney(departureDate, returnDate, departStationId, returnStationid, coveredDistance, journeyDuration);


    }
}
