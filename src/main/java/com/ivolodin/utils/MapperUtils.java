package com.ivolodin.utils;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

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
}
