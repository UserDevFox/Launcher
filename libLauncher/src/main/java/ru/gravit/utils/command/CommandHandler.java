package ru.gravit.utils.command;

import ru.gravit.utils.helper.CommonHelper;
import ru.gravit.utils.helper.LogHelper;
import ru.gravit.utils.helper.VerifyHelper;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CommandHandler implements Runnable {
    private final Map<String, Command> commands = new ConcurrentHashMap<>(32);

    public final void eval(String line, boolean bell) {
        LogHelper.info("Command '%s'", line);

        // Parse line to tokens
        String[] args;
        try {
            args = CommonHelper.parseCommand(line);
        } catch (Exception e) {
            LogHelper.error(e);
            return;
        }

        // Evaluate command
        eval(args, bell);
    }


    public final void eval(String[] args, boolean bell) {
        if (args.length == 0)
            return;

        // Measure start time and invoke command
        Instant startTime = Instant.now();
        try {
            lookup(args[0]).invoke(Arrays.copyOfRange(args, 1, args.length));
        } catch (Exception e) {
            LogHelper.error(e);
        }

        // Bell if invocation took > 1s
        Instant endTime = Instant.now();
        if (bell && Duration.between(startTime, endTime).getSeconds() >= 5)
            try {
                bell();
            } catch (IOException e) {
                LogHelper.error(e);
            }
    }


    public final Command lookup(String name) throws CommandException {
        Command command = commands.get(name);
        if (command == null)
            throw new CommandException(String.format("Unknown command: '%s'", name));
        return command;
    }


    public abstract String readLine() throws IOException;

    private void readLoop() throws IOException {
        for (String line = readLine(); line != null; line = readLine())
            eval(line, true);
    }


    public final void registerCommand(String name, Command command) {
        VerifyHelper.verifyIDName(name);
        VerifyHelper.putIfAbsent(commands, name, Objects.requireNonNull(command, "command"),
                String.format("Command has been already registered: '%s'", name));
    }

    @Override
    public final void run() {
        try {
            readLoop();
        } catch (IOException e) {
            LogHelper.error(e);
        }
    }



    public abstract void bell() throws IOException;


    public abstract void clear() throws IOException;


    public final Map<String, Command> commandsMap() {
        return Collections.unmodifiableMap(commands);
    }



}
