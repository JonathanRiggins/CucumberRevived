package jda;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class Listener implements EventListener {
	
	private final Main main;

	public Listener(Main main) {
		this.main = main;
	}
	
	public void onEvent(GenericEvent event) {
		
		// Message Listener
		if (event instanceof MessageReceivedEvent) {
			
			Message messege = ((MessageReceivedEvent) event).getMessage();
			
			// Command Handlers
			if (messege.getContentRaw().toLowerCase().contains("i'm")) {
				main.getMyNameIsCommand().execute((MessageReceivedEvent) event);
			}
		}
	}

}
