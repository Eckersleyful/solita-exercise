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


    @GetMapping
    public ResponseEntity<List<BikeStation>> getAllJourneys(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "stationName") String sortBy
    ){
        List<BikeStation> journeyList = this.stationService.getAllStations(pageNumber, pageSize, sortBy);


        return new ResponseEntity<List<BikeStation>>(journeyList, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> stationCount(){
        return new ResponseEntity<Integer>(this.stationService.getStationCount(), new HttpHeaders(), HttpStatus.OK);

    }


}
