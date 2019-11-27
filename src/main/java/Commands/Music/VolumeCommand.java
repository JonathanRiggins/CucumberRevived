package Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Music.GuildMusicManager;
import Music.PlayerManager;
import net.dv8tion.jda.api.entities.MessageChannel;

public class VolumeCommand extends Command {

	public VolumeCommand() {
		this.name = "volume";
		this.help = "Changes the volume";
		this.arguments = "<Integer Volume>";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		
		MessageChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		AudioPlayer player = musicManager.player;
		
		String input = String.join(" ", event.getArgs());
		
		try {
			int i = Integer.parseInt(input);
			player.setVolume(i);
			
			channel.sendMessage("Volume set to " + input).queue();
			
		} catch (NumberFormatException e) {
			String volume = Integer.toString(player.getVolume());
			channel.sendMessage("The volume is set to " + volume).queue();
			
			return;
		}
		
	}

}
