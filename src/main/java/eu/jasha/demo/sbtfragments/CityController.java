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

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.function.Supplier;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("cities")
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CityController {

  private static final Logger LOG = LoggerFactory.getLogger(CityController.class);

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

  @RequestMapping
  public String overview(ModelMap modelMap) {
    modelMap.addAttribute(MODEL_ATTRIBUTE_CITIES, cityDao.getAll());
    return VIEW_CITIES;
  }

  @RequestMapping(value = PATH_ID, method = GET)
  public String showUpdateCityPage(@PathVariable(ID) String id,
                                   ModelMap modelMap) {
    City city = cityDao.find(id).orElseThrow(notFoundException());
    modelMap.addAttribute(MODEL_ATTRIBUTE_CITY, city);
    return VIEW_CITY_FORM;
  }

  @RequestMapping(value = PATH_ID, method = GET, headers = { X_REQUESTED_WITH_XML_HTTP_REQUEST })
  public String showUpdateCityForm(@PathVariable(ID) String id,
                                   ModelMap modelMap) {
    LOG.info("Requesting city {} via XHR", id);

    // Let Thymeleaf only return the th:fragment="form" within the view
    return showUpdateCityPage(id, modelMap) + FRAGMENT_FORM;
  }

  @RequestMapping(value = PATH_ID, method = POST)
  public RedirectView updateCity(@PathVariable(ID) String id,
                                 @ModelAttribute("city") City city) {
    LOG.info("Updating city {}", id);

    cityDao.update(city);
    return new RedirectView("");
  }

  @RequestMapping(method = POST, headers = { X_REQUESTED_WITH_XML_HTTP_REQUEST }, params = { "pk" })
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
      LOG.warn("Invalid request for updating a city. Parameter name '{}', value '{}'", parameterName, value);
      return;
    }
    cityDao.update(city);
  }

  @RequestMapping(value = PATH_ID + "/delete", method = GET)
  public String showDeleteCityPage(@PathVariable(ID) String id,
                                   ModelMap modelMap) {
    City city = cityDao.find(id).orElseThrow(notFoundException());
    modelMap.addAttribute(MODEL_ATTRIBUTE_CITY, city);

    return VIEW_CITY_DELETE;
  }

  @RequestMapping(value = PATH_ID + "/delete", method = GET, headers = { X_REQUESTED_WITH_XML_HTTP_REQUEST })
  public String showDeleteCityForm(@PathVariable(ID) String id,
                                   ModelMap modelMap) {
    LOG.info("Requesting delete city form {} via XHR", id);

    return showDeleteCityPage(id, modelMap) + FRAGMENT_FORM;
  }

  @RequestMapping(value = PATH_ID + "/delete", method = POST)
  public RedirectView deleteCity(@PathVariable(ID) String id) {
    LOG.info("Deleting city {}", id);

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