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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.jasha.demo.sbtfragments.config.ProjectProperties;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

@Configuration
@EnableConfigurationProperties(ProjectProperties.class)
public class CitiesInitializer implements InitializingBean {

  @Resource
  private CityDao cityDao;
  @Resource
  private ObjectMapper objectMapper;
  @Resource
  private ProjectProperties projectProperties;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.springframework.core.io.Resource resource = new ClassPathResource(projectProperties.getCitiesFile());

    List<City> cities;
    try (InputStream inputStream = resource.getInputStream()) {
      cities = objectMapper.readValue(inputStream, new TypeReference<List<City>>() {
      });
    }
    cities.forEach(cityDao::add);
  }
}