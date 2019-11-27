package Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Music.GuildMusicManager;
import Music.PlayerManager;
import Music.TrackScheduler;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PauseCommand extends Command {

	public PauseCommand() {
		this.name = "pause";
		this.help = "Pauses the current song";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		
		MessageChannel channel = event.getChannel();
		AudioManager audioManager = event.getGuild().getAudioManager();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		TrackScheduler scheduler = musicManager.scheduler;
		AudioPlayer player = musicManager.player;
		
		if (!audioManager.isConnected()) {
			channel.sendMessage("I'm not in a voice channel how am I supposed to pause?").queue();
			
			return;
		}
		
		if (player.getPlayingTrack() == null) {
			channel.sendMessage("The player isn't playing anything").queue();
			
			return;
		}
		
		scheduler.pause();
		channel.sendMessage("use ~resume to resume playback").queue();
	}

}
