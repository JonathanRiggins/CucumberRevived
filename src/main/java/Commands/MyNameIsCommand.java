package Commands;

import jda.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MyNameIsCommand {
	
	public MyNameIsCommand(Main main) {
		
	}

	public void execute(MessageReceivedEvent event) {

		JDA jda = event.getJDA();

		// Event specific information
		User author = event.getAuthor();
		Message message = event.getMessage();

		message.getContentDisplay();

		if (event.isFromType(ChannelType.TEXT)) {

			if (message.isWebhookMessage()) {

				author.getName();
			} else {
				
				// If Bot don't do anything
				if (author.isBot()) {
					return;
				}

				// Pull "Dad Name" from message
				String dadName = event.getMessage().getContentRaw().toLowerCase();
				String s = dadName.substring(dadName.lastIndexOf("i'm")).replaceFirst("i'm", "");

				// Send super witty reply
				event.getChannel().sendMessage("Hi," + s + " i'm " + jda.getSelfUser().getName()).queue();
			}
		} else if (event.isFromType(ChannelType.PRIVATE)) {
			
			// If Bot don't do anything
			if (author.isBot()) {
				return;
			}

			PrivateChannel privateChannel = event.getPrivateChannel();

			// Pull "Dad Name" from message
			String dadName = event.getMessage().getContentRaw().toLowerCase();
			String s = dadName.substring(dadName.lastIndexOf("i'm")).replaceFirst("i'm", "");

			// Send super witty reply
			privateChannel.sendMessage("Hi," + s + " i'm " + jda.getSelfUser().getName()).queue();
		}
	}
}
