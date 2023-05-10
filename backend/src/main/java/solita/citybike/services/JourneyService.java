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
public class JourneyService {



    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private StationRepository stationRepository;




    public List<BikeJourney> getAllJourneys(Integer pageNumber, Integer pageSize, String sortBy) {

        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

        Page<BikeJourney> pagedResult = journeyRepository.findAll(paging);

        if(pagedResult.isEmpty()){
            return new ArrayList<>();
        }

        return pagedResult.getContent();

    }

    public String test() {
        return "cat";
    }


}
