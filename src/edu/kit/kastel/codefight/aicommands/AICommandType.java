package edu.kit.kastel.codefight.aicommands;

/**
 * This class represents all different types of AI commands that exist.
 *
 * @author uwwfh
 */
public enum AICommandType {
    /**
     * The STOP Command.
     */
    STOP,
    /**
     * The RELATIVE MOVE Command.
     */
    MOV_R,
    /**
     * The INDIRECT MOVE Command.
     */
    MOV_I,
    /**
     * The ADD Command.
     */
    ADD,
    /**
     * The RELATIVE ADD Command.
     */
    ADD_R,
    /**
     * The regular JUMP Command.
     */
    JMP,
    /**
     * The CONDITIONAL JUMP Command.
     */
    JMZ,
    /**
     * The COMPARE Command.
     */
    CMP,
    /**
     * The SWAP Command.
     */
    SWAP
}
