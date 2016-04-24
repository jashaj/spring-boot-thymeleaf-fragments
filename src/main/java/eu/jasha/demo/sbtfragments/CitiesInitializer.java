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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

@Configuration
public class CitiesInitializer implements InitializingBean {

    @Autowired
    private CityDao cityDao;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${sbtfragments.citiesFile}")
    private String citiesFile;

    @Override
    public void afterPropertiesSet() throws Exception {
        Resource resource = new ClassPathResource(citiesFile);
        List<City> cities;
        try (InputStream inputStream = resource.getInputStream()) {
            cities = objectMapper.readValue(inputStream, new TypeReference<List<City>>() {
            });
        }
        for (City city : cities) {
            cityDao.add(city);
        }
    }
}