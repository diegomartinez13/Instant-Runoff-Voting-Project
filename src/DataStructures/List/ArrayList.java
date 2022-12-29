package DataStructures.List;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class ArrayList<E> implements List<E> {
	//private attributes:
	private int currentSize;
	private E[] elements;
	
	@SuppressWarnings("hiding")
	private class ArrayListIterator<E> implements Iterator<E> {
		private int currentPosition;
		public ArrayListIterator() {
			super();
			this.currentPosition = 0;
		}
		@Override
		public boolean hasNext() {
			return this.currentPosition < currentSize;
		}
		@Override
		public E next() {
			if (this.hasNext()) {
				E result = (E) elements[this.currentPosition++]; // elements is array in enclosing class
				return result;
			}
			else {
				throw new NoSuchElementException();
			}
		}
	}
	
	
	
	/**Constructor:
	 * 
	 * Constructor of the class Array List
	 * @param initialCapacity - The initial capacity of the array list.
	 * */
	public ArrayList(int initialCapacity) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException("Capacity must be at least 1.");
		this.currentSize = 0;
		this.elements = (E[]) new Object[initialCapacity];
	}

	/**Add() method:
	 * 
	 * Adds a new element at the end of the array.
	 * @param obj - Object to be added to the array list.
	 * **/
	@Override
	public void add(E obj) {
		if(obj == null)
			throw new IllegalArgumentException("Object cannot be null.");
		else {
			if(this.size() == this.elements.length)
				reAllocate();
			this.elements[this.currentSize++] = obj;
		}
		
	}

	/**reAllocate method:
	 * 
	 * Creates a new array list with double the capacity of the old array, 
	 * and replaces the old element with the new elements.
	 * **/
	private void reAllocate() {
		// create a new array with twice the size
		E newElements[] = (E[]) new Object[2*this.elements.length];
		for (int i = 0; i < this.size(); i++)
			newElements[i] = this.elements[i];
		// replace old elements with newElements
		this.clear();
		this.elements = newElements;
	}
	
	/**Add(index, obj):
	 * 
	 * This method adds an object to a current position, if the position is not empty,
	 * it shifts the elements one position forward.
	 * 
	 * @param index - Index in witch position we want to add the object 
	 * @param obj- Object to be added 
	 * **/
	@Override
	public void add(int index, E obj) {
		if(obj == null)
			throw new IllegalArgumentException("Object cannot be null.");
		
		if (index == this.currentSize)
			this.add(obj); // Use other method to "append"
		else {
			if (index >= 0 && index < this.currentSize) {
				//check if there is room to add the object
				if (this.currentSize == this.elements.length)
					reAllocate();
				// move everybody one spot
				for (int i = this.currentSize; i > index; i--)
					this.elements[i] = this.elements[i - 1];
				// add element at position index
				this.elements[index] = obj;
				this.currentSize++;
			}
			else
				throw new ArrayIndexOutOfBoundsException();
		}
		
	}

	/**remove method:
	 * Removes from the array the current method
	 * 
	 * @param obj - object to be removed
	 * @return boolean value if the object was removed from the array
	 * **/
	@Override
	public boolean remove(E obj) {
		//check if obj is null
		if (obj == null)
			throw new IllegalArgumentException("Object cannot be null.");
		// first find obj in the array
		int position = this.firstIndex(obj);
		if (position >= 0) // found it
			return this.removePos(position);
		else
			return false;
	}

	/**remove by index method:
	 * Removes the element at a current position
	 * 
	 * @param index - index of element to be removed
	 * @return boolean value if the object was removed from the array in the current index
	 * **/
	@Override
	public boolean removePos(int index) {
		if (index >= 0 && index < this.currentSize) {
			// move everybody one spot to the front of the array
			for (int i = index; i < this.currentSize - 1; i++)
				this.elements[i] = this.elements[i + 1];
			this.elements[--this.currentSize] = null;
			return true;
		}
		else
			return false;
	}

	/**removeAll method:
	 * Removes all the elements that are equal to an object
	 * 
	 * @param obj - object that is going to be removed from all the list
	 * @return int. value of the number of elements removed 
	 * **/
	@Override
	public int removeAll(E obj) {
		int counter = 0;
		while (this.remove(obj))
			counter++;
		return counter;
	}

	/**get method:
	 * Returns an element at a current index
	 * 
	 * @param index - index of the object to be returned
	 * @return object at the current index
	 * **/
	@Override
	public E get(int index) {
		if (index >= 0 && index < this.size())
			return this.elements[index];
		else
			throw new ArrayIndexOutOfBoundsException();
	}
	
	/** Set an element at a given position
	 * 
	 * @param index - index where the object need to be set.
	 * @return object at the current index
	 * **/
	@Override
	public E set(int index, E obj) {
		if (obj == null)
			throw new IllegalArgumentException("Object cannot be null.");
		if (index >= 0 && index < this.size()) {
			E temp = this.elements[index];
			this.elements[index] = obj;
			return temp;
		}
		else
			throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Returns first element.
	 * @return first element in the array.**/
	@Override
	public E first() {
		if (this.isEmpty())
			return null;
		else
			return this.elements[0];
	}

	/**
	 * Returns last element.
	 * @return last element in the array.**/
	@Override
	public E last() {
		if (this.isEmpty())
			return null;
		else
			return this.elements[this.currentSize - 1];
	}

	/**
	 * Returns the index of the first element that equals to the parameter.
	 * @param obj - Element that we need to return the value of.
	 * @return i - index of the element if found in the array.
	 * @return -1 if no element found.
	 * **/
	@Override
	public int firstIndex(E obj) {
		for (int i = 0; i < this.size(); i++)
			if (this.elements[i].equals(obj))
				return i;
		return -1;
	}

	/**
	 * Returns the index of the last element that equals to the parameter.
	 * @param obj - Element that we need to return the value of.
	 * @return i - index of the element if found in the array.
	 * @return -1 if no element found.
	 * **/
	@Override
	public int lastIndex(E obj) {
		for (int i = this.size() - 1; i >= 0; i--)
			if (this.elements[i].equals(obj))
				return i;
		return -1;
	}

	/**
	 * Size of the array
	 * @return int size of the array.**/
	@Override
	public int size() {
		return this.currentSize;
	}

	/**Checks if the array is empty
	 * @return boolean - true if empty, false otherwise **/
	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	/**Checks if the element is in the array
	 * @param obj - Object that we want to find
	 * @return boolean - true if element in array, false otherwise **/
	@Override
	public boolean contains(E obj) {
		return this.firstIndex(obj) >= 0;
	}

	//Sets every element in the array to null and sets currentSize to zero;
	@Override
	public void clear() {
		for (int i = 0; i < this.currentSize; i++)
			this.elements[i] = null;
		this.currentSize = 0;
	}

	/**Returns the count of an element at a given arrayList
	 * @param listArray, s: List to be worked on, string to be found at the array.
	 * @return counter: Number of elements that matched the parameter s.
	 * **/
	public static int totalCount(List<String>[] listArray, String s) {
		int counter = 0;
		for(int i = 0; i < listArray.length; i++) {
			for(int j = 0; j < listArray[i].size(); j++) {
				if(listArray[i].get(j).equals(s)) {
					counter++;
				}
			}
		}
		return counter;
	}

	/**Replaces an element at a given position
	 * @param position, newElement: Position of the element we want to replace, element that we want to add.
	 * @return counter: Number of elements that matched the parameter s.
	 * **/
	@Override
	public E replace(int position, E newElement) {
		if ((position < 0) || (position >= this.currentSize)) {
			throw new IndexOutOfBoundsException("Illegal position");
		}
		E result = this.elements[position];
		this.elements[position] = newElement;
		return result;
	}

	/**Turns the arrayList to an array
	 * @return an array type Object**/
	public Object[] toArray() {
		Object[] result = new Object[this.size()];
		System.arraycopy(this.elements, 0, result, 0, this.size());
//		for (int i=0; i < this.size(); ++i) {
//			result[i] = this.elements[i];
//		}
		return result;
	}
	
	/**returns a new ArrayListIterator**/
	@Override
	public Iterator<E> iterator() {
		return new ArrayListIterator<E>();
	}
}
