/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pt.isel.mpd;

import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd.util.EagerQueries;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.WeatherInfo;
import pt.isel.mpd.weather.WeatherWebApi;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static pt.isel.mpd.util.EagerQueries.filter;
import static pt.isel.mpd.util.EagerQueries.map;
import static pt.isel.mpd.util.EagerQueries.max;

public class EagerQueriesTest {
    final Iterable<WeatherInfo> jan;

    public EagerQueriesTest() {
        WeatherWebApi api = new WeatherWebApi(new MockRequest());
        jan = api.pastWeather(37.017, -7.933, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 30));
    }

    @Test public void testMaxTemperatureOnSunnyDays() {
        int maxTemp = max(map(filter(jan, wi -> wi.getDesc().contains("Sun")), WeatherInfo::getTempC));
        assertEquals(15, maxTemp);
    }

    @Test public void testFilterCloudyDays() {
        Iterable<WeatherInfo> cloud = filter(jan, EagerQueriesTest::cloudyDays);
        assertEquals(14, EagerQueries.count(cloud));
    }

    static boolean cloudyDays(WeatherInfo info) {
        return info.desc.toLowerCase().contains("cloud");
    }

    @Test public void testFilterWithRainnyDaysPredicate() {
        Iterable<WeatherInfo> rainny = filter(jan, new RainnyDays());
        assertEquals(11,  EagerQueries.count(rainny));
    }

    @Test public void testFilterWithWarmDaysPredicate() {
        Iterable<WeatherInfo> warm = filter(jan, new WarmDays());
        assertEquals(27,  EagerQueries.count(warm));
    }

    private class RainnyDays implements Predicate<WeatherInfo> {
        @Override
        public boolean test(WeatherInfo info) {
            return info.precipMM != 0;
        }
    }

    private class WarmDays implements Predicate<WeatherInfo> {
        @Override
        public boolean test(WeatherInfo info) {
            return info.tempC >= 14;
        }
    }
}