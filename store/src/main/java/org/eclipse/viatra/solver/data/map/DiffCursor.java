package org.eclipse.viatra.solver.data.map;

public interface DiffCursor<K, V> extends Cursor<K,V> {
	public V getFromValue();
	public V getToValue();
}