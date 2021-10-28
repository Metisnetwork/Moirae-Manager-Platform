package com.moirae.rosettaflow.common.utils;


import com.moirae.rosettaflow.common.constants.SysConstant;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hudenian
 * @date 2021/5/17
 */
public class IoUtils {

    public static InputStream readFileToInputStream(String filePath) throws IOException {
        InputStream in;
        if (filePath.contains(SysConstant.CLASSPATH)) {
            filePath = filePath.replace(SysConstant.CLASSPATH, "").trim();
            ClassPathResource resource = new ClassPathResource(filePath);
            in = resource.getInputStream();
        } else {
            in = new FileInputStream(filePath);
        }
        return in;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
