package Commands;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HangManCommand extends Command {

	private final EventWaiter waiter;
	
	public ArrayList<String> wordArray = new ArrayList<String>();
	public String word;

	public HangManCommand(EventWaiter waiter) {
		this.name = "hangman";
		this.guildOnly = true;
		this.help = "It's hangman what more can I say";
		this.waiter = waiter;
	}

	@Override
	protected void execute(CommandEvent event) {
		
		if (wordArray.isEmpty()) {
			event.reply("Creating your hangman game please dm your desired word");

			waiter.waitForEvent(MessageReceivedEvent.class,
					e -> e.getAuthor().equals(event.getAuthor()) && !e.getMessage().equals(event.getMessage()),
					// respond, inserting the name they listed into the response
					e -> word = event.getMessage().getContentRaw(),
					// if the user takes more than a minute, time out
					1, TimeUnit.MINUTES, () -> event.reply("Sorry, you took too long."));
			
			event.reply("Your word is now " + word);
			
			wordArray.add(word);
		} else if (!wordArray.isEmpty()) {
			event.reply("There seems to already be a game running. If there isn't ask Jonathan to fix his Bot.");
		}

	}
	
	private void getHangman(String stage, String wordState) {
		
		EmbedBuilder hangMan = new EmbedBuilder();
		hangMan.setTitle("Hangman " + stage);
		hangMan.setImage("https://github.com/JonathanRiggins/CucumberRevived/blob/master/bin/Assets/Hangman/Base.png");
		hangMan.addField("Word", wordState, false);
	}
	
	private void getWordState(ArrayList<String> wordArray) {
		
	}

}
