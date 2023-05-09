package solita.citybike.services;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;
import solita.citybike.models.BikeJourney;
import solita.citybike.repositories.JourneyRepository;
import solita.citybike.utils.Unzipper;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class JourneyService {


    @Autowired
    private JourneyRepository repository;

    private final int CORRECT_ENTRY_FIELDS = 8;



    public List<BikeJourney> getAllJourneys(Integer pageNumber, Integer pageSize, String sortBy) {

        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

        Page<BikeJourney> pagedResult = repository.findAll(paging);

        if(pagedResult.isEmpty()){
            return new ArrayList<>();
        }

        return pagedResult.getContent();

    }

    public boolean populateData() {


        Unzipper unzipper = new Unzipper();
        ArrayList<String[]> csvBikeJourneys = unzipper.getJourneysFromCsv();

        ArrayList<BikeJourney> validJourneys = new ArrayList<>();

        for(String [] s : csvBikeJourneys){
            for(String journeyEntry : s){
                
                BikeJourney journey = createJourneyFromEntry(journeyEntry);

                if(journey != null){

                    validJourneys.add(journey);

                }
                
            }
            Logger.info("Dumping file");
            this.repository.saveAll(validJourneys);
            validJourneys.clear();
        }


        return true;








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


        if(coveredDistance < 10 || journeyDuration < 10) {
            return null;
        }

        return new BikeJourney(departureDate, returnDate, entryValues[2], entryValues[3],
                               entryValues[4], entryValues[5], coveredDistance, journeyDuration);


    }


}
