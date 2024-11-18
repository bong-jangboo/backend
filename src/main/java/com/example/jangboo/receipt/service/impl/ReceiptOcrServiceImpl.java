package com.example.jangboo.receipt.service.impl;

import com.example.jangboo.global.error.CustomException;
import com.example.jangboo.receipt.controller.dto.ocr.OcrJsonRes;
import com.example.jangboo.receipt.controller.dto.ocr.OcrRes;
import com.example.jangboo.receipt.domain.Receipt;
import com.example.jangboo.receipt.error.OcrErrorCode;
import com.example.jangboo.receipt.service.ReceiptOcrService;
import com.example.jangboo.receipt.utils.ReceiptUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
public class ReceiptOcrServiceImpl implements ReceiptOcrService {

    private final ReceiptUtils receiptUtils;

    @Value("${ocr-api-secret}")
    private String apiSecret;
    @Value("${ocr-api-url}")
    private String apiUrl;

    @Override
    public OcrRes.OcrResponse ocrStart(String imageUrl) {
        try {
            return getOcrData(imageUrl);
        } catch (Exception e) {
            throw new CustomException(OcrErrorCode.OCR_FAIL);
        }
    }

    @Override
    public OcrRes.OcrResponse getOcrData(String imageUrl) {
        StringBuffer response = new StringBuffer();
        String jsonText = "";
        try {

            URL requestURL = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) requestURL.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", apiSecret);

            URL url = new URL(imageUrl);
            String postParams = receiptUtils.getPostParams(new String(receiptUtils.transformInputStreamToByteArray(url.openStream())));

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            jsonText = response.toString();

        } catch (IOException | JSONException e) {
            log.error(e.toString());
            throw new CustomException(OcrErrorCode.OCR_FAIL);
        }

        ObjectMapper mapper = new ObjectMapper();
        if (JsonPath.read(jsonText, "$.images[0].receipt") != null) {
            OcrJsonRes.Receipt receipt = mapper.convertValue(JsonPath.read(jsonText, "$.images[0].receipt"),
                    OcrJsonRes.Receipt.class);
            return OcrRes.OcrResponse.of(receipt.getResult());
        } else {
            throw new CustomException(OcrErrorCode.OCR_FAIL);
        }
    }


}
