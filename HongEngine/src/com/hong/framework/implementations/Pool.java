package com.hong.framework.implementations;

import java.util.ArrayList;
import java.util.List;

/*
 * recycle TouchEvents in order to improve
 * efficiency
 */
public class Pool<T> {
	private final List<T> freeObjects; // list of objects to reuse
	private final PoolObjectFactory<T> factory; // create new object
	private final int maxSize;

	interface PoolObjectFactory<T> {
		public T createObject();
	}

	public Pool(PoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}

	public T newObject() {
		/*
		 * if no object exists create new object, else, reuse last object
		 */
		T object = null;
		if (freeObjects.isEmpty()) {
			object = factory.createObject();
		} else {
			object = freeObjects.remove(freeObjects.size() - 1);
		}
		return object;
	}

	public void free(T object) {
		// add object to pool
		if (freeObjects.size() < maxSize) {
			freeObjects.add(object);
		}
	}
}
