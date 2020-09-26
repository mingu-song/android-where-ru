package sa.devming.whereru;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import sa.devming.whereru.core.AppLock;
import sa.devming.whereru.core.AppLockActivity;
import sa.devming.whereru.core.BaseActivity;
import sa.devming.whereru.core.LockManager;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Switch passSwitch;
    private Switch soundSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        requester.create().request(
                Manifest.permission.RECEIVE_SMS,
                10000,
                new PermissionRequester.OnClickDenyButtonListener() {
                    @Override
                    public void onClick(Activity activity) {
                        Toast.makeText(MainActivity.this, "권한을 얻지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        initializeView();
    }

    private void initializeView() {
        passSwitch = (Switch)findViewById(R.id.passSwitch);
        soundSwitch = (Switch)findViewById(R.id.soundSwitch);

        passSwitch.setOnCheckedChangeListener(this);
        soundSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.equals(passSwitch)) {
            if (isChecked != LockManager.getInstance().getAppLock().isPasscodeSet()) {
                int type = LockManager.getInstance().getAppLock().isPasscodeSet() ? AppLock.DISABLE_PASSLOCK : AppLock.ENABLE_PASSLOCK;
                Intent intent = new Intent(this, AppLockActivity.class);
                intent.putExtra(AppLock.TYPE, type);
                startActivityForResult(intent, type);
                /*Intent intent = new Intent(this, AppLockActivity.class);
                intent.putExtra(AppLock.TYPE, AppLock.CHANGE_PASSWORD);
                intent.putExtra(AppLock.MESSAGE, getString(R.string.enter_old_passcode));
                startActivityForResult(intent, AppLock.CHANGE_PASSWORD);*/
            }
        } else if (buttonView.equals(soundSwitch)) {
            if (isChecked) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppLock.DISABLE_PASSLOCK:
                break;
            case AppLock.ENABLE_PASSLOCK:
            case AppLock.CHANGE_PASSWORD:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, getString(R.string.setup_passcode),Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        passSwitch.setChecked(LockManager.getInstance().getAppLock().isPasscodeSet());
    }
}
