package com.ivolodin.utils;

import com.ivolodin.model.dto.StationConnectDto;
import com.ivolodin.model.dto.TicketDto;
import com.ivolodin.model.dto.TrainDto;
import com.ivolodin.model.dto.UserInfoDto;
import com.ivolodin.model.entities.StationConnect;
import com.ivolodin.model.entities.Ticket;
import com.ivolodin.model.entities.Train;
import com.ivolodin.model.entities.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.addConverter(new StationConnectConverter());
        //Type map for train
        TypeMap<Train, TrainDto> trainToDto = modelMapper.createTypeMap(Train.class, TrainDto.class);
        trainToDto.addMappings(mapping -> {
            mapping.map(src -> src.getFromStation().getName(), TrainDto::setFromStation);
            mapping.map(src -> src.getToStation().getName(), TrainDto::setToStation);
        });
        //Type map for user
        TypeMap<User, UserInfoDto> userToDto = modelMapper.createTypeMap(User.class, UserInfoDto.class);
        userToDto.addMappings(mapping -> mapping.map(User::getUsername, UserInfoDto::setUserName));
        //Type map for ticket
        TypeMap<Ticket, TicketDto> ticketToDto = modelMapper.createTypeMap(Ticket.class, TicketDto.class);
        ticketToDto.addMappings(mapping -> {
            mapping.map(src -> src.getFromEdge().getStation().getName(), TicketDto::setFromStation);
            mapping.map(src -> src.getToEdge().getStation().getName(), TicketDto::setToStation);
            mapping.map(src -> src.getTrain().getTrainName(), TicketDto::setTrainName);
            mapping.map(src -> src.getFromEdge().getDeparture(), TicketDto::setDeparture);
            mapping.map(src -> src.getToEdge().getArrival(), TicketDto::setArrival);
            mapping.map(src -> src.getUser().getName(), TicketDto::setName);
            mapping.map(src -> src.getUser().getSurname(), TicketDto::setSurname);
            mapping.map(src -> src.getUser().getBirthdate(), TicketDto::setBirthdate);

        });

    }

    private MapperUtils() {
    }

    public static <S, D> D map(S source, Class<D> dest) {
        return modelMapper.map(source, dest);
    }

    public static <S, D> List<D> mapAll(Collection<S> sourceList, Class<D> dest) {
        return sourceList
                .stream()
                .map(item -> modelMapper.map(item, dest))
                .collect(Collectors.toList());
    }

    private static class StationConnectConverter implements Converter<StationConnect, StationConnectDto> {
        @Override
        public StationConnectDto convert(MappingContext<StationConnect, StationConnectDto> context) {
            StationConnectDto result = new StationConnectDto();
            StationConnect source = context.getSource();
            result.setFromStation(source.getFrom().getName());
            result.setToStation(source.getTo().getName());
            result.setDistance(source.getDistanceInMinutes());
            return result;
        }
    }
}
