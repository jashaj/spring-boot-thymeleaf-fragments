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

import static eu.jasha.demo.sbtfragments.CityController.FRAGMENT_FORM;
import static eu.jasha.demo.sbtfragments.CityController.MODEL_ATTRIBUTE_CITIES;
import static eu.jasha.demo.sbtfragments.CityController.MODEL_ATTRIBUTE_CITY;
import static eu.jasha.demo.sbtfragments.CityController.SECTION_CITIES;
import static eu.jasha.demo.sbtfragments.CityController.VIEW_CITIES;
import static eu.jasha.demo.sbtfragments.CityController.VIEW_CITY_DELETE;
import static eu.jasha.demo.sbtfragments.CityController.VIEW_CITY_FORM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.RedirectView;

@RunWith(MockitoJUnitRunner.class)
public class CityControllerTest {

  private static final String CITY_ID = "sim";
  @Mock
  private CityDao cityDao;
  @InjectMocks
  private CityController controller;

  private ModelMap modelMap;
  private City city;

  @Before
  public void setup() {
    modelMap = new ModelMap();
    city = new City(CITY_ID, "Sim City", 2016, 123_456);
  }

  @Test
  public void should_show_overview() {
    List<City> cityList = new ArrayList<>();
    when(cityDao.getAll()).thenReturn(cityList);

    String view = controller.overview(modelMap);

    assertThat(view).isEqualTo(VIEW_CITIES);
    assertThat(modelMap.get(MODEL_ATTRIBUTE_CITIES)).isEqualTo(cityList);
  }

  @Test
  public void should_show_city_form_page() {
    mockFindCity();

    String view = controller.showUpdateCityPage(CITY_ID, modelMap);

    assertThat(view).isEqualTo(VIEW_CITY_FORM);
    assertThat(modelMap.get(MODEL_ATTRIBUTE_CITY)).isEqualTo(city);
  }

  @Test
  public void should_show_city_form_fragment() {
    mockFindCity();

    String view = controller.showUpdateCityForm(CITY_ID, modelMap);

    assertThat(view).isEqualTo(VIEW_CITY_FORM + FRAGMENT_FORM);
    assertThat(modelMap.get(MODEL_ATTRIBUTE_CITY)).isEqualTo(city);
  }

  @Test
  public void should_update_city() {
    RedirectView view = controller.updateCity(CITY_ID, city);

    assertThat(view.isRedirectView()).isTrue();
    assertThat(view.getUrl()).isEqualTo("");

    verify(cityDao).update(city);
  }

  @Test
  public void should_update_city_name() {
    mockFindCity();

    controller.partialUpdateCity(CITY_ID, "name", "Almere");

    assertThat(city.getName()).isEqualTo("Almere");

    verify(cityDao).update(city);
  }

  @Test
  public void should_update_city_population() {
    mockFindCity();

    controller.partialUpdateCity(CITY_ID, "population", "987654");

    assertThat(city.getPopulation()).isEqualTo(987654);

    verify(cityDao).update(city);
  }

  @Test
  public void should_update_city_founded_in() {
    mockFindCity();

    controller.partialUpdateCity(CITY_ID, "foundedIn", "2000");

    assertThat(city.getFoundedIn()).isEqualTo(2000);

    verify(cityDao).update(city);
  }

  @Test
  public void should_not_update_city_if_parameter_is_unknown() {
    mockFindCity();

    controller.partialUpdateCity(CITY_ID, "unsupported", "My value");

    verify(cityDao, never()).update(city);
  }

  @Test
  public void should_show_delete_city_page() {
    mockFindCity();

    String view = controller.showDeleteCityPage(CITY_ID, modelMap);

    assertThat(view).isEqualTo(VIEW_CITY_DELETE);
    assertThat(modelMap.get(MODEL_ATTRIBUTE_CITY)).isEqualTo(city);
  }

  @Test
  public void should_show_delete_city_form_fragment() {
    mockFindCity();

    String view = controller.showDeleteCityForm(CITY_ID, modelMap);

    assertThat(view).isEqualTo(VIEW_CITY_DELETE + FRAGMENT_FORM);
    assertThat(modelMap.get(MODEL_ATTRIBUTE_CITY)).isEqualTo(city);
  }

  @Test
  public void should_delete_city() {
    RedirectView view = controller.deleteCity(CITY_ID);

    assertThat(view.isRedirectView()).isTrue();
    assertThat(view.getUrl()).isEqualTo("/cities");

    verify(cityDao).remove(CITY_ID);
  }

  @Test
  public void should_set_section_to_modelMap() {
    String section = controller.section();

    assertThat(section).isEqualTo(SECTION_CITIES);
  }

  private void mockFindCity() {
    when(cityDao.find(CITY_ID)).thenReturn(Optional.of(city));
  }
}