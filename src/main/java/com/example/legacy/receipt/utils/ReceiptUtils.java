package com.example.legacy.receipt.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Component
public class ReceiptUtils {
    public byte[] transformInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int byteRead;
        while((byteRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteRead);
        }

        inputStream.close();

        byte[] result = Base64.getEncoder().encode(outputStream.toByteArray());
        outputStream.close();
        return result;
    }

    public String getPostParams(String encodedImageData) throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray images = new JSONArray();
        JSONObject image = new JSONObject();

        json.put("version", "V2");
        json.put("requestId", UUID.randomUUID().toString());
        json.put("timestamp", System.currentTimeMillis());
        json.put("images", images);

        images.put(image);
        image.put("format", "jpg");
        image.put("data", encodedImageData);
        image.put("name", "demo");
        return json.toString();
    }
}
