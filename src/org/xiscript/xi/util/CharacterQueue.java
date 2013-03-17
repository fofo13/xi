package org.xiscript.xi.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class CharacterQueue implements Queue<Character> {

	private CharSequence source;
	private int index;

	public CharacterQueue(CharSequence source) {
		this.source = source;
		index = 0;
	}

	@Override
	public boolean offer(Character e) {
		source = (new StringBuilder(source)).append(e);
		return true;
	}

	@Override
	public Character remove() {
		if (index >= source.length())
			throw new NoSuchElementException();

		return source.charAt(index++);
	}

	@Override
	public Character poll() {
		if (index >= source.length())
			return null;

		return source.charAt(index++);
	}

	@Override
	public Character element() {
		if (index >= source.length())
			throw new NoSuchElementException();

		return source.charAt(index);
	}

	@Override
	public Character peek() {
		if (index >= source.length())
			return null;

		return source.charAt(index);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		char arg = (Character) o;
		for (char c : this)
			if (c == arg)
				return true;
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> col) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Character> col) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return source.length() - index;
	}

	@Override
	public Iterator<Character> iterator() {
		return new Iterator<Character>() {
			private int i = index;

			@Override
			public Character next() {
				return source.charAt(i++);
			}

			@Override
			public boolean hasNext() {
				return i <= source.length();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public boolean removeAll(Collection<?> col) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> col) {
		for (Object o : col)
			if (!contains(o))
				return false;
		return true;
	}

	@Override
	public boolean add(Character e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Character[] toArray() {
		Character[] arr = new Character[size()];
		for (int i = 0; i < arr.length; i++)
			arr[i] = source.charAt(index + i);

		return arr;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (size() > a.length)
			return (T[]) toArray();

		System.arraycopy(toArray(), index, a, 0, size());
		return a;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

}