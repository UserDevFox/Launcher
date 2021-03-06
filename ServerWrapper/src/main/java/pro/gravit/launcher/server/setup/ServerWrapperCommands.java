package pro.gravit.launcher.server.setup;

import java.io.IOException;

import pro.gravit.utils.command.CommandHandler;
import pro.gravit.utils.command.JLineCommandHandler;
import pro.gravit.utils.command.StdCommandHandler;
import pro.gravit.utils.helper.LogHelper;

public class ServerWrapperCommands {
    public final CommandHandler commandHandler;

    public void registerCommands() {
        //FUTURE
    }

    public ServerWrapperCommands(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public ServerWrapperCommands() throws IOException {
        // Set command handler
        CommandHandler localCommandHandler;
        try {
            Class.forName("org.jline.terminal.Terminal");

            // JLine2 available
            localCommandHandler = new JLineCommandHandler();
            LogHelper.info("JLine2 terminal enabled");
        } catch (ClassNotFoundException ignored) {
            localCommandHandler = new StdCommandHandler(true);
            LogHelper.warning("JLine2 isn't in classpath, using std");
        }
        commandHandler = localCommandHandler;
    }
}
