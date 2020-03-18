/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pt.isel.mpd;

import org.junit.Test;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class WeatherWebApiTest {
    @Test public void testPastWeather() {
        WeatherWebApi api = new WeatherWebApi();
        List<WeatherInfo> jan = api.pastWeather(37.017, -7.933, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 30));
        jan.forEach(System.out::println);
    }

    // TPC: Add unit test for search() of WeatherWebApi
}