import java.util.Timer;
import java.util.TimerTask;

public class Session {
    private long id;
    private Timer timer;
    private Account account;
    private static int SESSION_LENGTH = 300;

    public Session(Account account)
    {
        setAccount(account);
        setId((long) Math.random());
        timer = new Timer();
        timer.schedule(new EndSessionTask(), SESSION_LENGTH);
    }

    class EndSessionTask extends TimerTask {
        @Override
        public void run()
        {
            timer.cancel();
        }
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