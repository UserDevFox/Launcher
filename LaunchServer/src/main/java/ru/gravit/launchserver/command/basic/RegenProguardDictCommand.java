package ru.gravit.launchserver.command.basic;

import java.io.IOException;

import ru.gravit.launchserver.LaunchServer;
import ru.gravit.launchserver.command.Command;

public class RegenProguardDictCommand extends Command {

    public RegenProguardDictCommand(LaunchServer server) {
        super(server);
    }

    @Override
    public String getArgsDescription() {
        return null;
    }

    @Override
    public String getUsageDescription() {
        return "Regenerates proguard dictonary";
    }

    @Override
    public void invoke(String... args) throws IOException {
        server.proguardConf.genWords(true);
    }

}
