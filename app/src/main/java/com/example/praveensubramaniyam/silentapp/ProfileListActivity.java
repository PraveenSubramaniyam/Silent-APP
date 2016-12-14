
package com.example.praveensubramaniyam.silentapp;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Name: ProfileListActivity
 * Java Class to display all the profiles created
 *
 */
public class ProfileListActivity extends AppCompatActivity implements SensorEventListener {
    private List<String> mAppList;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    private DataBaseHelper db;
    private String TAG="Silent APP";
    private SharedPreferences mSettings;
    private SilentAppSettings mSilentAppSettings;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ProfileTableValues profileTableValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mSilentAppSettings = new SilentAppSettings(mSettings);


        mAppList = new ArrayList<String>();

        db = new DataBaseHelper(getApplicationContext());
        profileTableValues = db.getProfileTableValues(null);


        if(profileTableValues.getProfileNames() != null) {
            for (String temp : profileTableValues.getProfileNames()) {
                mAppList.add(temp);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        mListView = (SwipeMenuListView) findViewById(R.id.listView);

        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setTitle("-");
                deleteItem.setTitleSize(50);
                deleteItem.setTitleColor(Color.BLACK);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String item = mAppList.get(position);
                Log.i(TAG,"Delete Clicked: "+index);
                switch (index) {
                    case 0:
                        mAppList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        db = new DataBaseHelper(getApplicationContext());

                        db.deleteProfile(profileTableValues.getProfileId().get(position));
                        break;
                }
                return false;
            }
        });

        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }
            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }
            @Override
            public void onMenuClose(int position) {
            }
        });

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                String item = mAppList.get(position);
                Intent intent= new Intent(ProfileListActivity.this,ProfileAddScreen.class);
                intent.putExtra("ProfileName", item);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    private void startGPSService() {
        Log.i(TAG, "[SERVICE] Start");
        startService(new Intent(ProfileListActivity.this,
                GpsService.class));
    }

    private void bindGpsService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(ProfileListActivity.this,
                GpsService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindGpsService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }


    public void checkAndStartGPSService()
    {
        ProfileTableValues profileTableValues;
        db = new DataBaseHelper(getApplicationContext());
        profileTableValues = db.getCoordinates();

        if(profileTableValues != null)
        {
            if(profileTableValues.getCoordinates() != null) {
                if(!mSilentAppSettings.isServiceRunning())
                {
                    startGPSService();
                    bindGpsService();
                }
            }
        }

    }

    private GpsService mService;

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 0) {
            //Near
            Log.i(TAG,"Phone near to hand");
            Intent intent= new Intent(ProfileListActivity.this,ProfileAddScreen.class);
            startActivity(intent);
            finish();
        } else {
            //Far
            Log.i(TAG,"Phone far to hand");
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((GpsService.StepBinder)service).getService();
            Log.i(TAG,"Service connected");
        }
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            Log.i(TAG,"Service unconnected");
        }
    };

    private void stopStepService() {
        Log.i(TAG, "[SERVICE] Stop");
        if (mService != null) {
            Log.i(TAG, "[SERVICE] stopService");
            stopService(new Intent(ProfileListActivity.this,
                    GpsService.class));
        }
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        Log.i(TAG,"UnRegistering for Proximity Sensor");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "[ACTIVITY] onResume");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(TAG,"Registering for Proximity Sensor");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.i(TAG, "[ACTIVITY] Permission granted");

                } else {

                }
                return;
            }
        }
    }


    class AppAdapter extends BaseSwipListAdapter {

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public String getItem(int position) {
            System.out.println("item name :"+mAppList.get(position));
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            String item = getItem(position);
            holder.tv_name.setText(item);
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProfileListActivity.this, "iv_icon_click", Toast.LENGTH_SHORT).show();
                }
            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProfileListActivity.this,"iv_icon_click",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }

        @Override
        public boolean getSwipEnableByPosition(int position) {
            return true;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_name) {
            System.out.println("Menu Plus clicked");
            Intent intent= new Intent(ProfileListActivity.this,ProfileAddScreen.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "[SERVICE] onDestroy");
        super.onDestroy();

    }
}
