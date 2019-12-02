package jda;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class Listener implements EventListener {

	private final Main main;

	private final EventWaiter waiter;

	public Listener(Main main, EventWaiter waiter) {
		this.main = main;
		this.waiter = waiter;
	}

	public void onEvent(GenericEvent event) {

		// Message Listener
		if (event instanceof MessageReceivedEvent) {

			Message message = ((MessageReceivedEvent) event).getMessage();

			// MyName dad joke handler
			if (message.getContentRaw().toLowerCase().contains("i'm ")) {
				main.getMyNameIsCommand().execute((MessageReceivedEvent) event);
			} else if (message.getContentRaw().toLowerCase().contains("im ")) {
				main.getMyNameIsCommand().execute((MessageReceivedEvent) event);
			} else if (message.getContentRaw().toLowerCase().contains("i am ")) {
				main.getMyNameIsCommand().execute((MessageReceivedEvent) event);
			}

			// Cucumber fact handler
			if (message.getContentRaw().toLowerCase().contains("cucumber")
					&& !message.getContentRaw().toLowerCase().startsWith("~")
					&& !message.getContentRaw().toLowerCase().startsWith("yes")) {
				main.getCucumberCommand().execute((MessageReceivedEvent) event, waiter);
			}
		}
	}

}
