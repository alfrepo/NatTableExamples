package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.AggregateConfiguration;
import org.eclipse.nebula.widgets.nattable.print.config.DefaultPrintBindings;

public class ConfigurationsPrinting extends AggregateConfiguration {

    public ConfigurationsPrinting() {
        addPrintUIBindings();
    }

    protected final void addPrintUIBindings() {
        addConfiguration(new DefaultPrintBindings());
    }
}
