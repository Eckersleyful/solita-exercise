package solita.citybike.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solita.citybike.models.BikeJourney;


@Repository
public interface JourneyRepository extends JpaRepository<BikeJourney, Integer> {


}
