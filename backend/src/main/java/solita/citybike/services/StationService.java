package solita.citybike.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import solita.citybike.models.BikeStation;
import solita.citybike.repositories.JourneyRepository;
import solita.citybike.repositories.StationRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class StationService {


    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private StationRepository stationRepository;

    /**
     *
     * @param pageNumber Which page the user is on the pagination
     * @param pageSize How many records we want the query to return
     * @param sortBy Based on which value we want to sort the stations on
     * @return If the query fails/parameters yield no results, we return an empty List.
     * Otherwise we return a List of BikeStations matching the query
     */
    public List<BikeStation> getAllStations(Integer pageNumber, Integer pageSize, String sortBy) {



        //Pageable is responsible for holding pagination and sorting parameters
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

        Page<BikeStation> pagedResult = stationRepository.findAll(paging);

        if(pagedResult.isEmpty()){
            return new ArrayList<>();
        }

        for(BikeStation b : pagedResult){
            this.addJourneyCounts(b);
        }

        return pagedResult.getContent();
    }


    /**
     * Service class wrapper function for the repository's getStationCount()
     * that returns how many stations there is present in the DB
     * @return Integer that represents the amount of station entities in the DB
     */
    public Integer getStationCount(){
        return this.stationRepository.getStationCount();
    }


    /**
     * Takes a station and queries the DB for how many
     * departing and returning journeys start/end to that
     * particular station. Gets called in getAllStations() after
     * the stations have been fetched.
     *
     * @param station The BikeStation you want to fetch the count data for
     */
    private void addJourneyCounts(BikeStation station){

        int departingJourneys = this.stationRepository.getDepartingStationCountByStationId(station.getStationId());
        station.setDepartingJourneysCount(departingJourneys);

        int returningJourneys = this.stationRepository.getReturningStationCountByStationId(station.getStationId());
        station.setReturningJourneysCount(returningJourneys);

    }


}
