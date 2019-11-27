package Commands.Music;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.annotation.Nullable;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import Music.GuildMusicManager;
import Music.PlayerManager;
import Music.TrackScheduler;
import net.dv8tion.jda.api.entities.MessageChannel;

public class PlayCommand extends Command {
	
	private final YouTube youTube;
	
	public PlayCommand() {
		this.name = "play";
		this.help = "plays song";
		this.arguments = "<songUrl>";
		this.guildOnly = true;
		
		YouTube temp = null;


        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("CucumberBot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;
	}
	


	@Override
	protected void execute(CommandEvent event) {
		
		MessageChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		TrackScheduler scheduler = musicManager.scheduler;
		AudioPlayer player = musicManager.player;
		
		if (player.isPaused()) {
			scheduler.resume();
			channel.sendMessage("Resuming song");
			
			return;
		}
		
		if (event.getArgs().isEmpty()) {
			channel.sendMessage("You didnt give me a song").queue();
			
			return;
		}
		
		String input = String.join(" ", event.getArgs());
		
		if (!isUrl(input)) {
			String ytSearched = searchYoutube(input);

            if (ytSearched == null) {
                channel.sendMessage("Youtube returned no results").queue();

                return;
            }

            input = ytSearched;
		}
		
		PlayerManager manager = PlayerManager.getInstance();
		
		manager.loadAndPlay(event.getTextChannel(), input, event);
	}
	
	private boolean isUrl(String input) {
		try {
			new URL(input);
			
			return true;
		} catch (MalformedURLException ignored) {
			return false;
		}
	}
	
	@Nullable
    private String searchYoutube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setMaxResults((long) 1)
                    .setQ(input)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey("AIzaSyBYbmrcO_1iMZB8UqV9OloK6VBZ8ftMNrk")
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
