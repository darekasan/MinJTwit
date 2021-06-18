package net.darekasan.minjtwit.model;

import twitter4j.Status;

public class MStatus {
	Status status;
	boolean fromhome;
	boolean fromother;
	boolean isDeleated;


	public MStatus(Status s,boolean home,boolean other){
		status=s;
		status.getId();
		fromhome=home;
		fromother=other;
	}

	public void setIsHome(boolean b){
		fromhome=b;
	}

	public void setIsOther(boolean b){
		fromhome=b;
	}

	public boolean getIsHome(){
		return fromhome;
	}

	public boolean getIsOther(){
		return fromother;
	}

	public Status getStatus(){
		return status;
	}

	public long getId(){
		return status.getId();
	}

	public boolean getIsDeleted(){
		return isDeleated;
	}

	public void setIsDeleated(boolean value){
		isDeleated=value;
	}
}
