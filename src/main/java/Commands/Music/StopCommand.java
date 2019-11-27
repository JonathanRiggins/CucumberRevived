package Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import Music.GuildMusicManager;
import Music.PlayerManager;

public class StopCommand extends Command {

	public StopCommand() {
		this.name = "play";
		this.help = "Stops the surrent song";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());

		musicManager.scheduler.getQueue().clear();
		musicManager.player.stopTrack();
		musicManager.player.setPaused(false);

		event.getChannel().sendMessage("Stoppping the player and clearing the queue").queue();
	}

}
