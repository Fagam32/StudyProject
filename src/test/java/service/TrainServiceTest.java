package service;

import com.ivolodin.TrainStationApp;
import com.ivolodin.dto.TrainDto;
import com.ivolodin.dto.TrainEdgeDto;
import com.ivolodin.entities.Station;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TrainEdgeRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.services.EdgeService;
import com.ivolodin.services.GraphService;
import com.ivolodin.services.TrainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TrainStationApp.class)
public class TrainServiceTest {

    private static final String TRAIN_NAME = "Valid";

    private static final LocalDateTime DEPARTING_TIME = LocalDateTime.now();

    @Autowired
    private TrainService trainService;

    @MockBean
    private StationRepository stationRepository;

    @MockBean
    private TrainRepository trainRepository;

    @MockBean
    private GraphService graphService;

    @MockBean
    private EdgeService edgeService;

    @MockBean
    private TrainEdgeRepository trainEdgeRepository;

    @Test
    public void addValidTrain() {
        ArrayList<Station> validTrainPath = new ArrayList<>();

        Station first = new Station("First");
        Station second = new Station("Second");
        Station third = new Station("Third");

        validTrainPath.add(first);
        validTrainPath.add(second);
        validTrainPath.add(third);
        TrainDto validTrain = getValidTrainWithoutPath();

        when(stationRepository.findByName("First")).thenReturn(first);
        when(stationRepository.findByName("Third")).thenReturn(third);
        when(trainRepository.findTrainByTrainName(TRAIN_NAME)).thenReturn(null);
        when(graphService.getPathList(first, third)).thenReturn(validTrainPath);
        when(edgeService.getDistanceBetweenStations(first, second)).thenReturn(10);
        when(edgeService.getDistanceBetweenStations(second, third)).thenReturn(10);
        when(trainRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TrainDto inputTrain = new TrainDto();
        inputTrain.setTrainName(TRAIN_NAME);
        inputTrain.setSeatsNumber(15);
        inputTrain.setDeparture(DEPARTING_TIME.plusHours(1));
        inputTrain.setFromStation("First");
        inputTrain.setToStation("Third");

        TrainDto testingTrain = trainService.addNewTrain(inputTrain);

        assertEquals(testingTrain, validTrain);
    }

    @Test
    public void addInvalidTrain_wrongTime(){

    }

    private TrainDto getValidTrainWithoutPath() {
        return new TrainDto(TRAIN_NAME,
                15,
                "First",
                "Third",
                DEPARTING_TIME.plusHours(1),
                DEPARTING_TIME.plusHours(1).plusMinutes(10).plusMinutes(10),
                getValidTrainPathDto());
    }

    private List<TrainEdgeDto> getValidTrainPathDto() {
        TrainEdgeDto first = new TrainEdgeDto(
                TRAIN_NAME,
                1,
                "First",
                15,
                null,
                0,
                DEPARTING_TIME.plusHours(1)
        );
        TrainEdgeDto second = new TrainEdgeDto(
                TRAIN_NAME,
                2,
                "Second",
                15,
                DEPARTING_TIME.plusHours(1).plusMinutes(10),
                0,
                DEPARTING_TIME.plusHours(1).plusMinutes(10)
        );
        TrainEdgeDto third = new TrainEdgeDto(
                TRAIN_NAME,
                3,
                "Third",
                15,
                DEPARTING_TIME.plusHours(1).plusMinutes(20),
                0,
                null
        );
        return Arrays.asList(first, second, third);
    }

}
