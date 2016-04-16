package eu.jasha;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CityDao {

    private static Set<City> cities = new HashSet<>();

    public Optional<City> find(String id) {
        return cities
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public void add(City city) {
        cities.add(city);
    }

    public void update(City city) {
        remove(city.getId());
        add(city);
    }


    public void remove(String id) {
        if(StringUtils.isNotBlank(id)) {
            cities.removeIf(c -> c.getId().equals(id));
        }
    }

    public List<City> getAll() {
        ArrayList<City> cities = new ArrayList<>(CityDao.cities);
        cities.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return cities;
    }
}
