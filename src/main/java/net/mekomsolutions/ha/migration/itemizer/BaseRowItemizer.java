package net.mekomsolutions.ha.migration.itemizer;

import java.util.List;

import net.mekomsolutions.ha.migration.model.BaseEntity;

/**
 * Base class for classes that itemize an input data row to a list of entity objects that make up
 * the row.
 *
 * @param <T> the type to itemize
 */
public abstract class BaseRowItemizer<T> {
	
	/**
	 * Itemizes the specified row data
	 *
	 * @param rowData the row data to itemize
	 * @return a list of itemized entities
	 */
	public abstract List<BaseEntity> itemize(T rowData);
	
}
