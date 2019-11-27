package Commands.Music;

import java.net.MalformedURLException;
import java.net.URL;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import Music.PlayerManager;
import net.dv8tion.jda.api.entities.MessageChannel;

public class PlayCommand extends Command {

	public PlayCommand() {
		this.name = "play";
		this.help = "plays song";
		this.arguments = "<songUrl>";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		
		MessageChannel channel = event.getChannel();
		
		if (event.getArgs().isEmpty()) {
			channel.sendMessage("You didnt give me a song").queue();
			
			return;
		}
		
		String input = String.join(" ", event.getArgs());
		
		if (!isUrl(input) && input.startsWith("ytsearch:")) {
			channel.sendMessage("Please provide a valid youtube, soundcloud, or bandcamp link").queue();
			
			return;
		}
		
		PlayerManager manager = PlayerManager.getInstance();
		
		manager.loadAndPlay(event.getTextChannel(), input);
	}
	
	private boolean isUrl(String input) {
		try {
			new URL(input);
			
			return true;
		} catch (MalformedURLException ignored) {
			return false;
		}
	}

}
