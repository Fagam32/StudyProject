package service;

import com.ivolodin.model.dto.TicketDto;
import com.ivolodin.model.entities.Station;
import com.ivolodin.model.entities.Ticket;
import com.ivolodin.model.entities.Train;
import com.ivolodin.model.entities.User;
import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TicketRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.services.GraphService;
import com.ivolodin.services.TicketService;
import com.ivolodin.services.TrainService;
import com.ivolodin.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TrainService trainService;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private GraphService graphService;

    @Mock
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
