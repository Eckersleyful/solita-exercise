package solita.citybike.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solita.citybike.models.BikeStation;
import solita.citybike.services.JourneyService;
import solita.citybike.services.PopulatingService;
import solita.citybike.services.StationService;

import java.util.List;

@RestController
@RequestMapping("stations")

public class StationController {

    @Autowired
    private PopulatingService populatingService;

    @Autowired
    private JourneyService journeyService;

    @Autowired
    private StationService stationService;

    /**
     * Fetches BikeStations based on parameters
     *
     * @param pageNumber
     * Which page the user is on the pagination
     * @param pageSize
     * How many records the query fetches
     * @param sortBy
     * On which parameter the query sorts the results on (default = name of the station)
     * @return
     * ResponseEntity<List<BikeJourney>>> A response entity with List of
     * the fetched BikeStations as Body and HTTP Status of 200
     */
    @GetMapping
    public ResponseEntity<List<BikeStation>> getAllJourneys(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "stationName") String sortBy
    ){
        List<BikeStation> journeyList = this.stationService.getAllStations(pageNumber, pageSize, sortBy);


        return new ResponseEntity<List<BikeStation>>(journeyList, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Fetches how many BikeStations there are in the DB
     *
     * @return The count of stations as Integer
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> stationCount(){
        return new ResponseEntity<>(this.stationService.getStationCount(), new HttpHeaders(), HttpStatus.OK);

    }



}
