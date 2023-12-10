package com.mintpot.broadcasting.common.utils;

import com.mintpot.broadcasting.common.enums.CategoryFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author ChienNX
 * @CreatedDate Oct 10, 2017 9:08:40 AM
 */
@Slf4j
public class Utils {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    @Value("${static.file.location}")
    private static String rootDirectory;

    public static String generateRandomString(int len) {
        var r = new SecureRandom();
        int maxLen = ALPHABET.length();

        var sb = new StringBuilder();
        for (var i = 0; i < len; i++) {
            sb.append(ALPHABET.charAt(r.nextInt(maxLen)));
        }
        return sb.toString();
    }

    public static String generateRandomStringOrNumber(String source, int length) {
        int maxLen = source.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(source.charAt(new SecureRandom().nextInt(maxLen)));
        }
        return sb.toString();
    }

    public static String readFile(Object file) {
        StringBuilder result = new StringBuilder();
        String line = "";
        try (BufferedReader br = new BufferedReader(file instanceof File ? new FileReader((File) file) : new FileReader((String) file))) {
            while ((line = br.readLine()) != null) {
                if (result.length() != 0) {
                    result.append("\n");
                }
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();

    }

    public static Date convertStringToDate(String dateStr, String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern);
            return format.parse(dateStr);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(String fileUrl) {
        try {
            String filePath = fileUrl.substring(fileUrl.lastIndexOf("private")).replace("private", rootDirectory);
            File file = new File(filePath);
            if (file.delete()) {
                log.info("-----------" + file.getName() + " is deleted!---");
            } else {
                log.info("-----------Delete operation is failed.--------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param multipart
     * @param category
     * @param subFolder
     * @return Path to file on local disk
     */
    public static String uploadLargeFileToLocalServer(MultipartFile multipart, CategoryFile category, String subFolder) {
        String fileName = multipart.getOriginalFilename();
        String directory = rootDirectory + File.separator + category.name().toLowerCase() +
                File.separator + subFolder;
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        assert fileName != null;
        String ext = fileName.substring(fileName.lastIndexOf("."));
        String fileNew = UUID.randomUUID().toString().concat(ext);
        String filePath = Paths.get(directory, fileNew).toString();
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            InputStream inputStream = multipart.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] buf = new byte[102400];
            while ((bis.read(buf)) != -1) {
                stream.write(buf);
            }
            stream.flush();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "public" + File.separator + category.name().toLowerCase() +
                File.separator + subFolder + File.separator + fileNew;
    }

    public static String getCellString(Row row, Integer index) {
        if (row == null || index == null)
            return null;
        Cell c = row.getCell(index);
        DataFormatter formatter = new DataFormatter();
        return c != null ? formatter.formatCellValue(c) : null;
    }

    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (!StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }
        return remoteAddr;
    }
}
