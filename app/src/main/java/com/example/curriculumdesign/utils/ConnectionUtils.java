package com.example.curriculumdesign.utils;




import android.telephony.TelephonyManager;

import com.example.curriculumdesign.api.ApiConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 配置RabbitMQ连接
 */
public class ConnectionUtils
{
    public static Connection getConnection() throws IOException, TimeoutException
    {
        ConnectionFactory conn = new ConnectionFactory();
        conn.setHost(ApiConfig.MQ_HOST);  //RabbitMQ服务所在的ip地址
        conn.setPort(ApiConfig.MQ_PORT);   //RabbitMQ服务所在的端口号
        conn.setUsername(ApiConfig.MQ_USERNAME);
        conn.setPassword(ApiConfig.MQ_PASSWORD);
        return conn.newConnection();
    }
}