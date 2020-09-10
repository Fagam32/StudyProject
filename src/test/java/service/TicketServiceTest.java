package service;

import com.ivolodin.TrainStationApp;
import com.ivolodin.dto.TicketDto;
import com.ivolodin.entities.Station;
import com.ivolodin.entities.Ticket;
import com.ivolodin.entities.Train;
import com.ivolodin.entities.User;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TicketRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.services.GraphService;
import com.ivolodin.services.TicketService;
import com.ivolodin.services.TrainService;
import com.ivolodin.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TrainStationApp.class)
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @MockBean
    private TrainService trainService;

    @MockBean
    private TrainRepository trainRepository;

    @MockBean
    private StationRepository stationRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private GraphService graphService;

    @MockBean
    private UserService userService;


    @Test
    public void buyTicket() {
        TicketDto validTicketDto = TestUtils.validTicketDto();
        User user = TestUtils.getValidUser();
        Train train = TestUtils.getValidTrainEntity();

        Ticket expected = new Ticket();
        expected.setUser(user);
        expected.setFromEdge(train.getPath().get(0));
        expected.setToEdge(train.getPath().get(2));
        expected.setTrain(train);

        when(trainRepository.findTrainByTrainName(any())).thenReturn(train);
        when(stationRepository.findByName(eq("First"))).thenReturn(new Station("First"));
        when(stationRepository.findByName(eq("Third"))).thenReturn(new Station("Third"));
        when(userService.getUserFromAuthentication(any())).thenReturn(user);
        when(trainService.hasAvailableSeatsOnPath(any(), any(), any())).thenReturn(true);
        when(ticketRepository.save(any())).then(invocation -> {
            Ticket ticket = invocation.getArgument(0);
            assertEquals(ticket.getUser(), expected.getUser());
            assertEquals(ticket.getFromEdge(), expected.getFromEdge());
            assertEquals(ticket.getTrain(), train);
            return null;
        });
        ticketService.buyTicket(validTicketDto, any());

    }


}
