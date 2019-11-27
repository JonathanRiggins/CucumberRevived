package Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class PingCommand extends Command {

	public PingCommand() {
		
		this.name = "ping";
		this.help = "pong";
		this.guildOnly = false;
	}

	@Override
	protected void execute(CommandEvent event) {
		
		// responds to event
		event.reply("Pong!");
	}

}
