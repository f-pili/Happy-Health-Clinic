package it.fpili.happy_health_clinic.services;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Service for generating avatar URLs.
 * Creates URLs for avatar images using the ui-avatars.com API.
 */
@Service
public class AvatarService {

    /**
     * Base URL for the ui-avatars API.
     */
    private static final String UI_AVATARS_BASE_URL = "https://ui-avatars.com/api/";

    /**
     * Generates an avatar URL for a user with default styling.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @return a URL string for the generated avatar image
     */
    public String generateAvatarUrl(String firstName, String lastName) {
        String fullName = firstName + " " + lastName;

        return UriComponentsBuilder.fromUriString(UI_AVATARS_BASE_URL)
                .queryParam("name", fullName)
                .queryParam("background", "0D8ABC")
                .queryParam("color", "fff")
                .queryParam("size", "200")
                .queryParam("bold", "true")
                .queryParam("rounded", "true")
                .build()
                .toUriString();
    }

    /**
     * Generates an avatar URL for a user with a custom background color.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param backgroundColor the desired background color (hex format with or without #)
     * @return a URL string for the generated avatar image
     */
    public String generateAvatarUrlWithCustomColor(String firstName, String lastName, String backgroundColor) {
        String fullName = firstName + " " + lastName;

        return UriComponentsBuilder.fromUriString(UI_AVATARS_BASE_URL)
                .queryParam("name", fullName)
                .queryParam("background", backgroundColor.replace("#", ""))
                .queryParam("color", "fff")
                .queryParam("size", "200")
                .queryParam("bold", "true")
                .queryParam("rounded", "true")
                .build()
                .toUriString();
    }
}
