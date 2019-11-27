package Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Music.GuildMusicManager;
import Music.PlayerManager;
import Music.TrackScheduler;
import net.dv8tion.jda.api.entities.MessageChannel;

public class SkipCommand extends Command {

	public SkipCommand() {
		this.name = "skip";
		this.help = "Skips current song";
		this.guildOnly = true;
		}

	@Override
	protected void execute(CommandEvent event) {
		MessageChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		TrackScheduler scheduler = musicManager.scheduler;
		AudioPlayer player = musicManager.player;
		
		if (player.getPlayingTrack() == null) {
			channel.sendMessage("The player isn't playing anything").queue();
			
			return;
		}
		
		scheduler.nextTrack();
		
		channel.sendMessage("Skipping the current track").queue();
	}

}
