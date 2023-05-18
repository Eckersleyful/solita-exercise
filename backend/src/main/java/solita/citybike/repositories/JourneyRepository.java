package solita.citybike.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
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

    Iterable<BikeJourney> deleteAllInBatch();

    @Query(value = "SELECT COUNT(id) FROM journeys", nativeQuery = true)
    Integer getJourneyCount();

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS seq", nativeQuery = true)
    void dropSequenceTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE seq ( next_val bigint(20) DEFAULT NULL)", nativeQuery = true)
    void createSequenceTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO seq VALUES(1)", nativeQuery = true)
    void insertSequenceStart();

}
