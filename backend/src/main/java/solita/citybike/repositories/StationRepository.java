package solita.citybike.repositories;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import solita.citybike.models.BikeJourney;
import solita.citybike.models.BikeStation;

import java.util.List;

@Repository
public interface StationRepository extends PagingAndSortingRepository<BikeStation, Integer> {


    Iterable<BikeStation> saveAll(Iterable<BikeStation> allStations);
    BikeStation save(BikeStation station);


    @Cacheable("stations")
    List<BikeStation> findByStationId(Integer id);

    Iterable<BikeJourney> deleteAll();

    @Query(value = "SELECT COUNT(id) FROM stations", nativeQuery = true)
    Integer getStationCount();

    @Query(value = "SELECT COUNT(j.id) FROM journeys j JOIN stations s ON j.departure_station_id = s.id WHERE s.station_id = :id", nativeQuery = true)
    Integer getDepartingStationCountByStationId(Integer id);
}
