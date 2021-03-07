import java.util.Timer;
import java.util.TimerTask;

public class Session {
    private long id;
    private Timer timer;
    private Account account;
    private static final int SESSION_LENGTH = 300; // This is the number of seconds the session should last
    private static final int UPDATE_TIME = 10; // This is the number of seconds between updates to time left
    private static final long[] SESSION_ID_RANGE = { 1L, 1000L };
    private boolean isActive;
    private int timeLeft;

    public Session(Account account)
    {
        // Associate the account with this session
        setAccount(account);

        // Create a random ID
        setId(SESSION_ID_RANGE[0] + (long) (Math.random() * (SESSION_ID_RANGE[1] - SESSION_ID_RANGE[0])));

        // Create a new timer with the specified session length
        // (* 1000 because the argument is in ms rather than seconds)
        timer = new Timer();
        setActive(true);
        setTimeLeft(SESSION_LENGTH * 1000);
        timer.schedule(new EndSessionTask(), SESSION_LENGTH * 1000);
        timer.schedule(new UpdateTimeLeftTask(), UPDATE_TIME * 1000);
    }

    // This task is called when the timer ends
    class EndSessionTask extends TimerTask {
        @Override
        public void run()
        {
            setActive(false);
            timer.cancel();
        }
    }

    class UpdateTimeLeftTask extends TimerTask {
        @Override
        public void run()
        {
            setTimeLeft(timeLeft - (UPDATE_TIME * 1000));
            timer.schedule(this, UPDATE_TIME * 1000);
        }
    }

    public int getTimeLeft()
    {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft)
    {
        this.timeLeft = timeLeft;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }
}