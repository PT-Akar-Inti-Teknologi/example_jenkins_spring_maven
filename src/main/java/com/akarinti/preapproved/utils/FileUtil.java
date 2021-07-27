package com.akarinti.preapproved.utils;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Component
public class FileUtil {

    @Value("${hcp.url}")
    String hcpUrl;

    @Value("${hcp.auth}")
    String hcpAuth;

    public void requestHCP() {
        Unirest.config().verifySsl(false);
    }

    public InputStream getFileHCP(String url) {
        byte[] content = new byte[0];
        try {
            requestHCP();
            String HCP_AUTHORIZATION = hcpAuth;
            content = Unirest.get(url)
                    .header("Authorization", HCP_AUTHORIZATION)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .asBytes()
                    .getBody();
        } catch (UnirestException e) {
            log.info("error:: "+ e);
            e.printStackTrace();
        }
        return new ByteArrayInputStream(content);
    }
}
