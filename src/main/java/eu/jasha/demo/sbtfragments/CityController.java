/*
 *   Copyright 2016 Jasha Joachimsthal
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Controller
@RequestMapping("cities")
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CityController {

    private static final Logger LOG = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private CityDao cityDao;

    @RequestMapping
    public String overview(ModelMap modelMap) {
        modelMap.addAttribute("cities", cityDao.getAll());
        return "pages/cities";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String cityPage(@PathVariable("id") String id, ModelMap modelMap) {
        Optional<City> city = cityDao.find(id);
        modelMap.addAttribute("city", city.get());
        return "pages/city-form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = {"X-Requested-With=XMLHttpRequest"})
    public String cityFragment(@PathVariable("id") String id, ModelMap modelMap) {
        LOG.info("Requesting city {} via XHR", id);

        // Let Thymeleaf only return the th:fragment="form" within the view
        return cityPage(id, modelMap) + " :: form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public RedirectView city(@PathVariable("id") String id,
                             @ModelAttribute("city") City city) {
        LOG.info("Updating city {}", id);

        cityDao.update(city);
        return new RedirectView("");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST,
            headers = {"X-Requested-With=XMLHttpRequest"}, params = {"pk=1"})
    @ResponseStatus(code = NO_CONTENT)
    public void partialUpdateCity(@PathVariable("id") String id,
                                  @RequestParam("name") String parameterName,
                                  @RequestParam String value) {
        City city = cityDao.find(id).get();
        if ("name".equalsIgnoreCase(parameterName)) {
            city.setName(value);
        } else if ("population".equalsIgnoreCase(parameterName)) {
            city.setPopulation(Integer.parseInt(value));
        } else if ("foundedIn".equalsIgnoreCase(parameterName)) {
            city.setFoundedIn(Integer.parseInt(value));
        }
        cityDao.update(city);
    }


    @ModelAttribute("section")
    public String section() {
        return "cities";
    }

}