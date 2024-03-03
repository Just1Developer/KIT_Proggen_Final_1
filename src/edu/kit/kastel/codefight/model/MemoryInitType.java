package edu.kit.kastel.codefight.model;

/**
 * This class lists the two different memory initialization types.
 *
 * @author uwwfh
 */
public enum MemoryInitType {
    /**
     * The Stop Initialization type. Fills all unknowns with stop commands.
     */
    STOP_FILL,
    /**
     * The Random Initialization type. Fills all unknowns with pseudo-random commands.
     */
    RANDOM_FILL
}
