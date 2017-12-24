package com.react_native_gzip;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;

public class RNGZipManager extends ReactContextBaseJavaModule {
    public RNGZipManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNGZipManager";
    }

    @ReactMethod
    public void gunzip(String source, String dest, Promise promise) {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            promise.reject("-2", "file not found");
            return;
        }

        File destFile = new File(dest);
        byte[] buffer = new byte[1024];

        try {
            FileInputStream fileInputStream = new FileInputStream(source);

            GZIPInputStream gzipIs = new GZIPInputStream(fileInputStream);

            FileOutputStream fout = new FileOutputStream(destFile);
            int len = 0;
            while ((len = gzipIs.read(buffer)) > 0) {
                fout.write(buffer, 0, len);
            }

            fout.close();

            WritableMap map = Arguments.createMap();
            map.putString("path", destFile.getAbsolutePath());
            promise.resolve(map);

        } catch (IOException e) {
            promise.reject("-2", e);
        }
    }

    @ReactMethod
    public void getUnpackedContextFromGzipFile(String source, Promise promise) {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            promise.reject("-2", "file not found");
            return;
        }

        String body = null;
        String charset = "UTF-8"; // You should determine it based on response header.

        try (
                FileInputStream gzippedResponse = new FileInputStream(source);

                InputStream ungzippedResponse = new GZIPInputStream(gzippedResponse);
                Reader reader = new InputStreamReader(ungzippedResponse, charset);
                Writer writer = new StringWriter();

        ) {
            char[] buffer = new char[10240];
            for (int length = 0; (length = reader.read(buffer)) > 0;) {
                writer.write(buffer, 0, length);
            }
            body = writer.toString();

            WritableMap map = Arguments.createMap();
            map.putString("body", body);
            promise.resolve(map);

        } catch (IOException e) {
            promise.reject("-2", e);
        }
    }
}