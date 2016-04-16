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

package eu.jasha;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CitiesInitializer implements InitializingBean {

    @Autowired
    private CityDao cityDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        cityDao.add(new City("1", "Utrecht", 48, 338_949));
        cityDao.add(new City("2", "Amsterdam", 1000, 834_119));
        cityDao.add(new City("3", "Rotterdam", 1260, 630_383));
        cityDao.add(new City("4", "New York", 1624, 9_576_321));
        cityDao.add(new City("5", "Nijmegen", -15, 172_063));
        cityDao.add(new City("6", "Rome", -753, 2_863_322));
    }
}