package net.darekasan.minjtwit.model;

import java.util.Collection;
import java.util.EventListener;

public interface ListChangeListener extends EventListener{
	public void add(Object o);
	public void addAll(Collection c);
}