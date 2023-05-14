package solita.citybike.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solita.citybike.models.BikeJourney;
import solita.citybike.services.JourneyService;
import solita.citybike.services.PopulatingService;
import solita.citybike.services.StationService;

import java.util.List;

@RestController
@RequestMapping("journeys")
public class JourneyController {

    @Autowired
    public PopulatingService populatingService;

    @Autowired
    public JourneyService journeyService;

    @GetMapping
    public ResponseEntity<List<BikeJourney>> getAllJourneys(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ){
        List<BikeJourney> journeyList = this.journeyService.getAllJourneys(pageNumber, pageSize, sortBy, order);


        return new ResponseEntity<List<BikeJourney>>(journeyList, new HttpHeaders(), HttpStatus.OK);


    }

    @GetMapping("/count")
    public ResponseEntity<Integer> journeyCount(){
        return new ResponseEntity<Integer>(this.journeyService.getJourneyCount(), new HttpHeaders(), HttpStatus.OK);

    }

    @PostMapping("/populate")
    public ResponseEntity<String> add(){




        if(this.populatingService.populateData()){
            return ResponseEntity.status(HttpStatus.OK).body("Data read from file to db succesfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reading data from file failed");


    }


}
