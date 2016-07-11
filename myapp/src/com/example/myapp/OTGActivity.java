package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lz.domain.IdentityCard;
import com.lz.nfc.device.AndroidDriver;
import com.lz.nfc.handler.ReadHandler;

public class OTGActivity extends Activity {
    /**
     * otg
     */
    private TextView name;
    private TextView nametext;
    private TextView sex;
    private TextView sextext;
    private TextView mingzu;
    private TextView mingzutext;
    private TextView birthday;
    private TextView birthdaytext;
    private TextView address;
    private TextView addresstext;
    private TextView number;
    private TextView numbertext;
    private TextView qianfa;
    private TextView qianfatext;
    private TextView start;
    private TextView starttext;
    private TextView dncodetext;
    private static Button onredo;
    private TextView dncode;
    private TextView Readingtext;
    private ImageView idimg;
    public static final int NFC_NET_NETCONNECT_ERROR = 1; // 网络连接错误
    public static final int NFC_NET_NETRECV_ERROR = 2;// 网络接收错误
    public static final int NFC_NET_NETSEND_ERROR = 3;// 网络发送错误
    public static final int NFC_NET_NFCOPEN_ERROR = 4;// NFC打开错误
    public static final int NFC_NET_NFCREADCARD_ERROR = 5; // NFC读卡错误
    public static final int NFC_NET_AUTHENTICATION_FAIL = 6;// 认证失败
    public static final int NFC_NET_SEARCHCARD_FAIL = 7;// NFC寻卡失败
    public static final int NFC_NET_STARTNETREADCARD_FAIL = 8; // 启动网络读卡失败
    public static final int NFC_NET_INVALIDNETCMD = 9;// 无效的网络命令
    public static final int NFC_NET_GETSERIALFAIL = 10;// 获取设备序列号失败
    public static final int NFC_NET_DEVICEAUTHFAIL = 11;// 无效的网络命令
    public static final int NFC_NET_UNKNOWN_ERROR = 99;// 未知错误

    public static final int READ_IDCARD_SUCCESS = 511; //读取身份信息成功
    public static final int PLASE_INIT_SERVER = 513; //请先初始化服务器
    public static final int NOT_FOUND_DEVICE = 514; //未找到读卡设备
    public static final int READ_PHOTO_SUCCESS = 512; //读取照片信息信息成功
    public static final int READ_PHOTO_ERROR = 515; //读取照片信息失败


    private static final String ACTION_USB_PERMISSION = "com.lz.nfc.USB_PERMISSION";
    private AndroidDriver androidDriver;
    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otg_layout);
        context = this;

        /**
         * otg
         */
        onredo = (Button) findViewById(R.id.scale);
        onredo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onredo.setEnabled(false);
                onredo.setFocusable(false);
                nametext.setText("");
                sextext.setText("");
                mingzutext.setText("");
                birthdaytext.setText("");
                addresstext.setText("");
                numbertext.setText("");
                qianfatext.setText("");
                starttext.setText("");
                dncodetext.setText("");
                idimg.setImageBitmap(null);
                Readingtext.setText("      正在读卡，请稍候...");
                Readingtext.setVisibility(View.VISIBLE);
                int cfd = androidDriver.GetFD();
                String ip = "222.46.20.174";
                int port = 8018;
                ReadHandler threadHandler = new ReadHandler(mHandler, context, cfd,ip,port);
                threadHandler.postData();
            }
        });

        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        nametext = (TextView) findViewById(R.id.nametext);
        sextext = (TextView) findViewById(R.id.sextext);
        mingzu = (TextView) findViewById(R.id.mingzu);
        mingzutext = (TextView) findViewById(R.id.mingzutext);
        birthday = (TextView) findViewById(R.id.birthday);
        birthdaytext = (TextView) findViewById(R.id.birthdaytext);
        address = (TextView) findViewById(R.id.address);
        addresstext = (TextView) findViewById(R.id.addresstext);
        number = (TextView) findViewById(R.id.number);
        numbertext = (TextView) findViewById(R.id.numbertext);
        qianfa = (TextView) findViewById(R.id.qianfa);
        qianfatext = (TextView) findViewById(R.id.qianfatext);
        start = (TextView) findViewById(R.id.start);
        starttext = (TextView) findViewById(R.id.starttext);
        Readingtext = (TextView) findViewById(R.id.Readingtext);
        dncodetext = (TextView) findViewById(R.id.dncodetext);
        dncode = (TextView) findViewById(R.id.dncode);

        name.setText("姓名：");
        sex.setText("性别：");
        mingzu.setText("民族：");
        birthday.setText("出生年月：");
        address.setText("地址：");
        number.setText("身份证号码：");
        qianfa.setText("签发机关：");
        start.setText("有效时间：");
        dncode.setText("DN码：");
        idimg = (ImageView) findViewById(R.id.idimg);
        Readingtext.setVisibility(View.GONE);
        Readingtext.setText("      正在读卡，请稍候...");
        Readingtext.setTextColor(Color.RED);

        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        androidDriver = new AndroidDriver(usbManager, context, ACTION_USB_PERMISSION);
    }

    @Override
    public void onDestroy() {
        if (androidDriver != null) {
            if (androidDriver.isConnected()) {
                androidDriver.CloseDevice();
            }
            androidDriver = null;
        }

        super.onDestroy();
    }


    @Override
    public void onResume() {
        if (2 == androidDriver.ResumeUsbList()) {
            androidDriver.CloseDevice();
        }
        super.onResume();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READ_IDCARD_SUCCESS:
                    Readingtext.setText("      读卡成功");
                    Readingtext.setVisibility(View.GONE);
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    IdentityCard identityCard = (IdentityCard) msg.obj;
                    nametext.setText(identityCard.getName());
                    sextext.setText(identityCard.getSex());
                    mingzutext.setText(identityCard.getEthnicity());
                    birthdaytext.setText(identityCard.getBirth());
                    addresstext.setText(identityCard.getAddress());
                    numbertext.setText(identityCard.getCardNo());
                    qianfatext.setText(identityCard.getAuthority());
                    starttext.setText(identityCard.getPeriod());
                    dncodetext.setText(identityCard.getDN());
                    Readingtext.setVisibility(View.GONE);
                    break;
                case READ_PHOTO_SUCCESS:
                    Readingtext.setText("      读照片成功");
                    Readingtext.setVisibility(View.GONE);
                    byte[] cardbmp = (byte[]) msg.obj;
                    Bitmap bm = BitmapFactory.decodeByteArray(cardbmp, 0, cardbmp.length);
                    idimg.setImageBitmap(bm);
                    break;
                case NFC_NET_NETCONNECT_ERROR:
                    Readingtext.setText("      网络连接错误！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_NETRECV_ERROR:
                    Readingtext.setText("      网络接收错误！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_NETSEND_ERROR:
                    Readingtext.setText("      网络发送错误！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_NFCOPEN_ERROR:
                    Readingtext.setText("      NFC打开错误！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_NFCREADCARD_ERROR:
                    Readingtext.setText("      NFC读卡错误！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_AUTHENTICATION_FAIL:
                    Readingtext.setText("      认证失败！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_SEARCHCARD_FAIL:
                    Readingtext.setText("      NFC寻卡失败！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_STARTNETREADCARD_FAIL:
                    Readingtext.setText("      启动网络读卡失败！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_GETSERIALFAIL:
                    Readingtext.setText("      获取设备序列号失败！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_DEVICEAUTHFAIL:
                    Readingtext.setText("      设备认证失败！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_INVALIDNETCMD:
                    Readingtext.setText("      无效的网络命令！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NFC_NET_UNKNOWN_ERROR:
                    Readingtext.setText("      未知错误！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case PLASE_INIT_SERVER:
                    Readingtext.setText("      请初始化服务器配置！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    break;
                case NOT_FOUND_DEVICE:
                    Readingtext.setText("      未找到读卡设备！");
                    onredo.setEnabled(true);
                    onredo.setFocusable(true);
                    onredo.setBackgroundResource(R.drawable.sfz_dq);
                    if (2 == androidDriver.ResumeUsbList()) {
                        androidDriver.CloseDevice();
                    }
                    break;
                case READ_PHOTO_ERROR:
                    String m = (String) msg.obj;
                    Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
