package net.lanet.vollmed.infra.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConvertsDataUtil {
    private static final ModelMapper modelMapper = new ModelMapper();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getDataJsonToClass(String json, Class<T> modelTarget) {
        try {
            return objectMapper.readValue(json, modelTarget);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> getListJsonToClass(String json, Class<T> modelTarget) {
        CollectionType listModel = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, modelTarget);
        try {
            return objectMapper.readValue(json, listModel);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String setDataClassToJson(Class<T> modelSource) {
        try {
            String json = objectMapper.writeValueAsString(modelSource);
            return json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <S, T> T mapDataClassToClass(S modelSource, Class<T> modelTarget) {
        try {
            return modelMapper.map(modelSource, modelTarget);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <S, T> List<T> mapListClassToClass(List<S> modelSource, Class<T> modelTarget) {
        try {
            return modelSource.stream()
                    .map(e -> modelMapper.map(e, modelTarget))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<Map<String, Object>> convertToListOfMaps(List<T> modelSource) {
        List<Map<String, Object>> listOfMaps = new ArrayList<>();

        for (T model : modelSource) {
            Map<String, Object> map = new LinkedHashMap<>();

            Field[] fields = model.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    map.put(field.getName(), field.get(model));
                } catch (IllegalAccessException ignored) {}
            }
            listOfMaps.add(map);
        }

        return listOfMaps;
    }
}
