package solita.citybike.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "journeys")
public class BikeJourney {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    @JsonIgnore
    private Long id;

    @Column(name = "departure_time")
    private LocalDateTime departureDate;

    @Column(name = "return_time")
    private LocalDateTime returnDate;

    @Column(name = "departure_station_id")
    private String departureStationId;

    @Column(name = "return_station_id")
    private String returnStationId;


    @Column(name = "covered_distance")
    private double coveredDistance;

    @Column(name = "duration")
    private int duration;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getDepartureStationId() {
        return departureStationId;
    }

    public void setDepartureStationId(String departureStationId) {
        this.departureStationId = departureStationId;
    }


    public String getReturnStationId() {
        return returnStationId;
    }

    public void setReturnStationId(String returnStationId) {
        this.returnStationId = returnStationId;
    }

    public double getCoveredDistance() {
        return coveredDistance;
    }

    public void setCoveredDistance(int coveredDistance) {
        this.coveredDistance = coveredDistance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BikeJourney(){

    }

    public BikeJourney(LocalDateTime departureDate, LocalDateTime returnDate, String departureStationId,  String returnStationId, double coveredDistance, int duration) {
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.departureStationId = departureStationId;
        this.returnStationId = returnStationId;
        this.coveredDistance = coveredDistance;
        this.duration = duration;
    }
}
