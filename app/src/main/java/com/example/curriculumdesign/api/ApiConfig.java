package com.example.curriculumdesign.api;

public class ApiConfig {
    public static final int PAGE_SIZE = 5;
    public static final String BASE_URL="http://47.93.33.180:8087";
    public static final String LOGIN = "/login"; //登录
    public static final String REGISTER = "/user/register";//注册
    public static final String CURRENT = "/user/current";//获取当前登陆的用户
    public static final String STARTSIGN= "/sign/start";//发起签到
    public static final String ENDSIGN= "/sign/end";//结束签到
    public static final String SIGN= "/sign/sign";//签到
    public static final String SIGNLIST= "/sign/list";//教师获取发起签到的列表
    public static final String FINISHSTUDENT= "/sign/finishstudent";//教师获得某次签到已完成学生的名单
    public static final String UNFINISHSTUDENT= "/sign/unfinishstudent";//教师获得某次签到未完成学生的名单
    public static final String FINISHSIGN= "/sign/finishsign";//学生获取已经完成的签到列表
    public static final String UNFINISHSIGN= "/sign/unfinishsign";//学生获取未完成的签到列表
    public static final String ADDCLASS= "/class/add";//添加一个课程
    public static final String ADDDELETE= "/class/delete";//删除一个课程
    public static final String JIONCLASS= "/class/join";//加入一个课程
    public static final String CLASSLIST= "/class/list";//获取我选的课堂
    public static final String CLASSLISTNYME= "/class/listByMe";//获取我建立的课堂
    public static final String QRCODE= "/images/getQRCodeBase64";//获取一个二维码
    public static final String SignExchange="/sign/exchange";
    public static final String MQ_HOST="47.93.33.180";
    public static final int MQ_PORT=5672;
    public static final String MQ_USERNAME="guest";
    public static final String MQ_PASSWORD="guest";
    public static final String MESSAGELIST="/message/list";

}
