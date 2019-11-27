package jda;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import Commands.ClearCommand;
import Commands.HangManCommand;
import Commands.MyNameIsCommand;
import Commands.PingCommand;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

public class Main{
	
	private final MyNameIsCommand myNameIsCommand;

	public Main() throws IOException, LoginException, IllegalArgumentException, RateLimitedException {

		myNameIsCommand = new MyNameIsCommand(this);
		
		// Defines an event waiter
		EventWaiter waiter = new EventWaiter();

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
				
				new HangManCommand(waiter)

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
	
	// Getters
	public MyNameIsCommand getMyNameIsCommand() {
		return myNameIsCommand;
	}
	
	public static void main(String[] args) throws IOException, LoginException, IllegalArgumentException, RateLimitedException {
        new Main();
    }

}
