package it.fpili.happy_health_clinic.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

/**
 * Service for retrieving drug information from the FDA API.
 * Fetches and extracts drug-related data including side effects and contraindications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DrugService {

    /**
     * Base URL for the FDA drug label API.
     */
    private static final String FDA_API_URL = "https://api.fda.gov/drug/label.json";

    /**
     * REST template for making HTTP requests to external APIs.
     */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Retrieves drug information from the FDA API based on drug name.
     * Extracts purpose, side effects, and contraindications information.
     *
     * @param drugName the name of the drug to search for
     * @return DrugInformation object containing drug details
     */
    public DrugInformation getDrugInformation(String drugName) {
        try {
            String url = UriComponentsBuilder.fromUriString(FDA_API_URL)
                    .queryParam("search", "openfda.brand_name:\"" + drugName + "\"")
                    .queryParam("limit", "1")
                    .build()
                    .toUriString();

            log.info("Fetching drug information for: {}", drugName);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("results")) {
                log.warn("No drug information found for: {}", drugName);
                return getDefaultDrugInfo(drugName);
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            if (results.isEmpty()) {
                log.warn("Empty results for drug: {}", drugName);
                return getDefaultDrugInfo(drugName);
            }

            Map<String, Object> drugData = results.get(0);

            DrugInformation drugInfo = new DrugInformation();

            drugInfo.setDrugInfo(extractDrugInfo(drugData));
            drugInfo.setSideEffects(extractSideEffects(drugData));
            drugInfo.setContraindications(extractContraindications(drugData));

            log.info("Drug information retrieved - drugInfo: {}, sideEffects: {}, contraindications: {}",
                    drugInfo.getDrugInfo() != null ? "Present" : "None",
                    drugInfo.getSideEffects() != null ? "Present" : "None",
                    drugInfo.getContraindications() != null ? "Present" : "None");

            return drugInfo;

        } catch (Exception e) {
            log.error("Error fetching drug information for {}: {}", drugName, e.getMessage());
            return getDefaultDrugInfo(drugName);
        }
    }

    /**
     * Extracts purpose and indication information from drug data.
     *
     * @param drugData the raw drug data from the API
     * @return the purpose information or null if not available
     */
    private String extractDrugInfo(Map<String, Object> drugData) {
        StringBuilder info = new StringBuilder();

        String purpose = extractFirstAvailableField(drugData, "purpose", "indications_and_usage", "description");
        if (purpose != null && !purpose.isEmpty()) {
            info.append("Purpose: ").append(purpose);
        }

        String result = info.toString().trim();
        return result.isEmpty() ? null : truncate(result, 500);
    }

    /**
     * Extracts side effects information from drug data.
     *
     * @param drugData the raw drug data from the API
     * @return the side effects information or null if not available
     */
    private String extractSideEffects(Map<String, Object> drugData) {
        return extractFirstAvailableField(drugData,
                "adverse_reactions",
                "warnings",
                "warnings_and_cautions",
                "precautions");
    }

    /**
     * Extracts contraindications information from drug data.
     *
     * @param drugData the raw drug data from the API
     * @return the contraindications information or null if not available
     */
    private String extractContraindications(Map<String, Object> drugData) {
        return extractFirstAvailableField(drugData,
                "contraindications",
                "do_not_use",
                "when_using");
    }

    /**
     * Extracts the first available field from multiple field name options.
     *
     * @param drugData the raw drug data
     * @param fieldNames the field names to search for in order
     * @return the value of the first available field or null if none found
     */
    private String extractFirstAvailableField(Map<String, Object> drugData, String... fieldNames) {
        for (String fieldName : fieldNames) {
            String value = extractListField(drugData, fieldName);
            if (value != null && !value.isEmpty()) {
                log.debug("Found data in field: {}", fieldName);
                return value;
            }
        }
        return null;
    }

    /**
     * Extracts a field value from drug data, handling both list and string formats.
     *
     * @param drugData the raw drug data
     * @param fieldName the name of the field to extract
     * @return the extracted field value or null if not available
     */
    private String extractListField(Map<String, Object> drugData, String fieldName) {
        try {
            if (!drugData.containsKey(fieldName)) {
                return null;
            }

            Object field = drugData.get(fieldName);
            if (field instanceof List) {
                List<String> items = (List<String>) field;
                if (items.isEmpty()) {
                    return null;
                }
                return truncate(items.get(0), 500);
            } else if (field instanceof String) {
                return truncate((String) field, 500);
            }
            return null;
        } catch (Exception e) {
            log.warn("Error extracting field {}: {}", fieldName, e.getMessage());
            return null;
        }
    }

    /**
     * Truncates text to a maximum length.
     *
     * @param text the text to truncate
     * @param maxLength the maximum length
     * @return the truncated text with ellipsis if exceeding max length
     */
    private String truncate(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }

    /**
     * Returns default drug information when API data is not available.
     *
     * @param drugName the name of the drug
     * @return default DrugInformation object
     */
    private DrugInformation getDefaultDrugInfo(String drugName) {
        DrugInformation drugInfo = new DrugInformation();
        drugInfo.setDrugInfo("Information not available for " + drugName);
        drugInfo.setSideEffects(null);
        drugInfo.setContraindications(null);
        return drugInfo;
    }

    /**
     * Data class for holding drug information.
     * Encapsulates drug details including side effects and contraindications.
     */
    public static class DrugInformation {
        /**
         * General information about the drug and its purpose.
         */
        private String drugInfo;

        /**
         * Known side effects of the drug.
         */
        private String sideEffects;

        /**
         * Contraindications and warnings for the drug.
         */
        private String contraindications;

        /**
         * Gets the drug information.
         *
         * @return the drug information
         */
        public String getDrugInfo() {
            return drugInfo;
        }

        /**
         * Sets the drug information.
         *
         * @param drugInfo the drug information to set
         */
        public void setDrugInfo(String drugInfo) {
            this.drugInfo = drugInfo;
        }

        /**
         * Gets the side effects information.
         *
         * @return the side effects
         */
        public String getSideEffects() {
            return sideEffects;
        }

        /**
         * Sets the side effects information.
         *
         * @param sideEffects the side effects to set
         */
        public void setSideEffects(String sideEffects) {
            this.sideEffects = sideEffects;
        }

        /**
         * Gets the contraindications information.
         *
         * @return the contraindications
         */
        public String getContraindications() {
            return contraindications;
        }

        /**
         * Sets the contraindications information.
         *
         * @param contraindications the contraindications to set
         */
        public void setContraindications(String contraindications) {
            this.contraindications = contraindications;
        }
    }
}