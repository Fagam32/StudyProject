package utils;

import com.ivolodin.model.dto.TicketDto;
import com.ivolodin.model.dto.TrainDto;
import com.ivolodin.model.dto.TrainEdgeDto;
import com.ivolodin.model.entities.Station;
import com.ivolodin.model.entities.Train;
import com.ivolodin.model.entities.TrainEdge;
import com.ivolodin.model.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TestUtils {
    public static final String TRAIN_NAME = "Valid";
    public static final LocalDateTime DEPARTING_TIME = LocalDateTime.now().plusHours(1);

    public static Train getValidTrainEntity() {
        Station first = new Station("First");
        Station second = new Station("Second");
        Station third = new Station("Third");
        Train train = new Train();
        train.setTrainName(TRAIN_NAME);
        train.setDeparture(DEPARTING_TIME);
        train.setArrival(DEPARTING_TIME.plusMinutes(20));
        train.setFromStation(first);
        train.setToStation(third);
        train.setSeatsNumber(15);

        ArrayList<TrainEdge> path = new ArrayList<>();
        path.add(new TrainEdge(1, train, first, 15, null, 0, DEPARTING_TIME, 1));
        path.add(new TrainEdge(2, train, second, 15, DEPARTING_TIME.plusMinutes(10), 0, DEPARTING_TIME.plusMinutes(10), 2));
        path.add(new TrainEdge(3, train, third, 15, DEPARTING_TIME.plusMinutes(20), 0, null, 3));

        train.setPath(path);

        train.setTickets(new HashSet<>());
        return train;
    }

    public static TrainDto getValidTrainDtoWithoutPath() {
        return new TrainDto(TRAIN_NAME,
                15,
                "First",
                "Third",
                DEPARTING_TIME,
                DEPARTING_TIME.plusMinutes(10).plusMinutes(10),
                getValidPathDto());
    }

    public static List<TrainEdgeDto> getValidPathDto() {
        TrainEdgeDto first = new TrainEdgeDto(
                TRAIN_NAME,
                1,
                "First",
                15,
                null,
                0,
                DEPARTING_TIME
        );
        TrainEdgeDto second = new TrainEdgeDto(
                TRAIN_NAME,
                2,
                "Second",
                15,
                DEPARTING_TIME.plusMinutes(10),
                0,
                DEPARTING_TIME.plusMinutes(10)
        );
        TrainEdgeDto third = new TrainEdgeDto(
                TRAIN_NAME,
                3,
                "Third",
                15,
                DEPARTING_TIME.plusMinutes(20),
                0,
                null
        );
        return Arrays.asList(first, second, third);
    }

    public static TicketDto validTicketDto() {
        TicketDto ticket = new TicketDto();
        ticket.setFromStation("First");
        ticket.setToStation("Third");
        ticket.setTrainName("Valid");
        return ticket;
    }

    public static User getValidUser() {
        User user = new User();
        user.setName("Test");
        user.setSurname("Test");
        user.setEmail("test@test.test");
        user.setBirthdate(LocalDate.of(2000, 1, 1));
        return user;
    }
}
