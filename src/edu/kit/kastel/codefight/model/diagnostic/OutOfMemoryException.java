package edu.kit.kastel.codefight.model.diagnostic;

/**
 * This exception occurs when the codefight memory instance runs out of memory while populating the memory with all
 * players at setup.
 *
 * @author uwwfh
 */
public class OutOfMemoryException extends IllegalStateException {
    /**
     * Constructs a new Out of Memory Exception with a message.
     * @param message An error message with some information.
     */
    public OutOfMemoryException(String message) {
        super(message);
    }
}
