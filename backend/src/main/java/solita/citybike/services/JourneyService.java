package solita.citybike.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import solita.citybike.models.BikeJourney;
import solita.citybike.repositories.JourneyRepository;
import solita.citybike.repositories.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class JourneyService {



    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private StationRepository stationRepository;


    /**
     * Fetches journeys according to sorting and ordering parameters and returns them to the Controller
     * @param pageNumber Which pagination page you want to query
     * @param pageSize   How many records per page/query you want to return
     * @param sortBy     On which parameter you want to sort the results
     * @param order      On which parameter you want to order the results
     * @return
     */

    public List<BikeJourney> getAllJourneys(Integer pageNumber, Integer pageSize, String sortBy, String order) {

        Sort sort;

        // Is there a more sophisticated way to do this, feels odd
        if(sortBy.equals("departureStation")){
            sort = Sort.by("departureStation.stationName");
        }
        else if(sortBy.equals("returnStation")){
            sort = Sort.by("returnStation.stationName");
        }
        else{
            sort = Sort.by(sortBy);
        }


        //This can't be right
        if(order.equals("asc")){
            sort = sort.ascending();
        }
        else if(order.equals("desc")){
            sort = sort.descending();
        }


        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);

        Page<BikeJourney> pagedResult = journeyRepository.findAll(paging);

        //We still go through with the request even if the query returns nothing
        if(pagedResult.isEmpty()){
            return new ArrayList<>();
        }

        return pagedResult.getContent();

    }

    /**
     * Fetches how many joruneys there are in the DB
     *
     * @return Integer value of the journey count
     */
    public Integer getJourneyCount(){
        return this.journeyRepository.getJourneyCount();
    }


    public void initializeSequenceTable(){
        this.journeyRepository.dropSequenceTable();
        this.journeyRepository.createSequenceTable();
        this.journeyRepository.insertSequenceStart();
    }
}
