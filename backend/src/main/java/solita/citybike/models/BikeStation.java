package solita.citybike.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
@Table(name = "stations")
public class BikeStation {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer id;

    @Column(name = "station_id")
    private Integer stationId;

    @Column(name = "station_name")
    private String stationName;

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
