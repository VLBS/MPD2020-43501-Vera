package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.LazyQueries;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.WeatherInfo;
import pt.isel.mpd.weather.WeatherWebApi;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static pt.isel.mpd.util.LazyQueries.filter;

public class LazyQueriesTest {
    final Iterable<WeatherInfo> jan;

    public LazyQueriesTest() {
        WeatherWebApi api = new WeatherWebApi(new MockRequest());
        jan = api.pastWeather(37.017, -7.933, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 30));
    }
    static boolean cloudyDays(WeatherInfo info) {
        return info.desc.toLowerCase().contains("cloud");
    }

    @Test
    public void testFilterCloudyDays() {
        Iterable<WeatherInfo> cloud = filter(jan, LazyQueriesTest::cloudyDays);
        assertEquals(14, LazyQueries.count(cloud));
    }

}
