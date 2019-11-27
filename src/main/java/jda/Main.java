package jda;

import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import Commands.ClearCommand;
import Commands.HangManCommand;
import Commands.MyNameIsCommand;
import Commands.PingCommand;
import Commands.Music.JoinCommand;
import Commands.Music.LeaveCommand;
import Commands.Music.PlayCommand;
import Commands.Music.QueueCommand;
import Commands.Music.StopCommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

public class Main {
	
	private final Random random = new Random();

	private final MyNameIsCommand myNameIsCommand;

	public Main() throws IOException, LoginException, IllegalArgumentException, RateLimitedException {

		myNameIsCommand = new MyNameIsCommand(this);

		// Defines an event waiter
		EventWaiter waiter = new EventWaiter();
		
		EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                		.setColor(getRandomColor())
                        .setFooter("IkeFamDid", null)
                        .setTimestamp(Instant.now())
        );

		// Defines the builder
		CommandClientBuilder builder = new CommandClientBuilder();

		// Sets owner and prefix
		builder.setOwnerId("145767766346039296");
		builder.setPrefix("~");
		builder.setStatus(OnlineStatus.IDLE).setActivity(Activity.playing("Loading..."));

		// Instances generic commands with prefix
		builder.addCommands(

				new PingCommand(),
				new ClearCommand(),
				new HangManCommand(waiter),

				new JoinCommand(),
				new LeaveCommand(),
				new PlayCommand(),
				new StopCommand(),
				new QueueCommand()
		);

		// Defines the builder as client
		CommandClient client = builder.build();

		// Start the Bot account
		@SuppressWarnings("unused")
		JDA jdaBuilder = new JDABuilder(AccountType.BOT)

				// Set the token
				.setToken(Secret.TOKEN)

				// Set status and game playing
				.setStatus(OnlineStatus.IDLE).setActivity(Activity.playing("Loading..."))

				// Add the listeners
				.addEventListeners(waiter, client, new Listener(this))

				// Start
				.build();
	}
	
    private Color getRandomColor() {
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }

	// Getters
	public MyNameIsCommand getMyNameIsCommand() {
		return myNameIsCommand;
	}

	public static void main(String[] args)
			throws IOException, LoginException, IllegalArgumentException, RateLimitedException {
		new Main();
	}

}
