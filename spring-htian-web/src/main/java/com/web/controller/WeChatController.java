package com.web.controller;

import com.bean.wechat.AccessTokenDTO;
import com.bean.wechat.TemplateMessageParam;
import com.bean.wechat.TemplateParam;
import com.bean.wechat.WechatRestDTO;
import com.client.wechat.WechatFeighApi;
import com.common.utils.WechatUtils;
import com.web.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizehao
 */
@Api(value = "CallBackController", description = "")
@RestController
@RequestMapping("/rest/wechat")
public class WeChatController {

    private static final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Value("${wx.appid}")
    private String wxAppid;

    @Value("${wx.secret}")
    private String wxSecret;

    private final static String token = "blogs";

    private static String accessToken = null;

    private static long expiresInTime = 0;

    @Autowired
    private WechatFeighApi wechatFeighApi;

    @ApiOperation(value = "获取授权access_token")
    @RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
    public Result<AccessTokenDTO> getAccessToken() {
        AccessTokenDTO accessTokenRes;
        if(expiresInTime == 0 || expiresInTime <= (System.currentTimeMillis() /1000)){
            accessTokenRes = wechatFeighApi.getConfig("client_credential",wxAppid,wxSecret);
            if(null!=accessTokenRes){
                accessToken = accessTokenRes.getAccess_token();
                expiresInTime = (System.currentTimeMillis() / 1000) + accessTokenRes.getExpires_in();
            }
            accessTokenRes.setAbout("已刷新");
        } else {
            accessTokenRes = new AccessTokenDTO();
            accessTokenRes.setAccess_token(accessToken);
            accessTokenRes.setExpires_in(expiresInTime - (System.currentTimeMillis() /1000));
            accessTokenRes.setAbout("未失效");
        }
        return Result.success(accessTokenRes);
    }

    @ApiOperation(value = "发送消息模板通知")
    @RequestMapping(value = "/sendTemplate", method = RequestMethod.POST)
    public Result sendTemplate(@RequestBody TemplateMessageParam param) {
        Map<String,TemplateParam> data = new HashMap<>();
        data.put("name",new TemplateParam("广州喜来登酒店 豪华海景房 二晚\r\n","#173177"));
        data.put("withdrawMoney",new TemplateParam("66.68\r\n","#173177"));
        data.put("withdrawTime",new TemplateParam("12:12\r\n","#173177"));
        data.put("cardInfo",new TemplateParam("50015359541510188\r\n","#173177"));
        data.put("arrivedTime",new TemplateParam("2019年8月8日\r\n","#173177"));
        data.put("remark",new TemplateParam("欢迎再次预定！\r\n","#173177"));
        param.setData(data);
        WechatRestDTO result = wechatFeighApi.sendMessage(param,accessToken);
        if(0 == result.getErrcode()){
            return Result.success(result.getMsgid());
        }
        return Result.error(result.getErrmsg());
    }


    /***
     * 开发者接入验证 确认请求来自微信服务器
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 成为开发者验证
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "微信接入验证")
    @RequestMapping(value = "config", method = RequestMethod.GET)
    public void get(@RequestParam(required = false) String signature, @RequestParam(required = false) String timestamp,
                    @RequestParam(required = false) String nonce, @RequestParam(required = false) String echostr,
                    HttpServletResponse response) throws IOException {
        //消息来源可靠性验证,确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        PrintWriter out = response.getWriter();
        try {
            if (WechatUtils.checkSignature(token,signature, timestamp, nonce)) {
                logger.info("=======请求校验成功======，{}", echostr);
                out.print(echostr);
            }
        } catch (Exception e) {
            logger.error("验证微信服务器异常");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


}
