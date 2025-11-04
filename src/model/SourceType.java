/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nat
 */
public enum SourceType {
    RESCUED,
    SURRENDERED,
    BORN_IN_SHELTER,
    TRANSFERRED,
    UNKNOWN; // Default value

    // Helper method to safely convert a string from JSON
    public static SourceType fromString(String text) {
        if (text != null) {
            for (SourceType b : SourceType.values()) {
                if (text.equalsIgnoreCase(b.name())) {
                    return b;
                }
            }
        }
        return UNKNOWN;
    }
}
