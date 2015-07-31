package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.config.AggregateConfiguration;
import org.eclipse.nebula.widgets.nattable.export.config.DefaultExportBindings;

public class ConfigurationsExport extends AggregateConfiguration {

    public ConfigurationsExport() {
        addExcelExportUIBindings();
    }

    protected final void addExcelExportUIBindings() {
        addConfiguration(new DefaultExportBindings());
    }

}
