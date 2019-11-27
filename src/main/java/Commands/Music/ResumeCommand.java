package Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Music.GuildMusicManager;
import Music.PlayerManager;
import Music.TrackScheduler;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class ResumeCommand extends Command {

	public ResumeCommand() {
		this.name = "resume";
		this.help = "Resumes the current song";
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
			channel.sendMessage("I'm not in a voice channel how am I supposed to resume?").queue();

			return;
		}
		
		if (!player.isPaused()) {
			channel.sendMessage("I'm already playing something").queue();
			
			return;
		}

		scheduler.resume();
		channel.sendMessage("Resuming song").queue();
	}

}
