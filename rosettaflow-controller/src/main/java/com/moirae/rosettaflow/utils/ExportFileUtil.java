package com.moirae.rosettaflow.utils;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
public class ExportFileUtil {

    /**
     * 导出Csv
     *
     * @param filename 文件名，不带后缀
     * @param list     map封装的数据list
     * @param response response
     */
    public static void exportCsv(String filename, List<String[]> list, HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType("application/octet-stream");
        try {
            response.setCharacterEncoding("UTF-8");
            CsvWriter writer = CsvUtil.getWriter(response.getWriter());
            writer.write(list);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("文件下截失败，失败原因{}", e.getMessage(), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_FILE_DOWNLOAD_FAIL.getMsg());
        }
    }

    /**
     * 导出Csv
     *
     * @param filename 文件名，不带后缀
     * @param content  文件字节
     * @param response response
     */
    public static void exportCsv(String filename, byte[] content, HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("Assembly response header error:{}",e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_FILE_DOWNLOAD_FAIL.getMsg());
        }
        response.setContentType("application/octet-stream");
        try {
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("文件下截失败，失败原因{}", e.getMessage(), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_FILE_DOWNLOAD_FAIL.getMsg());
        }
    }
}
