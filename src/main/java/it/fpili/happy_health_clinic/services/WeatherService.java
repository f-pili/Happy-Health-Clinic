package it.fpili.happy_health_clinic.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Service for retrieving weather data from OpenWeather API.
 * Fetches current weather conditions and generates health-related alerts based on weather conditions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    /**
     * OpenWeather API key for authentication.
     */
    @Value("${openweather.api.key}")
    private String apiKey;

    /**
     * OpenWeather API URL for current weather data.
     */
    @Value("${openweather.api.url}")
    private String apiUrl;

    /**
     * REST template for making HTTP requests to external APIs.
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Retrieves weather data for a specified city.
     * Fetches current temperature and conditions, and generates health alerts if necessary.
     *
     * @param city the city name to fetch weather for
     * @return WeatherData object containing temperature, conditions, and health alerts
     */
    public WeatherData getWeatherForCity(String city) {
        try {
            String url = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("q", city + ",IT")
                    .queryParam("appid", apiKey)
                    .queryParam("units", "metric")
                    .queryParam("lang", "en")
                    .build()
                    .toUriString();

            log.info("Fetching weather for city: {}", city);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                log.warn("No weather data received for city: {}", city);
                return getDefaultWeather();
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> main = (Map<String, Object>) response.get("main");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");

            Map<String, Object> weather = weatherList.getFirst();

            double temp = ((Number) main.get("temp")).doubleValue();
            String description = (String) weather.get("description");
            String mainCondition = (String) weather.get("main");

            log.info("Weather data - Temp: {}°C, Condition: {}", temp, mainCondition);

            WeatherData weatherData = new WeatherData();
            weatherData.setTemperature(BigDecimal.valueOf(temp));
            weatherData.setConditions(description);
            weatherData.setAlert(generateHealthAlert(temp, mainCondition));

            return weatherData;

        } catch (Exception e) {
            log.error("Error fetching weather data for city {}: {}", city, e.getMessage());
            return getDefaultWeather();
        }
    }

    /**
     * Generates health-related alerts based on weather conditions and temperature.
     * Alerts are designed to inform patients and clinic staff of potential health risks.
     *
     * @param temp the current temperature in Celsius
     * @param condition the weather condition description
     * @return a health alert message or null if weather is normal
     */
    private String generateHealthAlert(double temp, String condition) {
        if (temp < 5) {
            return "⚠️ Very low temperatures - Caution for patients with respiratory issues";
        } else if (temp > 35) {
            return "⚠️ Very high temperatures - Risk of dehydration, hydration recommended";
        } else if ("Rain".equalsIgnoreCase(condition) || "Thunderstorm".equalsIgnoreCase(condition)) {
            return "🌧️ Bad weather - Possible appointment delays";
        } else if ("Snow".equalsIgnoreCase(condition)) {
            return "❄️ Snow - Be careful with transportation";
        } else if ("Fog".equalsIgnoreCase(condition) || "Mist".equalsIgnoreCase(condition)) {
            return "🌫️ Fog - Reduced visibility, be cautious";
        }
        return null;
    }

    /**
     * Returns default weather data when API is unavailable.
     *
     * @return default WeatherData object
     */
    private WeatherData getDefaultWeather() {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(BigDecimal.valueOf(20.0));
        weatherData.setConditions("Weather data unavailable");
        weatherData.setAlert(null);
        return weatherData;
    }

    /**
     * Data class for holding weather information.
     * Encapsulates temperature, conditions, and health alerts.
     */
    @Data
    public static class WeatherData {
        /**
         * The current temperature in Celsius.
         */
        private BigDecimal temperature;

        /**
         * A description of the current weather conditions.
         */
        private String conditions;

        /**
         * A health-related alert message based on weather conditions, or null if no alert.
         */
        private String alert;
    }
}