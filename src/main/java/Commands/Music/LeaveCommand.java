package Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand extends Command {

	public LeaveCommand() {
		this.name = "leave";
		this.help = "leaves your current voice channel";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {

		MessageChannel channel = event.getChannel();
		AudioManager audioManager = event.getGuild().getAudioManager();

		if (!audioManager.isConnected()) {
			channel.sendMessage("I'm not connected to a voice channel").queue();
			return;
		}

		audioManager.closeAudioConnection();
		channel.sendMessage("Leaving your voice channel").queue();
	}

}
