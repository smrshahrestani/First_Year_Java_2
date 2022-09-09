import java.util.Timer;
import java.util.TimerTask;

/**
 * Write a description of class Time here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Time
{
    // instance variables - replace the example below with your own
    int seconds= 0;
    int totalTime = 20;
    boolean timeOver = false;
    Timer timer = new Timer();
    TimerTask task = new TimerTask()
        {
            public void run()
            {
                seconds++;
            }
        };

    /**
     * Constructor for objects of class Time
     */
    public void startTime()
    {
        timer.scheduleAtFixedRate(task,1000,1000);

    }

    public void timeLeft()
    {
        int remainingTime=0;

        remainingTime= totalTime-seconds;
        System.out.println("the remainig time is: "+remainingTime+" seconds");

        
        System.out.println("Your time is over");
        if (timeOver)
        {
            isOver();
        }

    }

    public void isOver()
    {
        Game game = new Game();
        //game.timeUp();

    }

}
