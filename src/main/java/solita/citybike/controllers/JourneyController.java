package solita.citybike.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import solita.citybike.services.JourneyService;

@RestController
public class JourneyController {

    @Autowired
    private JourneyService service;

    @PostMapping("/journeys/populate")
    public ResponseEntity<String> add(){

        this.service.populateData();


        return ResponseEntity.status(HttpStatus.OK).body("Data read from file to db succesfully");

    }


}
