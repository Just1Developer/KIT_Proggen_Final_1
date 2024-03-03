package edu.kit.kastel.codefight.model;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.aicommands.AICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The main Codefight class, handles matches and globally available AIs.
 *
 * @author uwwfh
 */
public class Codefight {
    
    /**
     * For cases where an invalid address is needed.
     */
    private static final int INVALID_ADDRESS = -1;
    
    private static Memory memory;
    private static final List<AIPlayer> AVAILABLE_AI_PLAYERS = new ArrayList<>();
    private final List<AIPlayer> totalIngameAIs;
    private final List<AIPlayer> playingAIs;
    private int currentAIindex;
    private boolean skipNextAICommand;
    
    /**
     * Creates a new Codefight game on a memory with the given size using the listed AIs.
     * @param aiNames The AIs that participate.
     */
    public Codefight(String... aiNames) {
        playingAIs = getAIsByName(aiNames);
        totalIngameAIs = List.copyOf(playingAIs);
        currentAIindex = 0;
        skipNextAICommand = false;
        // AI Setup
        for (int i = 0; i < playingAIs.size(); ++i) {
            AIPlayer player = playingAIs.get(i);
            player.reset();
            player.setPrintWrapper(Main.getPrintWrapper(i));
        }
        memory.reset(playingAIs);
    }
    
    /**
     * Gets the amount of AIs involved in the game.
     * @return The amount of AIs ingame, dead or alive.
     */
    public int getTotalPlayingAICount() {
        return totalIngameAIs.size();
    }
    
    /**
     * Gets a copy of all ingame AIs, dead or alive.
     * @return List of all AIs.
     */
    public List<AIPlayer> getTotalIngameAIs() {
        List<AIPlayer> copy = new ArrayList<>();
        for (AIPlayer player : totalIngameAIs) {
            copy.add(new AIPlayer(player));
        }
        return copy;
    }
    
    /**
     * Gets a copy of all alive AIs. Players are copies.
     * @return List of all alive AIs.
     */
    public List<AIPlayer> getAliveAIs() {
        List<AIPlayer> copy = new ArrayList<>();
        for (AIPlayer player : playingAIs) {
            copy.add(new AIPlayer(player));
        }
        return copy;
    }
    
    /**
     * Gets the current memory address of the AI whose turn
     * will be executed next.
     * Does take the skip into account.
     * @return Next AI Memory Address.
     */
    public int getNextAIAddress() {
        if (playingAIs.isEmpty()) {
            return INVALID_ADDRESS;
        }
        AIPlayer nextAI;
        if (skipNextAICommand) {
            int nextIndex = currentAIindex >= playingAIs.size() ? 0 : currentAIindex + 1;
            nextAI = playingAIs.get(nextIndex);
        } else {
            nextAI = playingAIs.get(currentAIindex);
        }
        return nextAI.getMemoryPtr();
    }
    
    /**
     * Gets a currently playing AI by name. If no AI with that name is found returns empty optional.
     * @param aiName The name of the AI.
     * @return Optional AI player with the given name.
     */
    public Optional<AIPlayer> getPlayingAIbyName(String aiName) {
        for (AIPlayer player : totalIngameAIs) {
            if (player.getAIName().equals(aiName)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Plays a given number of turns.
     * If only 1 AI remains alive, the turns are ended prematurely
     * and the game is declared over.
     * @param steps The amount of turns.
     */
    public void next(int steps) {
        for (int i = 0; i < steps && !playingAIs.isEmpty(); ++i) {
            nextTurn();
        }
    }
    
    /**
     * Plays exactly one turn.
     * Removes the AI that played from the playing AIs if it's dead after the turn.
     * <p></p>
     * If skipNextAICommand is true, so the last command was a successful comparison,
     * the command will simply be skipped.
     */
    private void nextTurn() {
        if (skipNextAICommand) {
            skipNextAICommand = false;
            increaseAICounter();
            return;
        }
        AIPlayer player = playingAIs.get(currentAIindex);
        AICommand cmd = memory.readMemory(player.getMemoryPtr()).getCommand();
        cmd.execute(this, player);
        if (player.isDead()) {
            playingAIs.remove(player);
        }
        player.increaseMoveCount();
        increaseAICounter();
    }
    
    /**
     * Increases the AI counter by one. Basically advances to the next player.
     */
    private void increaseAICounter() {
        if (currentAIindex >= playingAIs.size() - 1) {
            currentAIindex = 0;
        } else {
            currentAIindex++;
        }
    }
    
    /**
     * If not already set to true, will set the indicator
     * to true to skip the next AI command.
     */
    public void skipNextTurn() {
        this.skipNextAICommand = true;
    }
    
    /**
     * Sets up the memory for the Codefight games. This will reset the memory if it's already in use.
     * @param memorySize The size of the memory
     * @param memoryInitType The initialization type of unpopulated memory cells.
     * @param seed The seed for the random generator. If init type is STOP_FILL, parameter will be ignored.
     */
    public static void initMemory(int memorySize, MemoryInitType memoryInitType, long seed) {
        Memory.initMemory(memorySize, memoryInitType, seed);
        memory = new Memory();
    }
    
    /**
     * Adds an AI to be available for a future game. AI is only
     * added if it's not already added.
     * @param player The AI player
     */
    public static void addAI(AIPlayer player) {
        if (!AVAILABLE_AI_PLAYERS.contains(player)) {
            AVAILABLE_AI_PLAYERS.add(player);
        }
    }
    
    /**
     * Removes an AI by names from the list of available AIs.
     * Returns true if the player was removed successfully, false if no
     * AI with that name could be found.
     * @param playerName The name of the AI
     * @return True if removed, false if not
     */
    public static boolean removeAI(String playerName) {
        for (AIPlayer player : AVAILABLE_AI_PLAYERS) {
            if (player.getAIName().equals(playerName)) {
                AVAILABLE_AI_PLAYERS.remove(player);
                return true;
            }
        }
        return false;
    }
    
    /**
     * If an AI by the specified name is already registered.
     * @param aiName The name of the AI.
     * @return If an AI by the name exists.
     */
    public static boolean containsAI(String aiName) {
        for (AIPlayer player : AVAILABLE_AI_PLAYERS) {
            if (player.getAIName().equals(aiName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the amount of AIs currently registered.
     * @return The amount of AIs
     */
    public static int getAICount() {
        return AVAILABLE_AI_PLAYERS.size();
    }
    
    /**
     * Gets an AI by name. If no AI with that name is found returns empty optional.
     * Note that current states of ingame AIs cannot be retrieved accurately using this method.
     * @param aiName The name of the AI.
     * @return Optional AI player with the given name.
     */
    public static Optional<AIPlayer> getAIbyName(String aiName) {
        for (AIPlayer player : AVAILABLE_AI_PLAYERS) {
            if (player.getAIName().equals(aiName)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Gets a collection of non-distinct AIs matching the names from the total AI players list,
     * so will return a subset of all players.
     * All AIs in the resulting list are clones.
     * <p></p>
     * Returns empty collection when no match is found, names that weren't found are ignored.
     * @param names The names of the AIs
     * @return A List of all AIs with the given names.
     */
    private List<AIPlayer> getAIsByName(String... names) {
        final List<AIPlayer> players = new ArrayList<>();
        
        for (String name : names) {
            Optional<AIPlayer> player = getAIbyName(name);
            player.ifPresent(aiPlayer -> players.add(new AIPlayer(aiPlayer)));
        }
        return players;
    }
    
    /**
     * Gets the global memory. Not immutable, changes are
     * reflected in the current game.
     * @return The game's memory.
     */
    public static Memory getMemory() {
        return memory;
    }
}
