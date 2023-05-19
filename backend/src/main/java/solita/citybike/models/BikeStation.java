package solita.citybike.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "stations")
public class BikeStation {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "station_id")
    @NotNull(message = "Station id is needed")
    private Integer stationId;

    @Column(name = "station_name")
    @NotBlank(message = "Station name is needed")
    private String stationName;

    @JsonProperty
    @Transient
    private int departingJourneysCount;

    @JsonProperty
    @Transient
    private int returningJourneysCount;

    public int getDepartingJourneysCount() {
        return departingJourneysCount;
    }

    public void setDepartingJourneysCount(int departingJourneysCount) {
        this.departingJourneysCount = departingJourneysCount;
    }

    public int getReturningJourneysCount() {
        return returningJourneysCount;
    }

    public void setReturningJourneysCount(int returningJourneysCount) {
        this.returningJourneysCount = returningJourneysCount;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public BikeStation(Integer stationId, String name) {
        this.stationId = stationId;

        this.stationName = name;

    }

    public BikeStation() {
    }

    public BikeStation(Integer id, Integer stationId, String stationName) {
        this.id = id;
        this.stationId = stationId;
        this.stationName = stationName;
    }
}
