package sa.devming.whereru;

import android.app.Application;

import sa.devming.whereru.core.LockManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LockManager.getInstance().enableAppLock(this);
    }
}
