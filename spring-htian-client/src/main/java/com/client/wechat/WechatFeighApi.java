package com.client.wechat;

import com.bean.wechat.AccessTokenDTO;
import com.bean.wechat.TemplateMessageParam;
import com.bean.wechat.WechatRestDTO;
import com.client.config.FeignConfig;
import com.constants.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lizehao 2019-08-08
 */
@FeignClient(name = Constants.ServiceName.Wechat,url = Constants.ServiceName.Wechat,configuration = FeignConfig.class)
public interface WechatFeighApi {

    @GetMapping(value = "/cgi-bin/token")
    AccessTokenDTO getConfig(@RequestParam("grant_type") String grant_type,@RequestParam("appid") String appid,@RequestParam("secret") String secret);

    @PostMapping(value = "/cgi-bin/message/template/send")
    WechatRestDTO sendMessage(@RequestBody TemplateMessageParam param, @RequestParam("access_token") String access_token);
}
