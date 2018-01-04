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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CityDaoTest {

  private static final String CITY_ID = "sim";
  private CityDao cityDao;
  private City simCity;

  @Before
  public void setup() {
    cityDao = new CityDao();
    simCity = new City(CITY_ID, "Sim City", 2016, 123_456);
    cityDao.add(simCity);
  }

  @Test
  public void should_return_empty_optional_for_unknown_id() {
    Optional<City> city = cityDao.find("unknown");

    assertThat(city).isNotPresent();
  }

  @Test
  public void should_add_city() {
    Optional<City> city = cityDao.find(CITY_ID);

    assertThat(city)
        .isPresent()
        .hasValue(simCity);
  }

  @Test
  public void should_update_city() {
    simCity.setFoundedIn(2015);
    cityDao.update(simCity);

    Optional<City> city = cityDao.find(CITY_ID);

    assertThat(city)
        .isPresent()
        .hasValueSatisfying(s -> assertThat(s.getFoundedIn()).isEqualTo(2015));
  }

  @Test
  public void should_remove_city() {
    cityDao.remove(CITY_ID);

    Optional<City> city = cityDao.find(CITY_ID);

    assertThat(city).isNotPresent();
  }

  @Test
  public void should_find_all_cities() {
    List<City> cities = cityDao.getAll();

    assertThat(cities)
        .hasSize(1)
        .containsExactly(simCity);
  }
}