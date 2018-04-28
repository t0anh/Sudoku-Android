package android.hfad.com.sudoku;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;


public class Timer {
    private Handler handler;
    private Runnable runnable;
    private int elapsedTime;
    private Context context;
    private TextView timeText;

    public Timer(Context context, int elapsedTime) {
        this.context = context;
        this.elapsedTime = elapsedTime;
        init();
    }
    public void start () {
        runnable.run();
    }
    public void stop () {
        handler.removeCallbacks(runnable);
    }
    private void init () {
        timeText = ((Activity) context).findViewById(R.id.txt_elapsed_time);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                elapsedTime += 1;
                handler.postDelayed(runnable, 1000);
                timeText.setText(getElapsedTimeString());
            }
        };
    }

    public int getElapsedSeconds() {
        return elapsedTime;
    }

    public String getElapsedTimeString () {
        int seconds = elapsedTime % 60;
        int minutes = elapsedTime / 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    static public String getTimeFormat (int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        if(hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }
}
