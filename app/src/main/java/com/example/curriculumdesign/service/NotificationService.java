package com.example.curriculumdesign.service;

import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.curriculumdesign.R;
import com.example.curriculumdesign.activity.CodeActivity;
import com.example.curriculumdesign.activity.GpsActivity;
import com.example.curriculumdesign.activity.QrCodeActivity;
import com.example.curriculumdesign.receiver.NotifyClickReceiver;
import com.example.curriculumdesign.utils.ConnectionUtils;
import com.example.curriculumdesign.utils.SPUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;

/**
 * 后台监听消息推送的服务
 */
public class NotificationService extends Service
{
    private Context context;

    private static String EXCHANGE_NAME = "class_lei.exchange";
    private static String QUEUE_NAME = "";

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {  // 当服务第一次被开启的时候调用
        super.onCreate();

        context = this;
        //app.user.
        //设置每个用户对应一个队列


        QUEUE_NAME = "app.user."+(String) SPUtils.getParam(context, "userid", "");

        getDataFromMQ();
    }

    /**
     * 从消息队列获取数据
     */
    private void getDataFromMQ()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Log.e("rabbitmq","start");
                    // 获取到连接
                    Connection connection = ConnectionUtils.getConnection();
                    // 获取通道
                    final Channel channel = connection.createChannel();
                    // 声明队列
                    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                    // 绑定队列到交换机，同时指定需要订阅的routing key。
                    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "app.class.common");

                    // 定义队列的消费者
//                    DefaultConsumer consumer = new DefaultConsumer(channel)
//                    {
//                        @Override
//                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
//                        {
//                            // body 即消息体
//                            final String msg = new String(body);
//
//                            showNotifictionIcon(context, getAppName(context), msg);
//                            Log.e("msg",msg);
//
//                            //手动设置ACK
//                            channel.basicAck(envelope.getDeliveryTag(), false);
//                        }
//                    };
//                    // 监听队列，手动ACK
//                    channel.basicConsume(QUEUE_NAME, true, consumer);
                    channel.basicConsume(QUEUE_NAME , false ,  new DefaultConsumer(channel){
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            super.handleDelivery(consumerTag, envelope, properties, body);

                            String msg = new String(body) ;
                            long deliveryTag = envelope.getDeliveryTag() ;
                            channel.basicAck(deliveryTag , false);
                            Log.e("msg",msg);
                            showNotifictionIcon(context, getAppName(context), msg);
                            //从message池中获取msg对象更高效
                        }
                    });
                } catch (IOException e)
                {
                    e.printStackTrace();
                } catch (TimeoutException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showNotifictionIcon(Context context, String title, String content)
    {
        isNotificationEnabled(context);
//        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setAutoCancel(true);//点击后消失
//        builder.setLargeIcon((getBitmap(context)));//设置通知栏消息标题的头像
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
//        builder.setContentTitle(title);//设置标题
//        builder.setContentText(content);//设置内容
//        builder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
//        //利用PendingIntent来包装我们的intent对象,使其延迟跳转 设置通知栏点击意图
//        builder.setContentIntent(createIntent(context, title + content));
//        manager.notify(new Random().nextInt(20), builder.build());
        String[] msglist=content.split(",");
        Intent intent;
        if(msglist[0].equals("0"))
        {
            //0为二维码
            intent = new Intent(this, QrCodeActivity.class);
        }
        else
        {
            intent = new Intent(this, GpsActivity.class);
        }
        intent.putExtra("signid",msglist[1]);
        intent.putExtra("type","gps");
        Log.e("id",msglist[1]);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = createNotificationChannel("my_channel_ID", "my_channel_NAME", NotificationManager.IMPORTANCE_HIGH);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText("有一个签到开始了，快来签到吧！")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(100, notification.build());

    }

    /**
     * 创建通知栏消息点击后跳转的intent。
     */
    public PendingIntent createIntent(Context context, String data)
    {
        Intent intent = new Intent(context, NotifyClickReceiver.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("data", data);
        intent.putExtras(mBundle);
        intent.setAction("com.example.curriculumdesign");

        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return contentIntent;
    }

    /**
     * 获取应用图标bitmap
     */
    public static Bitmap getBitmap(Context context)
    {

        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try
        {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private String createNotificationChannel(String channelID, String channelNAME, int level) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, channelNAME, level);
            manager.createNotificationChannel(channel);
            return channelID;
        } else {
            return null;
        }
    }
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}