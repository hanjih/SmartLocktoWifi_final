package com.example.han.smartlocktowifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.smartlocktowifi.interfaces.ConnectionResultListener;
import com.example.han.smartlocktowifi.interfaces.WifiStateListener;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity {

    private PopupWindow mPopupWindow;
    private View popupView;
    private ImageButton btn_Popup1;
    private ImageButton btn_Popup2;
    private ImageButton btn_Popup3;
    private int tmp_image;
    private EditText ed;
    private TextView tv_tmp;
    private ImageButton ib_tmp;
    private Intent intent;
    public GPSlng gpslng = new GPSlng();
    public String curlat;
    public String curlong;


    //저장용 변수
    public static String f_ssid;
    public static String f_pass;
    public static LatLng save_position;
    public static String latitude;
    public static String longitude;


    private EditText wifissid;
    private EditText wifipass;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Popup1 = (ImageButton) findViewById(R.id.place_setting1);
        btn_Popup2 = (ImageButton) findViewById(R.id.place_setting2);
        btn_Popup3 = (ImageButton) findViewById(R.id.place_setting3);
        wifissid = (EditText) findViewById(R.id.wifissid);
        wifipass = (EditText) findViewById(R.id.wifipass);
        tv = (TextView) findViewById(R.id.wifi_1);


        SharedPreferences settings = getSharedPreferences(f_ssid, 0);
        f_ssid = settings.getString("f_ssid", "");
        SharedPreferences settings_pass = getSharedPreferences(f_pass, 0);
        f_ssid = settings.getString("f_pass", "");
        SharedPreferences settings_lat = getSharedPreferences(latitude, 0);
        latitude = settings_lat.getString("latitude", "");
        SharedPreferences settings_long = getSharedPreferences(longitude, 0);
        longitude = settings_long.getString("longitude", "");

//        SharedPreferences settings_save_position = getSharedPreferences(save_position,0);
//        f_ssid = settings.getString("save_position","");

    }

    @Override
    protected void onStop() {
        super.onStop();

//        //저장 셋팅
//        SharedPreferences settings = getSharedPreferences(f_ssid, 0);
//        SharedPreferences settings_pass = getSharedPreferences(f_pass, 0);
//        SharedPreferences settings_lat = getSharedPreferences(latitude, 0);
//        SharedPreferences settings_long = getSharedPreferences(longitude, 0);
//        SharedPreferences.Editor editor = settings.edit();
//        SharedPreferences.Editor editor2 = settings_pass.edit();
//        SharedPreferences.Editor editor3 = settings_lat.edit();
//        SharedPreferences.Editor editor4 = settings_long.edit();
//
//        //저장할 변수들 불러옴..
//        f_ssid = wifissid.getText().toString();
//        f_pass = wifipass.getText().toString();
//        save_position = GPSlng.getlng();
//        latitude = Double.toString(save_position.latitude);
//        longitude = Double.toString(save_position.longitude);
//
//        //저장
//        editor.putString("f_ssid", f_ssid);
//        editor2.putString("f_pass", f_pass);
//        editor3.putString("latitude", latitude);
//        editor4.putString("longitude", longitude);

    }


    public void menu_open(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.setting_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_setting:
                                intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.menu_help:

                                break;
                            case R.id.menu_info:
                                intent = new Intent(MainActivity.this, InfomationActivity.class);
                                startActivity(intent);
                                break;
                        }
                        Toast.makeText(getApplicationContext(), "클릭된 팝업 메뉴" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
        popup.show();
    }

    public void wifi_setting_onclick(View v) {
        popupView = getLayoutInflater().inflate(R.layout.place_popup_window, null);
        mPopupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(1);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        switch (v.getId()) {
            case R.id.place_setting1:
                tmp_image = 1;
                break;
            case R.id.place_setting2:
                tmp_image = 2;
                break;
            case R.id.place_setting3:
                tmp_image = 3;
                break;
            case R.id.txtv1:
                tmp_image = 1;
                break;
            case R.id.txtv2:
                tmp_image = 2;
                break;
            case R.id.txtv3:
                tmp_image = 3;
                break;
            default:
                break;
        }
    }

    public void wifi_setting_image(View v) {

        switch (v.getId()) {
            case R.id.home:
                if (tmp_image == 1) {
                    tv_tmp = (TextView) findViewById(R.id.txtv1);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup1.setImageResource(R.drawable.home);
                    btn_Popup1.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else if (tmp_image == 2) {
                    tv_tmp = (TextView) findViewById(R.id.txtv2);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup2.setImageResource(R.drawable.home);
                    btn_Popup2.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else if (tmp_image == 3) {
                    tv_tmp = (TextView) findViewById(R.id.txtv3);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup3.setImageResource(R.drawable.home);
                    btn_Popup3.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else
                    break;

            case R.id.office:

                if (tmp_image == 1) {
                    tv_tmp = (TextView) findViewById(R.id.txtv1);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup1.setImageResource(R.drawable.office);
                    btn_Popup1.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else if (tmp_image == 2) {
                    tv_tmp = (TextView) findViewById(R.id.txtv2);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup2.setImageResource(R.drawable.office);
                    btn_Popup2.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else if (tmp_image == 3) {
                    tv_tmp = (TextView) findViewById(R.id.txtv3);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup3.setImageResource(R.drawable.office);
                    btn_Popup3.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else
                    break;
            case R.id.school:

                if (tmp_image == 1) {
                    tv_tmp = (TextView) findViewById(R.id.txtv1);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup1.setImageResource(R.drawable.school);
                    btn_Popup1.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else if (tmp_image == 2) {
                    tv_tmp = (TextView) findViewById(R.id.txtv2);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup2.setImageResource(R.drawable.school);
                    btn_Popup2.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else if (tmp_image == 3) {
                    tv_tmp = (TextView) findViewById(R.id.txtv3);
                    tv_tmp.setVisibility(View.GONE);
                    btn_Popup3.setImageResource(R.drawable.school);
                    btn_Popup3.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else
                    break;

            case R.id.image_to_text:

                if (tmp_image == 1) {
                    ib_tmp = (ImageButton) findViewById(R.id.place_setting1);
                    ib_tmp.setVisibility(View.GONE);
                    TextView tv1 = (TextView) findViewById(R.id.txtv1);
                    ed = (EditText) popupView.findViewById(R.id.image_to_textedit);
                    String str = ed.getText().toString();
                    tv1.setText(str);
                    tv1.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else if (tmp_image == 2) {
                    ib_tmp = (ImageButton) findViewById(R.id.place_setting2);
                    ib_tmp.setVisibility(View.GONE);
                    TextView tv2 = (TextView) findViewById(R.id.txtv2);
                    ed = (EditText) popupView.findViewById(R.id.image_to_textedit);
                    String str = ed.getText().toString();
                    tv2.setText(str);
                    tv2.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else if (tmp_image == 3) {
                    ib_tmp = (ImageButton) findViewById(R.id.place_setting3);
                    ib_tmp.setVisibility(View.GONE);
                    TextView tv3 = (TextView) findViewById(R.id.txtv3);
                    ed = (EditText) popupView.findViewById(R.id.image_to_textedit);
                    String str = ed.getText().toString();
                    tv3.setText(str);
                    tv3.setVisibility(View.VISIBLE);
                    mPopupWindow.dismiss();
                    break;
                } else
                    mPopupWindow.dismiss();
                break;

            default:
                break;
        }
    }


    public void wifi_setting(View v) {

        popupView = getLayoutInflater().inflate(R.layout.wifi_popup_window, null);
        mPopupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(1);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        switch (v.getId()) {
            case R.id.wifi_1:
                tmp_image = 1;
                break;
            case R.id.wifi_2:
                tmp_image = 2;
                break;
            case R.id.wifi_3:
                tmp_image = 3;
                break;

            default:
                break;
        }


//        //btn_connect 클릭 시
//        String ssid = f_ssid.getText().toString();
//        String pw = f_pw.getText().toString();
//
//        //위치 저장 코드 넣어야함!!
//
//        double lat = 35.1339294;
//        double lon = 129.1052006;
//
//
//
//
//        connectWifi(ssid, pw);


//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        boolean isWifiConn = networkInfo.isConnected();
//        Toast.makeText(getApplicationContext(), "클릭된 팝업 메뉴" + networkInfo, Toast.LENGTH_SHORT).show();
//
//        if (!isWifiConn) {
//            String networkSSID = "KING";
//            String networkPass = "gkswlgns";
//
//            WifiConfiguration conf = new WifiConfiguration();
//            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
//
//            conf.wepKeys[0] = "\"" + networkPass + "\"";
//            conf.wepTxKeyIndex = 0;
//            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//            //WEP구분 중요!!
//
//            conf.preSharedKey = "\"" + networkPass + "\""; // WPA 네트워크에 필요한 키
//
//            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  //Open 네트워크에 필요함
//
//            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//            wifiManager.addNetwork(conf);
//
//            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
//            for (WifiConfiguration i : list) {
//                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
//                    wifiManager.disconnect();
//                    wifiManager.enableNetwork(i.networkId, true);
//                    wifiManager.reconnect();
//
//                    break;
//                }
//            }
//
//            //WEP의 경우 암호가 16 진수이면 따옴표로 묶을 필요가 없습니다.
//
//        } //백그라운드에서 실행하도록만들어야함!!!
    }


    public void wifi_setting_con(View v) {
        f_ssid = "제발 성공 해라";
        f_pass = "jbjbjbjb";
//        wifissid=(EditText)findViewById(R.id.wifissid);
//        wifipass=(EditText)findViewById(R.id.wifipass);
//        String im_ssid = wifissid.getText().toString();
//        String im_pass = wifipass.getText().toString();
//        f_ssid = im_ssid;
//        f_pass = im_pass;
//        f_pass = wifipass.getText().toString();
//        교수님 이 부분 진짜 하다가 도저히 못하겠어서 포기하고 이렇게 설정했습니다. EditText에서 문자를 받아오는게 다른어플에서는 이렇게 하면 잘되는데 여기서만 유독안되네요 ..
//        다른 폰으로도 실행이안되고 안드로이드에 졌습니다...
        save_data();

        tv.setText(f_ssid);
        mPopupWindow.dismiss();

    }

    public void connect_final(View v) {
        Toast.makeText(getApplicationContext(), "시작", Toast.LENGTH_SHORT).show();
        connectWifi(f_ssid, f_pass);
    }


    public void save_data() {
        //저장 셋팅
        SharedPreferences settings = getSharedPreferences(f_ssid, 0);
        SharedPreferences settings_pass = getSharedPreferences(f_pass, 0);
        SharedPreferences settings_lat = getSharedPreferences(latitude, 0);
        SharedPreferences settings_long = getSharedPreferences(longitude, 0);
        SharedPreferences.Editor editor = settings.edit();
        SharedPreferences.Editor editor2 = settings_pass.edit();
        SharedPreferences.Editor editor3 = settings_lat.edit();
        SharedPreferences.Editor editor4 = settings_long.edit();


        //저장할 변수들 불러옴..

//        f_ssid = wifissid.getText().toString();
//        f_pass = wifipass.getText().toString();
        save_position = GPSlng.getlng();
        latitude = Double.toString(save_position.latitude);
        longitude = Double.toString(save_position.longitude);

        //저장
        editor.putString("f_ssid", f_ssid);
        editor2.putString("f_pass", f_pass);
        editor3.putString("latitude", latitude);
        editor4.putString("longitude", longitude);

        Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();

    }

    private void connectWifi(String ssid, String pw) {
//        ssid = "제발 성공 해라";
//        pw = "jbjbjb";

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        WDLocationListener locationListener = new WDLocationListener();

        try {
            //5초 단위로 10미터 이상 차이 발생 시 위치 정보 업데이트.ㅇㅇ
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 333, 0, locationListener); //와아파이의 위치정보
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 555, 0, locationListener);     //GPS 위치정보

            Double com1 = Double.parseDouble(latitude);
//            Double com2 = Double.parseDouble(curlat);
            Double com3 = Double.parseDouble(longitude);
//            Double com4 = Double.parseDouble(curlong);

            double a = Math.round(com1 * 100d) / 100d;
//            double b = Math.round(com2 * 100d) / 100d;
            double c = Math.round(com3 * 100d) / 100d;
//            double d = Math.round(com4 * 100d) / 100d;
            //소수점 둘째자리까지 위도경도 반올림해서 위치 차이를 근소하게 만듬, 현재위치와 저장된 위치와의 차이를 위도 경도의 반올림정도로 설정
            //이렇게 비교하려고 했으나 curlat는 실행하면 자꾸 에러 뜨고 폰이 꺼져서 도저히 넣고 해결할 수가 없었습니다. ㅠㅠ

//            if (a == b && c == d) {
                if (!isWifiConn) {
                    // First initializate a WifiConnector object
                    WifiConnector connector = new WifiConnector(this, ssid, "", "", pw);
                    connector.setWifiStateListener(new WifiStateListener() {

                        @Override
                        public void onStateChange(int wifiState) {

                        }

                        @Override
                        public void onWifiEnabled() {
                            // here you should be start your network operations
                        }

                        @Override
                        public void onWifiEnabling() {

                        }

                        @Override
                        public void onWifiDisabling() {

                        }

                        @Override
                        public void onWifiDisabled() {

                        }
                    });

                    // For connecting to specific wifi network, third parameter (new_bssid) could be null
                    connector.connectToWifi(new ConnectionResultListener() {

                        @Override
                        public void successfulConnect(String SSID) {
                            Toast.makeText(getApplicationContext(), "SSID 연결 성공", Toast.LENGTH_SHORT).show();// toast!
                        }

                        @Override
                        public void errorConnect(int codeReason) {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show(); // toast!
                        }

                        @Override
                        public void onStateChange(SupplicantState supplicantState) {
                            Toast.makeText(getApplicationContext(), "State Update 성공", Toast.LENGTH_SHORT).show(); // update UI!
                        }
                    });

                    // And do not forget to unregister your wifi state listener on the onStop() or onDestroy() method
                    connector.unregisterWifiStateListener();
                }

//            }

        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "위치 정보를 활성화 해 주세요!!!!!!!!제발!!!", Toast.LENGTH_LONG).show();
        }


//        if (!isWifiConn) {
//
//            WifiConfiguration wifiConfig = new WifiConfiguration();
//            wifiConfig.SSID = String.format("\"%s\"", ssid);
//            wifiConfig.preSharedKey = String.format("\"%s\"", pw);
//
//            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//
//            int netId = wifiManager.addNetwork(wifiConfig);
//            wifiManager.disconnect();
//            wifiManager.enableNetwork(netId, true);
//            wifiManager.reconnect();
//            Toast.makeText(getApplicationContext(), "클릭된 팝업 메뉴" + networkInfo, Toast.LENGTH_SHORT).show();
//
//        }


    }

    public void gps_setting(View target) {
        Intent intent = new Intent(getApplicationContext(), GPSActivity.class);
        startActivity(intent);
    }

    private class WDLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            final String latitude = Double.toString(location.getLatitude());  //위도
            final String longitude = Double.toString(location.getLongitude()); //경도
            curlat = latitude;
            curlong = longitude;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

//                    tv_latitude.setText(la);
//                    tv_longitude.setText(lo);
                }
            });
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //딱히 아무 행동 하지 않습니다
        }

        @Override
        public void onProviderEnabled(String provider) {
            //요기도
        }

        @Override
        public void onProviderDisabled(String provider) {
            //마찬가지
        }
    }


}
