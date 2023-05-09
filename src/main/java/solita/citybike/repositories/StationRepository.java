package solita.citybike.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import solita.citybike.models.BikeJourney;
import solita.citybike.models.BikeStation;

import java.util.List;

@Repository
public interface StationRepository extends PagingAndSortingRepository<BikeStation, Integer> {


    Iterable<BikeStation> saveAll(Iterable<BikeStation> allStations);

}
