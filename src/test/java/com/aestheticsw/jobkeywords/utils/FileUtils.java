package com.aestheticsw.jobkeywords.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class FileUtils {

    public static <T> String getClassResourceAsString(String resourcePath, T clazz) {
        try (final InputStream is = clazz.getClass().getResourceAsStream(resourcePath)) {
            return IOUtils.toString(is);
        } catch (IOException e) {
            throw new RuntimeException("Exception reading resource file: " + resourcePath + " for class: "
                + clazz.getClass().getName(), e);
        }
    }

}
