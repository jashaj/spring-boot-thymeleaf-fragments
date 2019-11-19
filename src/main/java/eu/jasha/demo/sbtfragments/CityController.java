/*
 *   Copyright 2019 Jasha Joachimsthal
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package eu.jasha.demo.sbtfragments;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.function.Supplier;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("cities")
public class CityController {

  static final String VIEW_CITIES = "pages/cities";
  static final String VIEW_CITY_FORM = "pages/city-form";
  static final String VIEW_CITY_DELETE = "pages/city-delete";
  static final String MODEL_ATTRIBUTE_CITIES = "cities";
  static final String MODEL_ATTRIBUTE_CITY = "city";
  static final String FRAGMENT_FORM = " :: form";
  static final String SECTION_CITIES = "cities";
  private static final String ID = "id";
  private static final String PATH_ID = "/{id}";
  private static final String X_REQUESTED_WITH_XML_HTTP_REQUEST = "X-Requested-With=XMLHttpRequest";

  @Resource
  private CityDao cityDao;

  @GetMapping
  public String overview(ModelMap modelMap) {
    modelMap.addAttribute(MODEL_ATTRIBUTE_CITIES, cityDao.getAll());
    return VIEW_CITIES;
  }

  @GetMapping(value = PATH_ID)
  public String showUpdateCityPage(@PathVariable(ID) String id,
                                   ModelMap modelMap) {
    City city = cityDao.find(id).orElseThrow(notFoundException());
    modelMap.addAttribute(MODEL_ATTRIBUTE_CITY, city);
    return VIEW_CITY_FORM;
  }

  @GetMapping(value = PATH_ID, headers = { X_REQUESTED_WITH_XML_HTTP_REQUEST })
  public String showUpdateCityForm(@PathVariable(ID) String id,
                                   ModelMap modelMap) {
    log.info("Requesting city {} via XHR", id);

    // Let Thymeleaf only return the th:fragment="form" within the view
    return showUpdateCityPage(id, modelMap) + FRAGMENT_FORM;
  }

  @PostMapping(value = PATH_ID)
  public RedirectView updateCity(@PathVariable(ID) String id,
                                 @ModelAttribute("city") City city) {
    log.info("Updating city {}", id);

    cityDao.update(city);
    return new RedirectView("");
  }

  @PostMapping(headers = { X_REQUESTED_WITH_XML_HTTP_REQUEST }, params = { "pk" })
  @ResponseStatus(code = NO_CONTENT)
  public void partialUpdateCity(@RequestParam("pk") String id,
                                @RequestParam("name") String parameterName,
                                @RequestParam("value") String value) {
    City city = cityDao.find(id).orElseThrow(notFoundException());
    if ("name".equalsIgnoreCase(parameterName)) {
      city.setName(value);
    } else if ("population".equalsIgnoreCase(parameterName)) {
      city.setPopulation(Integer.parseInt(value));
    } else if ("foundedIn".equalsIgnoreCase(parameterName)) {
      city.setFoundedIn(Integer.parseInt(value));
    } else {
      log.warn("Invalid request for updating a city. Parameter name '{}', value '{}'", parameterName, value);
      return;
    }
    cityDao.update(city);
  }

  @GetMapping(value = PATH_ID + "/delete")
  public String showDeleteCityPage(@PathVariable(ID) String id,
                                   ModelMap modelMap) {
    City city = cityDao.find(id).orElseThrow(notFoundException());
    modelMap.addAttribute(MODEL_ATTRIBUTE_CITY, city);

    return VIEW_CITY_DELETE;
  }

  @GetMapping(value = PATH_ID + "/delete", headers = { X_REQUESTED_WITH_XML_HTTP_REQUEST })
  public String showDeleteCityForm(@PathVariable(ID) String id,
                                   ModelMap modelMap) {
    log.info("Requesting delete city form {} via XHR", id);

    return showDeleteCityPage(id, modelMap) + FRAGMENT_FORM;
  }

  @PostMapping(value = PATH_ID + "/delete")
  public RedirectView deleteCity(@PathVariable(ID) String id) {
    log.info("Deleting city {}", id);

    cityDao.remove(id);
    return new RedirectView("/cities");
  }

  @ModelAttribute("section")
  public String section() {
    return SECTION_CITIES;
  }

  private Supplier<HttpClientErrorException> notFoundException() {
    return () -> new HttpClientErrorException(HttpStatus.NOT_FOUND);
  }
}