package ru.olgak.folks.soap;

import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import ru.hflabs.util.core.date.DateUtil;
import ru.hflabs.util.spring.util.ReflectionUtil;
import ru.olgak.folks.api.Device;
import ru.olgak.folks.api.Folk;
import ru.olgak.folks.api.search.EntityQuery;
import ru.olgak.folks.api.search.SearchService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nikolay Golubev
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class FolksWSImpl implements FolksWS {

    private final SearchService<Folk> searchService;

    @POST
    @Path("/search")
    @Override
    public SearchResponse search(Search parameters) {
        EntityQuery entityQuery = new EntityQuery();
        entityQuery.setSearch(parameters.getQuery());
        List<Folk> entities = searchService.findEntities(entityQuery);

        SearchResponse searchResponse = new SearchResponse();
        searchResponse.getFolk().addAll(entities.stream().map(this::convertFolk).collect(Collectors.toList()));
        return searchResponse;
    }

    private SearchResponse.Folk convertFolk(Folk folk) {
        SearchResponse.Folk wsFolk = new SearchResponse.Folk();
        wsFolk.setId(folk.getId());

        for (Field field : ReflectionUtil.getAllDeclaredFields(Folk.class)) {
            if (!Collection.class.isAssignableFrom(field.getType()) && !"id".equals(field.getName())) {
                try {
                    Object value = ReflectionUtil.get(field, folk);
                    wsFolk.getField().add(createWField(field.getName(), getStringValue(value)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (!CollectionUtils.isEmpty(folk.getDevices())) {
            wsFolk.getAttribute().addAll(folk.getDevices().stream().map(this::convertDevice).collect(Collectors.toList()));
        }

        return wsFolk;
    }

    private WAttribute convertDevice(Device device) {
        WAttribute attribute = new WAttribute();
        attribute.setType("DEVICE");
        attribute.setId(device.getId());

        for (Field field : ReflectionUtil.getAllDeclaredFields(Device.class)) {
            if (!"folk".equals(field.getName()) && !"id".equals(field.getName())) {
                try {
                    Object value = ReflectionUtil.get(field, device);
                    attribute.getField().add(createWField(field.getName(), getStringValue(value)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return attribute;
    }

    private String getStringValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Enum) {
            return ((Enum) value).name();
        } else if (value instanceof Date) {
            return DateUtil.formatDate((Date) value);
        } else {
            return value.toString();
        }
    }

    private WField createWField(String name, String value) {
        WField wField = new WField();
        wField.setName(name);
        wField.setValue(value);
        return wField;
    }
}
