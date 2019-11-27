package Music;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class PlayerManager {
	
	private static PlayerManager INSTANCE;
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;
	
	private PlayerManager() {
		this.musicManagers = new HashMap<> ();
		
		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}
	
	public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
		long guildId = guild.getIdLong();
		GuildMusicManager musicManager = musicManagers.get(guildId);
		
		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildId, musicManager);
		}
		
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		
		return musicManager;
	}
	
	public void loadAndPlay(TextChannel channel, String trackUrl, CommandEvent event) {
		GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
		
		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			
			@Override
			public void trackLoaded(AudioTrack track) {
				channel.sendMessage(EmbedUtils.embedMessage(String.format(
						"**Adding to queue** [%s](%s)\n%s %s",
						track.getInfo().title,
						track.getInfo().uri,
						"🕑",
						formatTime(track.getDuration())
					)).build()).queue();
				
				GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
				VoiceChannel vChannel = memberVoiceState.getChannel();
				if (!memberVoiceState.inVoiceChannel()) {
					channel.sendMessage("You need to join a channel first").queue();
					return;
				}
				event.getGuild().getAudioManager().openAudioConnection(vChannel);
				play(musicManager, track);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				
				if (firstTrack == null) {
					firstTrack = playlist.getTracks().remove(0);
				}
				
				channel.sendMessage(EmbedUtils.embedMessage(String.format(
					"**Adding to queue** [%s](%s)\n%s %s",
					firstTrack.getInfo().title,
					firstTrack.getInfo().uri,
					"🕑",
					formatTime(firstTrack.getDuration())
				)).build()).queue();
				
				GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
				VoiceChannel vChannel = memberVoiceState.getChannel();
				if (!memberVoiceState.inVoiceChannel()) {
					channel.sendMessage("You need to join a channel first").queue();
					return;
				}
				event.getGuild().getAudioManager().openAudioConnection(vChannel);
				play(musicManager, firstTrack);
				
				playlist.getTracks().forEach(musicManager.scheduler::queue);
			}
			
			@Override
			public void noMatches() {
				channel.sendMessage("Nothing found by " + trackUrl).queue();
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Could not play " + exception.getMessage()).queue();
			}
		});
	}
	
	private void play(GuildMusicManager musicManager, AudioTrack track) {
		musicManager.scheduler.queue(track);
	}
	
	private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
	
	
	public static synchronized PlayerManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PlayerManager();
		}
		
		return INSTANCE;
	}
}
