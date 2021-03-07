import java.util.Timer;
import java.util.TimerTask;

public class Session {
    private long id;
    private Timer timer;
    private Account account;
    private static final int SESSION_LENGTH = 300; // This is the number of seconds the session should last
    private boolean isActive;

    public Session(Account account)
    {
        // Associate the account with this session
        setAccount(account);

        // Create a random ID
        setId((long) Math.random());

        // Create a new timer with the specified session length
        // (* 1000 because the argument is in ms rather than seconds)
        timer = new Timer();
        setActive(true);
        timer.schedule(new EndSessionTask(), SESSION_LENGTH * 1000);
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