package Commands;

import java.util.List;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class ClearCommand extends Command {

	public ClearCommand() {

		this.name = "clear";
		this.aliases = new String[] { "purge" };
		this.help = "Clears X number of messages";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {

		String[] args = event.getMessage().getContentRaw().split("\\s+");

		// If does not include amount
		if (args.length < 2) {
			// Usage embed
			EmbedBuilder usage = new EmbedBuilder();
			usage.setColor(0xff3923);
			usage.setTitle("Specify amount to delete");
			usage.setDescription("Usage: `" + "~" + "clear [# of messages]`");
			event.getChannel().sendMessage(usage.build()).queue();
		} else {
			try {
				// Delete specified number of messages
				List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1]) + 1)
						.complete();
				event.getChannel().purgeMessages(messages);

				// Success
				EmbedBuilder success = new EmbedBuilder();
				success.setColor(0x22ff2a);
				success.setTitle("Successfully deleted " + args[1] + " messages.");
				event.getChannel().sendMessage(success.build()).queue();

				// Wait 4 seconds
				Thread.sleep(4000);

				// Delete Success message
				List<Message> message = event.getChannel().getHistory().retrievePast(1).complete();
				event.getChannel().purgeMessages(message);
			} catch (IllegalArgumentException | InterruptedException e) {
				if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
					// Too many messages
					EmbedBuilder error = new EmbedBuilder();
					error.setColor(0xff3923);
					error.setTitle("Too many messages selected");
					error.setDescription("Between 1-100 messages can be deleted at one time.");
					event.getChannel().sendMessage(error.build()).queue();
				} else {
					// Messages too old
					EmbedBuilder error = new EmbedBuilder();
					error.setColor(0xff3923);
					error.setTitle("Selected messages are older than 2 weeks");
					error.setDescription("Messages older than 2 weeks cannot be deleted.");
					event.getChannel().sendMessage(error.build()).queue();
				}
			}
		}
	}
}