import java.util.Timer;
import java.util.TimerTask;

public class Session {
    private long id;
    private Timer timer;
    private Account account;
    private static final int SESSION_LENGTH = 300;
    private boolean isActive;

    public Session(Account account)
    {
        setAccount(account);
        setId((long) Math.random());
        timer = new Timer();
        setActive(true);
        timer.schedule(new EndSessionTask(), SESSION_LENGTH * 1000);
    }

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

    public Timer getTimer()
    {
        return timer;
    }

    public void setTimer(Timer timer)
    {
        this.timer = timer;
    }
}