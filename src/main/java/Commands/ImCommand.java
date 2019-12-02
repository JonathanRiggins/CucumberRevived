package Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ImCommand extends Command {

	public ImCommand() {
		this.name = "Wow i'm tired";
		this.help = "\"Hi, tired, i'm Cucumber\" this command respond to anyone that says i'm in a message";
	}

	@Override
	protected void execute(CommandEvent event) {
		event.getChannel().sendMessage("This command is not meant to be used. It runs automatically when someone says i'm in a message.").queue();
		return;
	}

}
