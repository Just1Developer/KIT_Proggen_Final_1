package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.model.GamePhase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class handles the user input and executes the commands.
 *
 * @author Programmieren-Team
 * @author uwwfh
 */
public final class CommandHandler {
    
    /**
     * The prefix for any error messages.
     */
    public static final String ERROR_PREFIX = "Error, ";
    private static final String COMMAND_HANDLER_NOT_INITIALIZED =
            "%sthe CommandHandler has not yet been initialized. Call CommandHandler.initialize() first.".formatted(ERROR_PREFIX);
    private static final String COMMAND_SEPARATOR_REGEX = " +";
    private static final String COMMAND_NOT_FOUND_FORMAT = "Command '%s' not found!";
    private static final String WRONG_ARGUMENTS_COUNT_FORMAT = "Wrong number of arguments for command '%s'!";
    private static final String WRONG_GAMEPHASE_FORMAT = "This command is not valid in the GamePhase %s";
    private static final String ADD_AI_COMMAND_NAME = "add-ai";
    private static final String SHOW_AI_COMMAND_NAME = "show-ai";
    private static final String REMOVE_AI_COMMAND_NAME = "remove-ai";
    private static final String SET_INIT_MODE_COMMAND_NAME = "set-init-mode";
    private static final String START_GAME_COMMAND_NAME = "start-game";
    private static final String NEXT_COMMAND_NAME = "next";
    private static final String END_GAME_COMMAND_NAME = "end-game";
    private static final String SHOW_MEMORY_COMMAND_NAME = "show-memory";
    private static final String HELP_COMMAND_NAME = "help";
    private static final String QUIT_COMMAND_NAME = "quit";
    private static final String INVALID_RESULT_TYPE_FORMAT = "Unexpected value: %s";
    
    private static Map<String, Command> commands;
    private static boolean running = false;
    
    private CommandHandler() { }
    
    /**
     * Initializes the CommandHandler. Must be executed before any calls can be made.
     * References to Codefight are via the current Codefight instance in Main.
     */
    public static void initialize() {
        commands = new HashMap<>();
        initCommands();
    }
    
    /**
     * Starts the interaction with the user.
     * CommandHandler must be initialized.
     */
    public static void handleUserInput() {
        if (commands == null) {
            System.err.println(COMMAND_HANDLER_NOT_INITIALIZED);
            return;
        }
        
        running = true;
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
            }
        }
    }
    
    /**
     * Quits the interaction with the user.
     */
    static void quit() {
        running = false;
    }
    
    private static void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splittedCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);
        
        executeCommand(commandName, commandArguments);
    }
    
    private static void executeCommand(String commandName, String[] commandArguments) {
        if (!commands.containsKey(commandName)) {
            System.err.println(ERROR_PREFIX + COMMAND_NOT_FOUND_FORMAT.formatted(commandName));
            return;
        }
        
        Command command = commands.get(commandName);
        if (!command.isValidInGamePhase(Main.getCurrentPhase())) {
            System.err.println(ERROR_PREFIX + WRONG_GAMEPHASE_FORMAT.formatted(Main.getCurrentPhase()));
            return;
        }
        
        if (command.getRequiredArgumentCount() > commandArguments.length
            || command.getRequiredArgumentCount() + command.getOptionalArgumentCount() < commandArguments.length) {
            System.err.println(ERROR_PREFIX + WRONG_ARGUMENTS_COUNT_FORMAT.formatted(commandName));
            return;
        }
        
        CommandResult result = command.execute(commandArguments);
        String output = switch (result.getType()) {
            case SUCCESS -> result.getMessage();
            case FAILURE -> ERROR_PREFIX + result.getMessage();
        };
        if (output != null) {
            switch (result.getType()) {
                case SUCCESS -> System.out.println(output);
                case FAILURE -> System.err.println(output);
                default -> throw new IllegalStateException(INVALID_RESULT_TYPE_FORMAT.formatted(result.getType()));
            }
        }
    }
    
    /**
     * Initializes the commands.
     */
    private static void initCommands() {
        addCommand(ADD_AI_COMMAND_NAME, new AddAICommand());
        addCommand(SHOW_AI_COMMAND_NAME, new ShowAICommand());
        addCommand(REMOVE_AI_COMMAND_NAME, new RemoveAICommand());
        addCommand(SET_INIT_MODE_COMMAND_NAME, new SetInitModeCommand());
        addCommand(START_GAME_COMMAND_NAME, new StartCommand());
        addCommand(NEXT_COMMAND_NAME, new NextCommand());
        addCommand(END_GAME_COMMAND_NAME, new EndCommand());
        addCommand(SHOW_MEMORY_COMMAND_NAME, new ShowMemoryCommand());
        addCommand(HELP_COMMAND_NAME, new HelpCommand());
        addCommand(QUIT_COMMAND_NAME, new QuitCommand());
    }
    
    /**
     * Gets a list of all command descriptions from commands that are valid in the given GamePhase.
     * List is sorted alphabetically by default.
     * @param validPhase The valid GamePhase of the commands.
     * @return An alphabetically sorted list with all valid commands' descriptions from the game phase.
     */
    static List<String> getCommandDescriptionsAlphabetically(GamePhase validPhase) {
        List<String> descriptions = new ArrayList<>();
        for (Command command : commands.values()) {
            if (command.isValidInGamePhase(validPhase)) {
                descriptions.add(command.getCommandDescription());
            }
        }
        descriptions.sort(Comparator.naturalOrder());
        return descriptions;
    }
    
    /**
     * Adds a command to the known commands to handle.
     *
     * @param commandName The command name
     * @param command The executioner
     */
    private static void addCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }
    
}
