package it.fpili.happy_health_clinic.enums;

/**
 * Enumeration of blood types.
 * Includes all standard ABO and Rh blood group combinations.
 */
public enum BloodType {
    /**
     * A positive blood type.
     */
    A_POSITIVE("A+"),

    /**
     * A negative blood type.
     */
    A_NEGATIVE("A-"),

    /**
     * B positive blood type.
     */
    B_POSITIVE("B+"),

    /**
     * B negative blood type.
     */
    B_NEGATIVE("B-"),

    /**
     * AB positive blood type.
     */
    AB_POSITIVE("AB+"),

    /**
     * AB negative blood type.
     */
    AB_NEGATIVE("AB-"),

    /**
     * O positive blood type.
     */
    O_POSITIVE("O+"),

    /**
     * O negative blood type.
     */
    O_NEGATIVE("O-");

    /**
     * The display name representation of the blood type.
     */
    private final String displayName;

    /**
     * Constructs a BloodType with its display name.
     *
     * @param displayName the human-readable representation of the blood type
     */
    BloodType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the blood type.
     *
     * @return the display name (e.g., "A+", "O-")
     */
    public String getDisplayName() {
        return displayName;
    }
}
