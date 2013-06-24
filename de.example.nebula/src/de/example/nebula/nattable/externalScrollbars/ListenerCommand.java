package de.example.nebula.nattable.externalScrollbars;

import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;

public interface ListenerCommand {

	/**
	 * Execute command. The listener is triggered AFTER the command is executed inside the NatTable
	 * @param command
	 */
	public void doCommand(ILayerCommand command);
	
}
