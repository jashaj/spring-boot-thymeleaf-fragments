/*
 *   Copyright 2018 Jasha Joachimsthal
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.jasha.demo.sbtfragments;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CityDao {

  private final Set<City> cities = new HashSet<>();

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
    if (StringUtils.isNotBlank(id)) {
      cities.removeIf(c -> c.getId().equals(id));
    }
  }

  public List<City> getAll() {
    List<City> cityList = new ArrayList<>(cities);
    cityList.sort(Comparator.comparing(City::getName));
    return cityList;
  }
}