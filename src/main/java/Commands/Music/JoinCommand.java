package Commands.Music;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand extends Command {

	public JoinCommand() {
		this.name = "join";
		this.help = "joins your current voice channel";
		this.botPermissions = new Permission[] {Permission.VOICE_CONNECT};
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		
		MessageChannel channel = event.getChannel();
		AudioManager audioManager = event.getGuild().getAudioManager();
		
		if (audioManager.isConnected()) {
			channel.sendMessage("I'm already connected to a channel").queue();
			return;
		}
		
		GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
		
		if (!memberVoiceState.inVoiceChannel()) {
			channel.sendMessage("You need to join a channel first").queue();
			return;
		}
		
		VoiceChannel voiceChannel = memberVoiceState.getChannel();
		
		audioManager.openAudioConnection(voiceChannel);
		channel.sendMessage("Joining your voice channel").queue();
	}

}
