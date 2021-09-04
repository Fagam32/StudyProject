package service;

import com.ivolodin.TrainStationApp;
import com.ivolodin.model.dto.TrainDto;
import com.ivolodin.model.entities.Station;
import com.ivolodin.model.entities.Train;
import com.ivolodin.model.entities.TrainEdge;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TrainEdgeRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.services.EdgeService;
import com.ivolodin.services.GraphService;
import com.ivolodin.services.TrainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import utils.TestUtils;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static utils.TestUtils.*;

@ExtendWith(MockitoExtension.class)
public class TrainServiceTest {

    private static final String TRAIN_NAME = TestUtils.TRAIN_NAME;

    private static final LocalDateTime DEPARTING_TIME = TestUtils.DEPARTING_TIME;

    @InjectMocks
    private TrainService trainService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private GraphService graphService;

    @Mock
    private EdgeService edgeService;

    @Mock
    private TrainEdgeRepository trainEdgeRepository;

    @Mock
    private AmqpTemplate template;

    @Test
    public void addNewTrain() {
        ArrayList<Station> validTrainPath = new ArrayList<>();

        Station first = new Station("First");
        Station second = new Station("Second");
        Station third = new Station("Third");

        validTrainPath.add(first);
        validTrainPath.add(second);
        validTrainPath.add(third);
        TrainDto validTrain = getValidTrainDtoWithoutPath();

        when(stationRepository.findByName(eq("First"))).thenReturn(first);
        when(stationRepository.findByName(eq("Third"))).thenReturn(third);
        when(trainRepository.findTrainByTrainName(eq(TRAIN_NAME))).thenReturn(null);
        when(graphService.getPathList(first, third)).thenReturn(validTrainPath);
        when(edgeService.getDistanceBetweenStations(first, second)).thenReturn(10);
        when(edgeService.getDistanceBetweenStations(second, third)).thenReturn(10);
        when(trainRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(template).convertAndSend(anyString(), anyString());

        TrainDto inputTrain = new TrainDto();
        inputTrain.setTrainName(TRAIN_NAME);
        inputTrain.setSeatsNumber(15);
        inputTrain.setDeparture(DEPARTING_TIME);
        inputTrain.setFromStation("First");
        inputTrain.setToStation("Third");

        TrainDto testingTrain = trainService.addNewTrain(inputTrain);

        assertEquals(testingTrain, validTrain);

        TrainDto actual = new TrainDto();
        actual.setFromStation("First");
        assertThrows(EntityNotFoundException.class, () -> trainService.addNewTrain(actual));

        actual.setToStation("First");
        assertThrows(IllegalArgumentException.class, () -> trainService.addNewTrain(actual));

        actual.setToStation("Third");
        actual.setDeparture(DEPARTING_TIME.minusHours(2));
        assertThrows(DateTimeException.class, () -> trainService.addNewTrain(actual));

        actual.setDeparture(DEPARTING_TIME);
        assertThrows(IllegalArgumentException.class, () -> trainService.addNewTrain(actual));
    }

    @Test
    public void updateSeatsForTrain() {
        Train train = getValidTrainEntity();
        when(trainEdgeRepository.getTrainEdgeByStationNameAndTrain(eq("First"), any())).thenReturn(train.getPath().get(0));
        when(trainEdgeRepository.getTrainEdgeByStationNameAndTrain(eq("Third"), any())).thenReturn(train.getPath().get(2));
        trainService.updateSeatsForTrain(train, "First", "Third", -1);
        TrainEdge first = train.getPath().get(0);
        TrainEdge second = train.getPath().get(1);
        TrainEdge third = train.getPath().get(2);

        assertEquals(14, first.getSeatsLeft());
        assertEquals(14, second.getSeatsLeft());
        assertEquals(15, third.getSeatsLeft());

        trainService.updateSeatsForTrain(train, "First", "Third", 2);

        assertEquals(16, first.getSeatsLeft());
        assertEquals(16, second.getSeatsLeft());
        assertEquals(15, third.getSeatsLeft());


    }

    @Test
    public void hasAvailableSeatsOnPath() {
        Train train = getValidTrainEntity();
        Boolean answer = trainService.hasAvailableSeatsOnPath(train, train.getPath().get(0), train.getPath().get(2));

        assertTrue(answer);

        train.getPath().get(0).setSeatsLeft(0);
        answer = trainService.hasAvailableSeatsOnPath(train, train.getPath().get(0), train.getPath().get(2));

        assertFalse(answer);
    }

    @Test
    public void getTrainInfo() {
        TrainDto trainDto = new TrainDto();
        trainDto.setTrainName(TRAIN_NAME);
        Train trainEntity = getValidTrainEntity();
        when(trainRepository.findTrainByTrainName(TRAIN_NAME)).thenReturn(trainEntity);

        TrainDto actual = trainService.getTrainInfo(trainDto);
        TrainDto expected = getValidTrainDtoWithoutPath();

        expected.setPath(getValidPathDto());

        assertEquals(actual, expected);
    }

    @Test
    public void refreshTrainTimes() {
        Station first = new Station("First");
        Station second = new Station("Second");
        Station third = new Station("Third");

        when(edgeService.getDistanceBetweenStations(first, second)).thenReturn(10);
        when(edgeService.getDistanceBetweenStations(second, third)).thenReturn(10);

        Train actual = getValidTrainEntity();
        actual.getPath().get(0).setStandingMinutes(3);
        actual.getPath().get(1).setStandingMinutes(3);
        actual.getPath().get(2).setStandingMinutes(3);

        Train expected = getValidTrainEntity();
        expected.setArrival(DEPARTING_TIME.plusMinutes(23));
        expected.getPath().get(1).setArrival(DEPARTING_TIME.plusMinutes(10));
        expected.getPath().get(1).setStandingMinutes(3);
        expected.getPath().get(1).setDeparture(DEPARTING_TIME.plusMinutes(13));
        expected.getPath().get(2).setArrival(DEPARTING_TIME.plusMinutes(23));
        expected.getPath().get(2).setStandingMinutes(3);

        trainService.refreshTrainTimes(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void getTrainsDepartingFromStation() {

        when(stationRepository.findByName(eq("First"))).thenReturn(new Station());
        Train validTrainEntity = getValidTrainEntity();
        when(trainRepository
                .findTrainsDepartingFromStation(
                        eq("First"),
                        eq(Date.valueOf(DEPARTING_TIME.toLocalDate())
                        )
                )
        )
                .thenReturn(Collections.singletonList(validTrainEntity));

        List<TrainDto> actual = trainService.getTrainsDepartingFromStation("First", DEPARTING_TIME.toLocalDate());
        TrainDto train = getValidTrainDtoWithoutPath();
        train.setPath(getValidPathDto());
        List<TrainDto> expected = Collections.singletonList(train);

        assertEquals(expected, actual);

        LocalDate wrongDate = DEPARTING_TIME.minusDays(1).toLocalDate();
        assertThrows(IllegalArgumentException.class, () -> trainService.getTrainsDepartingFromStation
                ("Second", wrongDate));

        when(stationRepository.findByName(anyString())).thenReturn(null);

        LocalDate date = DEPARTING_TIME.toLocalDate();
        String stationName = anyString();
        assertThrows(EntityNotFoundException.class, () -> trainService.getTrainsDepartingFromStation(stationName, date));
    }


}
