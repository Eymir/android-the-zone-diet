package com.thuf.thezone;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.view.View;

import com.thuf.thezone.database.TableProfileStatistics;
import com.thuf.thezone.database.ZoneDatabase;
import com.thuf.thezone.objects.Profile;

public class StatisticsActivity extends Activity implements TableProfileStatistics {
	private static final String EXTRA_STATS = "STATS";
	private ZoneDatabase database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Profile profile = (Profile) getIntent().getSerializableExtra("Profile");
		String statisticKind = getIntent().getStringExtra(EXTRA_STATS);
		database = new ZoneDatabase(this);
		float[] statistics = null;
		if (statisticKind.equalsIgnoreCase(COLUMN_WEIGHT_STATISTICS)) {
			statistics = database.getStatisticsForProfile(profile.getId(),
					COLUMN_WEIGHT_STATISTICS);
		} else if (statisticKind.equalsIgnoreCase(COLUMN_WAIST_STATISTICS)) {
			statistics = database.getStatisticsForProfile(profile.getId(),
					COLUMN_WAIST_STATISTICS);
		} else if (statisticKind.equalsIgnoreCase(COLUMN_WRIST_STATISTICS)) {
			statistics = database.getStatisticsForProfile(profile.getId(),
					COLUMN_WRIST_STATISTICS);
		}
		if (statistics == null) {
			setContentView(new View(this));
			return;
		}

		if (statistics.length > 7) {
			setContentView(new MyRatingView(this, getLastSeven(statistics),
					!profile.isImperialSystem(), statisticKind));
		} else {
			setContentView(new MyRatingView(this, statistics, true, statisticKind));
		}
	}

	private float[] getLastSeven(float[] statistics) {
		int length = statistics.length - 7;
		float[] result = new float[7];
		for (int i = 0; i < 7; i++) {
			result[i] = statistics[length + i];
		}
		return result;
	}

	@Override
	public void finish() {
		database.close();
		super.finish();
	}
}

class MyRatingView extends View {
	private float[] values;
	private boolean metricSystem;
	private String text;
	private Paint paint;
	private Path path;
	private Random rand;

	private MyRatingView(Context context) {
		super(context);
	}

	public MyRatingView(Context context, float[] values, boolean metricSystem, String text) {
		super(context);
		this.values = values;
		this.metricSystem = metricSystem;
		this.text = text;
		this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.path = new Path();
		this.rand = new Random();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float width = getWidth();
		float height = getHeight();
		float dp = getResources().getDisplayMetrics().density;
		float sp = getResources().getDisplayMetrics().scaledDensity;

		paint.setStyle(Style.FILL);
		paint.setARGB(160, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		paint.setStrokeWidth(2);
		paint.setTextSize(22 * sp);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSkewX(-0.25f);

		String headLine = "Last " + this.text + " changes";
		canvas.drawText(headLine, width / 2, height * 0.1f, paint);

		paint.setTextAlign(Align.LEFT);
		paint.setTextSize(18 * sp);
		paint.setTextSkewX(0);

		float scaleFactorY = 1.0f;
		if (this.metricSystem) {
			scaleFactorY = 2.5f;
		} else {
			scaleFactorY = 1.25f;
		}

		int count = this.values.length;
		float columnWidth = width / count;
		for (int i = 0; i < count; i++) {
			float startX = i * columnWidth;
			float stopX = (i + 1) * columnWidth;
			float startY = height - this.values[i] * scaleFactorY * dp;
			path.addRect(startX, startY, stopX, height, Direction.CW);
			canvas.drawText(" " + this.values[i], startX, startY - 5, paint);
		}

		int red = rand.nextInt(255);
		int green = rand.nextInt(255);
		int blue = rand.nextInt(255);
		paint.setStyle(Style.FILL);
		paint.setARGB(50, red, green, blue);
		canvas.drawPath(path, paint);

		paint.setStyle(Style.STROKE);
		paint.setARGB(255, red, green, blue);
		paint.setStrokeWidth(1);
		canvas.drawPath(path, paint);
	}
}