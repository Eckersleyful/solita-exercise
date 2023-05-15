package solita.citybike.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import solita.citybike.models.BikeJourney;

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
