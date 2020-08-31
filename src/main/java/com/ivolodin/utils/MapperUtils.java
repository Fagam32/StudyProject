package com.ivolodin.utils;

import com.ivolodin.dto.StationConnectDto;
import com.ivolodin.dto.TrainDto;
import com.ivolodin.entities.StationConnect;
import com.ivolodin.entities.Train;
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
        modelMapper.addConverter(new StationConnectConverter());

        TypeMap<Train, TrainDto> trainToDto = modelMapper.createTypeMap(Train.class, TrainDto.class);
        trainToDto.addMappings(mapping -> {
            mapping.map(src -> src.getFromStation().getName(), TrainDto::setFromStation);
            mapping.map(src -> src.getToStation().getName(), TrainDto::setToStation);
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
