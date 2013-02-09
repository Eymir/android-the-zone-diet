package com.thuf.thezone.objects;

import java.io.Serializable;

public class Profile implements Serializable {
	private static final long serialVersionUID = 3095754729391858713L;
	private long id;
	private String name;
	private int weight;
	private int height;
	private float waist;
	private float wrist;
	private boolean man;
	private boolean imperialSystem;

	public Profile(String name, int weight, int height, float waist, float wrist, boolean man,
			boolean imperialSystem) {
		this.name = name;
		this.weight = weight;
		this.height = height;
		this.waist = waist;
		this.wrist = wrist;
		this.man = man;
		this.imperialSystem = imperialSystem;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getWeight() {
		return weight;
	}

	public int getHeight() {
		return height;
	}

	public float getWaist() {
		return waist;
	}

	public float getWrist() {
		return wrist;
	}

	public boolean isMan() {
		return man;
	}

	public boolean isImperialSystem() {
		return imperialSystem;
	}
}
