package solita.citybike.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "journeys")
public class BikeJourney {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    private Long id;

    @Column(name = "departure_time")
    @NotNull(message = "Departure time is needed")
    private LocalDateTime departureDate;

    @Column(name = "return_time")
    @NotNull(message = "Return time is needed")
    private LocalDateTime returnDate;

    @OneToOne
    @NotNull(message = "Departure station is needed")
    private BikeStation departureStation;

    @OneToOne
    @NotNull(message = "Return station is needed")
    private BikeStation returnStation;


    @Column(name = "covered_distance")
    @NotNull(message = "Covered distance is needed")
    private double coveredDistance;

    @Column(name = "duration")
    @NotNull(message = "Journey duration is needed")
    private int duration;

    public BikeJourney(){

    }

    public BikeJourney(LocalDateTime departureDate, LocalDateTime returnDate, double coveredDistance, int duration) {

        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.coveredDistance = coveredDistance;
        this.duration = duration;
    }

    public BikeStation getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(BikeStation departureStation) {
        this.departureStation = departureStation;
    }

    public BikeStation getReturnStation() {
        return returnStation;
    }

    public void setReturnStation(BikeStation returnStation) {
        this.returnStation = returnStation;
    }

    public void setCoveredDistance(double coveredDistance) {
        this.coveredDistance = coveredDistance;
    }




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



}
