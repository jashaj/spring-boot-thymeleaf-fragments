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
        City utrecht = new City("Utrecht", 48, 338_949);
        City amsterdam = new City("Amsterdam", 1000, 834_119);
        City rotterdam = new City("Rotterdam", 1260, 630_383);
        City nyc = new City("New York", 1624, 9_576_321);

        cityDao.add(utrecht);
        cityDao.add(amsterdam);
        cityDao.add(rotterdam);
        cityDao.add(nyc);
    }
}
