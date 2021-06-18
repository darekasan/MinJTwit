package net.darekasan.minjtwit.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.event.EventListenerList;

public class ExtendedList extends ArrayList {

	protected EventListenerList listenerList = new EventListenerList();

	public ExtendedList() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public ExtendedList(int initialCapacity) {
		super(initialCapacity);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public ExtendedList(Collection c) {
		super(c);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public void addListener(ListChangeListener l){
		this.listenerList.add(ListChangeListener.class,l);
	}

	public void removeListener(ListChangeListener l){
		this.listenerList.remove(ListChangeListener.class,l);
	}

	@Override
	public boolean add(Object e) {
		// TODO 自動生成されたメソッド・スタブ
		for (ListChangeListener listener : listenerList.getListeners(ListChangeListener.class)) {
			listener.add(e);
		}
		return super.add(e);
	}

	@Override
	public void add(int index, Object element) {
		// TODO 自動生成されたメソッド・スタブ
		for (ListChangeListener listener : listenerList.getListeners(ListChangeListener.class)) {
			listener.add(element);
		}
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection c) {
		for (ListChangeListener listener : listenerList.getListeners(ListChangeListener.class)) {
			listener.add(c);
		}
		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection c) {
		for (ListChangeListener listener : listenerList.getListeners(ListChangeListener.class)) {
			listener.add(c);
		}
		return super.addAll(index, c);
	}


}
