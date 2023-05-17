package solita.citybike;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import solita.citybike.controllers.JourneyController;
import solita.citybike.models.BikeJourney;
import solita.citybike.models.BikeStation;
import solita.citybike.repositories.JourneyRepository;
import solita.citybike.services.JourneyService;

import javax.print.attribute.standard.Media;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JourneyController.class)
@ContextConfiguration( classes = {JourneyController.class, JourneyService.class, JourneyRepository.class})
public class JourneyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    JourneyRepository repository;

    BikeStation STATION_1 = new BikeStation(1, "DepartureStation");
    BikeStation STATION_2 = new BikeStation(2, "ReturnStation");


    BikeJourney JOURNEY_1 = new BikeJourney(LocalDateTime.parse("2021-05-31T23:57:25"), LocalDateTime.parse("2021-05-31T23:57:25"), 15, 60);
    BikeJourney JOURNEY_2 = new BikeJourney(LocalDateTime.parse("2021-05-31T23:57:25"), LocalDateTime.parse("2021-05-31T23:57:25"), 45, 60);
    BikeJourney JOURNEY_3 = new BikeJourney(LocalDateTime.parse("2021-05-31T23:57:25"), LocalDateTime.parse("2021-05-31T23:57:25"), 50, 60);


    @Test
    public void testGetAllJourneys() throws Exception {

        JOURNEY_1.setDepartureStation(STATION_1);
        JOURNEY_1.setReturnStation(STATION_2);
        JOURNEY_2.setDepartureStation(STATION_1);
        JOURNEY_2.setReturnStation(STATION_2);
        JOURNEY_3.setDepartureStation(STATION_1);
        JOURNEY_3.setReturnStation(STATION_2);

        List<BikeJourney> journeys = new ArrayList<>(Arrays.asList(JOURNEY_1, JOURNEY_2, JOURNEY_3));

        Mockito.when(repository.findAll()).thenReturn(journeys);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].duration", is(60)));
    }

}
