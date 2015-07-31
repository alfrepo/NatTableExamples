package de.example.nebula.assembled.nattable.configurations;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.sort.ISortModel;


public class ConfigurationAll {

    public static void addCustomConfiguration(NatTable natTable, GridLayer gridLayer, ISortModel sortModel) {
        // HEADER
        // style the header
        natTable.addConfiguration(new ConfigurationStyledColumnHeader());
        // addConfiguration(new StyledRowHeaderConfiguration());

        // header context menu ()
        natTable.addConfiguration(new ConfigurationHeaderContextMenu(natTable, sortModel));

        // header mouse actions
        natTable.addConfiguration(new ConfigurationHeaderActions());

        // style the Filtered row
        natTable.addConfiguration(new ConfigurationStyleFilterRow());

        // BODY
        // alternating row color
        natTable.addConfiguration(new ConfigurationStyleRowAndCell(gridLayer));

        // default cell painters, padding, bgColor
        natTable.addConfiguration(new ConfigurationStylesBody());

        gridLayer.addConfiguration(new ConfigurationEditing());

        gridLayer.addConfiguration(new ConfigurationsPrinting());

        gridLayer.addConfiguration(new ConfigurationsExport());

//        gridLayer.addConfiguration(new ConfigurationFilterRow(dataColumnProvider, tableResolver));
    }
}
