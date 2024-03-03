package edu.kit.kastel.codefight.model.diagnostic;

/**
 * This exception occurs when the memory tries to read from a pointer that it does not recognize.
 *
 * @author uwwfh
 */
public class InvalidPointerException extends IllegalArgumentException {
    /**
     * Constructs a new Invalid Pointer Exception with a message.
     * @param message An error message with some information.
     */
    public InvalidPointerException(String message) {
        super(message);
    }
}
