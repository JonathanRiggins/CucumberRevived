package Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import Music.GuildMusicManager;
import Music.PlayerManager;
import net.dv8tion.jda.api.managers.AudioManager;

public class StopCommand extends Command {

	public StopCommand() {
		this.name = "stop";
		this.help = "Stops the surrent song";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		AudioManager audioManager = event.getGuild().getAudioManager();

		musicManager.scheduler.getQueue().clear();
		musicManager.player.stopTrack();
		musicManager.player.setPaused(false);
		audioManager.closeAudioConnection();

		event.getChannel().sendMessage("Stoppping the player and clearing the queue").queue();
	}

}
