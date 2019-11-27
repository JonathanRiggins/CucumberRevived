package Commands.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import Music.GuildMusicManager;
import Music.PlayerManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

public class QueueCommand extends Command{

	public QueueCommand() {
		this.name = "queue";
		this.help = "Shows the current queue for the music player";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		MessageChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
		
		if (queue.isEmpty()) {
			channel.sendMessage("The queue is empty").queue();
			
			return;
		}
		
		int trackCount = Math.min(queue.size(), 20);
		List<AudioTrack> tracks = new ArrayList<>(queue);
		@SuppressWarnings("static-access")
		EmbedBuilder builder = new EmbedUtils().defaultEmbed()
				.setTitle("Current Queue (Total: " + queue.size() + ")");
		
		for (int i = 0; i < trackCount; i++) {
			AudioTrack track = tracks.get(i);
			AudioTrackInfo info = track.getInfo();
			
			builder.appendDescription(String.format(
					"%s - %s\n",
					info.title,
					info.author
					
			));
		}
		
		channel.sendMessage(builder.build()).queue();
	}

}
