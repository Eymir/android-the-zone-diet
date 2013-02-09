package com.thuf.thezone.objects;

import com.thuf.thezone.tables.TableMan;

public class Man {
	private static final double KG_TO_POUND = 2.2075055187637969094922737306843;
	private static final double CM_TO_INCH = 0.3937007874015748031496062992126;

	private int weight;
	private float waist;
	private float wrist;
	private boolean isMetric;

	private float fatsPercent;
	private float leanBodyMass;
	private float dailyBlocks;

	public Man(int weight, float waist, float wrist, boolean isMetric) {
		this.weight = weight;
		this.waist = waist;
		this.wrist = wrist;
		this.isMetric = isMetric;

		this.fatsPercent = 0f;
		this.leanBodyMass = 0f;
		this.dailyBlocks = 0f;
	}

	public int getFatsPercent() {
		int fats = calculateFats();
		this.fatsPercent = fats;
		return fats;
	}

	public float getLeanBodyMass() {
		float leanBodyMass = this.weight - (this.weight * (this.fatsPercent / 100.0f));
		leanBodyMass = roundValue(leanBodyMass);
		this.leanBodyMass = leanBodyMass;
		return leanBodyMass;
	}

	public float getDailyBlocks(float physicalActivity) {
		float dailyBlocks = ((this.leanBodyMass / 7.0f) * physicalActivity);
		dailyBlocks = roundValue(dailyBlocks);
		this.dailyBlocks = dailyBlocks;
		return this.dailyBlocks;
	}

	private int calculateFats() {
		if (this.isMetric) {
			convertToImperialSystem();
		}
		this.weight = (int) roundValue(this.weight);
		float difference = (this.waist - this.wrist);
		difference = roundValue(difference);

		int row = calcRow();
		int col = calcCol(difference);
		int fats = TableMan.FATS_PERCENT[row][col];
		return fats;
	}

	private void convertToImperialSystem() {
		this.weight *= KG_TO_POUND;
		this.waist *= CM_TO_INCH;
		this.wrist *= CM_TO_INCH;
	}

	private float roundValue(float value) {
		float rounded = value;
		String strValue = Double.toString(value);
		int dotPosition = strValue.indexOf(".");
		if (dotPosition != -1) {
			String strBeforeDotDigets = strValue.substring(0, dotPosition);
			float beforeDotDigets = Float.parseFloat(strBeforeDotDigets);
			strValue = strValue.substring(0, dotPosition + 2);
			String strAfterDotDiget = strValue.substring(dotPosition + 1, dotPosition + 2);
			float afterDotDiget = Float.parseFloat(strAfterDotDiget);
			if (afterDotDiget > 5) {
				beforeDotDigets++;
			} else if (afterDotDiget == 5) {
				beforeDotDigets = beforeDotDigets + (afterDotDiget / 10.0f);
			}

			rounded = beforeDotDigets;
		}
		return rounded;
	}

	private int calcRow() {
		int row = (int) (((this.weight * 10.0) - 1200.0) / 50.0);
		return row;
	}

	private int calcCol(double difference) {
		int col = (int) ((difference * 10.0) - 220.0) / 5;
		return col;
	}
}
