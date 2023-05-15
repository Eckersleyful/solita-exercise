package solita.citybike.repositories;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import solita.citybike.models.BikeJourney;
import solita.citybike.models.BikeStation;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface JourneyRepository extends PagingAndSortingRepository<BikeJourney, Integer> {



    List<BikeJourney> findAll();

    Iterable<BikeJourney> saveAll(Iterable<BikeJourney> validJourneys);

    BikeJourney save(BikeJourney journey);

    Iterable<BikeJourney> deleteAll();

    @Query(value = "SELECT COUNT(id) FROM journeys", nativeQuery = true)
    Integer getJourneyCount();
}
