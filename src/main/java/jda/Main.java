package jda;

import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import Commands.ClearCommand;
import Commands.HangManCommand;
import Commands.MyNameIsCommand;
import Commands.PingCommand;
import Commands.Music.JoinCommand;
import Commands.Music.LeaveCommand;
import Commands.Music.NowPlayingCommand;
import Commands.Music.PauseCommand;
import Commands.Music.PlayCommand;
import Commands.Music.QueueCommand;
import Commands.Music.ResumeCommand;
import Commands.Music.SkipCommand;
import Commands.Music.StopCommand;
import Commands.Music.VolumeCommand;
import Music.AudioPlayerSendHandler;
import entities.Prompt;
import gui.GUI;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.RateLimitedException;

public class Main {

	Logger log = LoggerFactory.getLogger("Startup");

	private final Random random = new Random();

	private final MyNameIsCommand myNameIsCommand;

	private boolean shuttingDown = false;
	private JDA jda;
	private GUI gui;
	private Main main;

	public Main(String[] args) throws IOException, LoginException, IllegalArgumentException, RateLimitedException {

		myNameIsCommand = new MyNameIsCommand(this);

		// Defines an event waiter
		EventWaiter waiter = new EventWaiter();

		// create prompt to handle startup
		Prompt prompt = new Prompt("JMusicBot",
				"Switching to nogui mode. You can manually start in nogui mode by including the -Dnogui=true flag.",
				"true".equalsIgnoreCase(System.getProperty("nogui", "false")));

		// check deprecated nogui mode (new way of setting it is -Dnogui=true)
		for (String arg : args)
			if ("-nogui".equalsIgnoreCase(arg)) {
				prompt.alert(Prompt.Level.WARNING, "GUI", "The -nogui flag has been deprecated. "
						+ "Please use the -Dnogui=true flag before the name of the jar. Example: java -jar -Dnogui=true JMusicBot.jar");
				break;
			}

		EmbedUtils.setEmbedBuilder(() -> new EmbedBuilder().setColor(getRandomColor()).setFooter("IkeFamDid", null)
				.setTimestamp(Instant.now()));

		// Defines the builder
		CommandClientBuilder builder = new CommandClientBuilder();

		// Sets owner and prefix
		builder.setOwnerId("145767766346039296");
		builder.setPrefix("~");
		builder.setStatus(OnlineStatus.IDLE).setActivity(Activity.playing("Loading..."));

		// Instances generic commands with prefix
		builder.addCommands(

				// General Commands
				new PingCommand(), new ClearCommand(), new HangManCommand(waiter),

				// Music Commands
				new JoinCommand(), new LeaveCommand(), new PlayCommand(), new StopCommand(), new QueueCommand(),
				new SkipCommand(), new NowPlayingCommand(), new PauseCommand(), new ResumeCommand(),
				new VolumeCommand());

		// Defines the builder as client
		CommandClient client = builder.build();

		if (!prompt.isNoGUI()) {
			try {
				GUI gui = new GUI(main);
				gui.init();
			} catch (Exception e) {
				log.error("Could not start GUI. If you are "
						+ "running on a server or in a location where you cannot display a "
						+ "window, please run in nogui mode using the -Dnogui=true flag.");
			}
		}

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

	public void shutdown() {
		if (shuttingDown)
			return;
		shuttingDown = true;
		if (jda.getStatus() != JDA.Status.SHUTTING_DOWN) {
			jda.getGuilds().stream().forEach(g -> {
				g.getAudioManager().closeAudioConnection();
				AudioPlayerSendHandler ah = (AudioPlayerSendHandler) g.getAudioManager().getSendingHandler();
				if (ah != null) {
					ah.stopAndClear();
					ah.getPlayer().destroy();
				}
			});
			jda.shutdown();
		}
		if (gui != null)
			gui.dispose();
		System.exit(0);
	}

	public static void main(String[] args)
			throws IOException, LoginException, IllegalArgumentException, RateLimitedException {
		new Main(args);
	}

}
