package Commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CucumberFactsCommand extends Command {

	private final EventWaiter waiter;
	private String[] fact;

	public CucumberFactsCommand(EventWaiter waiter) {
		this.name = "CucumberFact";
		this.help = "Command to add a new fact";
		this.arguments = "add";
		this.waiter = waiter;
	}

	@Override
	protected void execute(CommandEvent event) {

		String[] args = event.getMessage().getContentRaw().split("\\s+");

		// Event specific information
		User author = event.getAuthor();
		Message message = event.getMessage();
		TextChannel channel = event.getTextChannel();
		File directory = new File("C:\\Users\\jonat\\eclipse-workspace\\CucumberRevived\\CucumberFacts.txt");

		if (args.length < 2) {
			channel.sendMessage("Proper usage is ~CucumberFacts add").queue();
		}

		if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {

			if (author.isBot()) {
				return;
			}

			if (message.getContentRaw().contains("add")) {
				try {
					channel.sendMessage("If you want to add a new fact please Type Yes followed by the fact").queue();

					waiter.waitForEvent(MessageReceivedEvent.class,
							// make sure it's by the same user, and in the same channel, and for safety, a
							// different message
							e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel())
									&& !e.getMessage().equals(event.getMessage()),
							// respond and set the args
							e -> {
								fact = e.getMessage().getContentRaw().split("\\s+", 2);
								event.getChannel().sendMessage("Added \"" + fact[1] + "\" to string list").queue();
								try (BufferedWriter bufferedwriter = new BufferedWriter(
										new FileWriter(directory.getAbsoluteFile(), true))) {
									bufferedwriter.newLine();
									bufferedwriter.write(fact[1].toString());
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							},
							// if the user takes more than a minute, time out
							1, TimeUnit.MINUTES,
							() -> event.getChannel().sendMessage("Sorry you took too long").queue());
				} catch (NullPointerException e3) {
					e3.printStackTrace();
				}
			}

		} else {
			channel.sendMessage("You need to be an admin to add facts").queue();
			return;
		}
	}

}
