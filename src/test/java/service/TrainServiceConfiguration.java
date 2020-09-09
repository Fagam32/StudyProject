package service;

import com.ivolodin.repositories.StationRepository;
import com.ivolodin.repositories.TrainEdgeRepository;
import com.ivolodin.repositories.TrainRepository;
import com.ivolodin.services.EdgeService;
import com.ivolodin.services.GraphService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TrainServiceConfiguration {
    @Bean
    @Primary
    public TrainRepository trainRepository() {
        return Mockito.mock(TrainRepository.class);
    }

    @Bean
    @Primary
    public StationRepository stationRepository() {
        return Mockito.mock(StationRepository.class);
    }

    @Bean
    @Primary
    public GraphService graphService() {
        return Mockito.mock(GraphService.class);
    }

    @Bean
    @Primary
    public EdgeService edgeService() {
        return Mockito.mock(EdgeService.class);
    }

    @Bean
    @Primary
    public TrainEdgeRepository trainEdgeRepository() {
        return Mockito.mock(TrainEdgeRepository.class);
    }
}
