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

    public List<BikeStation> getAllStations(Integer pageNumber, Integer pageSize, String sortBy) {


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


    public Integer getStationCount(){
        return this.stationRepository.getStationCount();
    }

    private void addJourneyCounts(BikeStation station){

        int departingJourneys = this.stationRepository.getDepartingStationCountByStationId(station.getStationId());
        station.setDepartingJourneysCount(departingJourneys);

        int returningJourneys = this.stationRepository.getReturningStationCountByStationId(station.getStationId());
        station.setReturningJourneysCount(returningJourneys);

    }


}
