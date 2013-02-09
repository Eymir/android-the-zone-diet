package com.thuf.thezone.objects;

import android.util.Log;

import com.thuf.thezone.tables.TableWoman;

public class Woman {
	private static final double CM_TO_INCH = 0.3937007874015748031496062992126;

	private float height;
	private float belly;
	private float hip;
	private boolean isMetric;

	private float fatsPercent;
	private float leanBodyMass;
	private float dailyBlocks;

	public Woman(float height, float belly, float hip, boolean isMetric) {
		this.height = height;
		this.belly = belly;
		this.hip = hip;
		this.isMetric = isMetric;

		this.fatsPercent = 0f;
		this.leanBodyMass = 0f;
		this.dailyBlocks = 0f;
	}

	public float getFatsPercent() {
		float fats = calculateFats();
		this.fatsPercent = fats;
		return fats;
	}

	public float getLeanBodyMass() {
		float leanBodyMass = this.height - (this.height * (this.fatsPercent / 100.0f));
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

	private float calculateFats() {
		if (this.isMetric) {
			convertToImperialSystem();
		}
		Log.d("ThuF", "Height before " + this.height);
		Log.d("ThuF", "Belly before " + this.belly);
		Log.d("ThuF", "Hip before " + this.hip);

		this.height = roundValue(this.height);
		this.belly = roundValue(this.belly);
		this.hip = roundValue(this.hip);

		Log.d("ThuF", "Height after " + this.height);
		Log.d("ThuF", "Belly after " + this.belly);
		Log.d("ThuF", "Hip after " + this.hip);

		float constantA = findConstantA(this.hip);
		float constantB = findConstantB(this.belly);
		float constantC = findConstantC(this.height);

		Log.d("ThuF", "Constant A " + constantA);
		Log.d("ThuF", "Constant B " + constantB);
		Log.d("ThuF", "Constant C " + constantC);

		float result = (constantA + constantB) - constantC;
		Log.d("ThuF", "Result " + result);
		return result;
	}

	private float findConstantA(float hip) {
		int index = (int) ((hip * 10 - 300) / 5);
		float result = TableWoman.CONSTANT_A[index];
		return result;
	}

	private float findConstantB(float belly) {
		int index = (int) ((belly * 10 - 200) / 5);
		float result = TableWoman.CONSTANT_B[index];
		return result;
	}

	private float findConstantC(float height) {
		int index = (int) ((height * 10 - 550) / 5);
		float result = TableWoman.CONSTANT_C[index];
		return result;
	}

	private void convertToImperialSystem() {
		this.height *= CM_TO_INCH;
		this.belly *= CM_TO_INCH;
		this.hip *= CM_TO_INCH;
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

}
