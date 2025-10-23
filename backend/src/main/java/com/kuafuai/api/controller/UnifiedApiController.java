package com.kuafuai.api.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import com.kuafuai.api.parser.ApiResultParser;
import com.kuafuai.api.service.ApiBusinessService;
import com.kuafuai.common.domin.ErrorCode;
import com.kuafuai.common.domin.ResultUtils;
import com.kuafuai.common.exception.BusinessException;
import com.kuafuai.common.util.ServletUtils;
import com.kuafuai.login.handle.GlobalAppIdFilter;
import com.kuafuai.system.entity.DynamicApiSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UnifiedApiController {

    @Autowired
    private ApiBusinessService apiBusinessService;
    private final Gson gson = new Gson();

    private final Type return_value_type = new TypeToken<Map<String, Object>>() {
    }.getType();

    @PostMapping("/{key}")
    public Object handle(
            @PathVariable(value = "key") String apiKey,
            @RequestBody Map<String, Object> data
    ) {
        String appId = GlobalAppIdFilter.getAppId();
        DynamicApiSetting setting = apiBusinessService.getByApiKey(appId, apiKey);
        if (setting == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (StringUtils.equalsIgnoreCase(setting.getProtocol(), "http")) {
            // 设置环境变量参数
            data.put("ip", ServletUtils.getClientIp());
            String result = apiBusinessService.callHttpApi(setting, data);
            ApiResultParser.parser(result);
            String dataPath = setting.getDataPath();
            String dataType = setting.getDataType();
            try {
                if (StringUtils.isNotEmpty(dataPath)) {
                    Object content = JsonPath.read(result, dataPath);

                    if (StringUtils.isNotEmpty(dataType) && StringUtils.equalsIgnoreCase(dataType, "json")) {
                        if (content instanceof String) {
                            return ResultUtils.success(gson.fromJson(String.valueOf(content), return_value_type));
                        } else {
                            return ResultUtils.success(content);
                        }
                    } else {
                        return ResultUtils.success(content);
                    }
                } else {
                    return gson.fromJson(result, return_value_type);
                }
            } catch (Exception e) {
                throw new BusinessException(e.getMessage() + "\n" + result);
            }
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
    }
}
