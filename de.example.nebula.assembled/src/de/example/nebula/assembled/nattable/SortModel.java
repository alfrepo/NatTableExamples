package de.example.nebula.assembled.nattable;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.sort.ISortModel;
import org.eclipse.nebula.widgets.nattable.sort.SortDirectionEnum;

/**
 * Dummy implementation of a sort model.
 * It should be implemented to do the sorting inside the table.
 * @author alf
 *
 */
public class SortModel implements ISortModel {
	
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SortModel.class);
	
	public SortModel(NatTable natTable) {
		// todo do something with the nattable
	}

	@Override
	public List<Integer> getSortedColumnIndexes() {
		LOG.debug("getSortedColumnIndexes");
		return null;
	}

	@Override
	public boolean isColumnIndexSorted(int columnIndex) {
		LOG.debug("isColumnIndexSorted");
		return false;
	}

	@Override
	public SortDirectionEnum getSortDirection(int columnIndex) {
		LOG.debug("getSortDirection");
		return null;
	}

	@Override
	public int getSortOrder(int columnIndex) {
		LOG.debug("columnIndex");
		return 0;
	}

	@Override
	public List<Comparator> getComparatorsForColumnIndex(int columnIndex) {
		LOG.debug("getComparatorsForColumnIndex");
		return null;
	}

	@Override
	public Comparator<?> getColumnComparator(int columnIndex) {
		LOG.debug("getColumnComparator");
		return null;
	}

	@Override
	public void sort(int columnIndex, SortDirectionEnum sortDirection, boolean accumulate) {
		LOG.debug("sort");
	}

	@Override
	public void clear() {
		LOG.debug("clear");
	}

}
