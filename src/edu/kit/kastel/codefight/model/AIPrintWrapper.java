package edu.kit.kastel.codefight.model;

import java.util.Objects;

/**
 * This class is a wrapper that contains the Symbols for AI printing outputs.
 *
 * @param defaultSymbol The default symbol as String.
 * @param bombSymbol The bomb symbol as String.
 * @author uwwfh
 */
public record AIPrintWrapper(String defaultSymbol, String bombSymbol) {
    
    /**
     * A default AI PrintWrapper with just null entries.
     */
    public static final AIPrintWrapper NULL_WRAPPER = new AIPrintWrapper(null, null);
    
    /**
     * Constructs a new print wrapper.
     *
     * @param defaultSymbol The default symbol.
     * @param bombSymbol    The bomb symbol.
     */
    public AIPrintWrapper {
    }
    
    /**
     * Gets the default symbol used for the AI.
     *
     * @return The default symbol.
     */
    @Override
    public String defaultSymbol() {
        return defaultSymbol;
    }
    
    /**
     * Gets the Symbol used for AI bombs.
     *
     * @return The bomb symbol.
     */
    @Override
    public String bombSymbol() {
        return bombSymbol;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        // Compare exact class types, but use this to account for inherited types
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        return obj.hashCode() == this.hashCode();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(defaultSymbol, bombSymbol);
    }
}
