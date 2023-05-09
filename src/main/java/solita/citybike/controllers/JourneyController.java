package solita.citybike.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solita.citybike.models.BikeJourney;
import solita.citybike.services.JourneyService;

import java.util.List;

@RestController
@RequestMapping("journeys")
public class JourneyController {

    @Autowired
    private JourneyService service;


    @GetMapping
    public ResponseEntity<List<BikeJourney>> getAllJourneys(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        List<BikeJourney> journeyList = this.service.getAllJourneys(pageNumber, pageSize, sortBy);


        return new ResponseEntity<List<BikeJourney>>(journeyList, new HttpHeaders(), HttpStatus.OK);


    }

    @PostMapping("/populate")
    public ResponseEntity<String> add(){




        if(this.service.populateData()){
            return ResponseEntity.status(HttpStatus.OK).body("Data read from file to db succesfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reading data from file failed");


    }


}
