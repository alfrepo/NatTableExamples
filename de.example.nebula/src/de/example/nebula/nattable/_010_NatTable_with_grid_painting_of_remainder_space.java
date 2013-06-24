package de.example.nebula.nattable;


import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.examples.AbstractNatExample;
import org.eclipse.nebula.widgets.nattable.examples.runner.StandaloneNatExampleRunner;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class _010_NatTable_with_grid_painting_of_remainder_space extends AbstractNatExample {
	
	public static void main(String[] args) throws Exception {
		StandaloneNatExampleRunner.run(800, 600, new _010_NatTable_with_grid_painting_of_remainder_space());
	}
	
	public Control createExampleControl(Composite parent) {
		NatTable natTable = new NatTable(parent);
		NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable);
		natTable.setLayerPainter(layerPainter);
		return natTable;
	}
	
}

