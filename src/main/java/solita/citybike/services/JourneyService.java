package solita.citybike.services;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import solita.citybike.models.BikeJourney;
import solita.citybike.repositories.JourneyRepository;
import solita.citybike.utils.Unzipper;

import java.util.List;

@Service
@Transactional
public class JourneyService {


    @Autowired
    private JourneyRepository repository;



    public List<BikeJourney> listAllJourneys() {
        return repository.findAll();
    }

    public void populateData() {

        this.repository.deleteAll();

        List<BikeJourney> csvBikeJourneys = Unzipper.getJourneysFromCsv();







    }


}
