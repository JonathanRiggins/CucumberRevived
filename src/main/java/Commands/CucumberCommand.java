package Commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import jda.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CucumberCommand {

	public CucumberCommand(Main main) {
	}

	public void execute(MessageReceivedEvent event, EventWaiter waiter) {

		Random rand = new Random();

		String directory = "C:\\Users\\jonat\\eclipse-workspace\\CucumberRevived\\CucumberFacts.txt";

		// Event specific information
		User author = event.getAuthor();
		Message message = event.getMessage();
		TextChannel channel = event.getTextChannel();
		List<String> facts = new ArrayList<String>();

		message.getContentDisplay();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(directory))) {
			String line = bufferedReader.readLine();
			while (line != null) {
				facts.add(line);
				line = bufferedReader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (author.isBot()) {
			return;
		}

		int lines = facts.size();
		int randomNumber = rand.nextInt(lines);
		System.out.print(facts.get(randomNumber));
		channel.sendMessage(facts.get(randomNumber)).queue();
		return;

	}

}
