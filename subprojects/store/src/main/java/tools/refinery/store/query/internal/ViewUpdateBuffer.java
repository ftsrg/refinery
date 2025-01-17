package tools.refinery.store.query.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tools.refinery.store.model.Tuple;

public class ViewUpdateBuffer<D> {
	protected final ViewUpdateTranslator<D> updateListener;
	protected final List<ViewUpdate> buffer = new ArrayList<>();
	
	public ViewUpdateBuffer(ViewUpdateTranslator<D> updateListener) {
		this.updateListener = updateListener;
	}
	
	public ViewUpdateTranslator<D> getUpdateListener() {
		return updateListener;
	}
	
	public boolean hasChange() {
		return ! buffer.isEmpty();
	}
	
	public void addChange(Tuple tuple, D oldValue, D newValue) {
		if(oldValue != newValue) {
			Object[] oldTuple = updateListener.isMatching(tuple, oldValue);
			Object[] newTuple = updateListener.isMatching(tuple, newValue);
			if(!Arrays.equals(oldTuple, newTuple)) {
				if(oldTuple != null) {
					buffer.add(new ViewUpdate(oldTuple, false));
				}
				if(newTuple != null) {
					buffer.add(new ViewUpdate(newTuple, true));
				}
			}
		}
	}
	
	public void flush() {
		for (ViewUpdate viewChange : buffer) {
			updateListener.processChange(viewChange);
		}
		buffer.clear();
	}
}
