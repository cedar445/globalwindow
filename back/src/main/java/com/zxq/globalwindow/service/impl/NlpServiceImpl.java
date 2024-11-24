package com.zxq.globalwindow.service.impl;

import com.aliyun.teaopenapi.Client;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import com.aliyun.teaopenapi.models.Params;
import com.zxq.globalwindow.service.NlpService;
import com.aliyuncs.alinlp.model.v20200629.GetPosChEcomRequest;
import com.aliyuncs.alinlp.model.v20200629.GetPosChEcomResponse;
import com.aliyuncs.alinlp.model.v20200629.GetSaChGeneralRequest;
import com.aliyuncs.alinlp.model.v20200629.GetSaChGeneralResponse;
import com.aliyuncs.IAcsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NlpServiceImpl implements NlpService {

    @Autowired
    private IAcsClient acsClient;
    private final Config aliyunConfig;

    public NlpServiceImpl(Config aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    public String getPosChEcom(String text) throws Exception {
        GetPosChEcomRequest request = new GetPosChEcomRequest();
        request.setSysEndpoint("alinlp.cn-hangzhou.aliyuncs.com");
        request.setServiceCode("alinlp");
        request.setText(text);
        request.setTokenizerId("MAINSE");

        GetPosChEcomResponse response = acsClient.getAcsResponse(request);
        return response.getData();
    }

    public String getSaChGeneral(String text) throws Exception {
        GetSaChGeneralRequest request = new GetSaChGeneralRequest();
        request.setServiceCode("alinlp");
        request.setText(text);

        GetSaChGeneralResponse response = acsClient.getAcsResponse(request);
        return response.getData();
    }

    private Params createApiInfo() {
        return new Params()
                .setAction("RunPreTrainService")
                .setVersion("2019-11-11")
                .setProtocol("HTTPS")
                .setMethod("POST")
                .setAuthType("AK")
                .setStyle("RPC")
                .setPathname("/")
                .setReqBodyType("formData")
                .setBodyType("json");
    }

    public Map<String, Object> getKeyWord(String text) throws Exception {
        Client client = new Client(aliyunConfig);
        Params params = createApiInfo();
        Map<String, Object> body = new HashMap<>();
        String predictContent = String.format("{\"query\": \"%s\", \"top_k\": 5, \"type\": \"keyword_extraction\"}", text);
        body.put("ServiceName", "NLP-textrank");
        body.put("PredictContent", predictContent);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        com.aliyun.teaopenapi.models.OpenApiRequest request = new com.aliyun.teaopenapi.models.OpenApiRequest()
                .setBody(body);
        return (Map<String, Object>) client.callApi(params, request, runtime);
    }
}
