package com.sq.transportmanage.gateway.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CsvUtils {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtils.class);

    public static final Integer downPerSize = 10000;
	public static final String tab = "\t";
    private OutputStreamWriter osw;
    private BufferedWriter bw = null;

    public OutputStreamWriter getOsw() {
        return osw;
    }

    public void setOsw(OutputStreamWriter osw) {
        this.osw = osw;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public  boolean exportCsvV2(HttpServletResponse response,
                                List<String> dataList,
                                List<String> headdataList,
                                String  fileName, boolean isFirst, boolean islast) throws IOException {

        boolean isSucess=false;
        OutputStreamWriter osw = this.getOsw();
        BufferedWriter bw = this.getBw();
        try {

            if(isFirst){
                response.reset();
                //设置response
                response.setContentType("application/octet-stream;charset=UTF-8");
                response.setHeader("content-disposition", "attachment; filename="+fileName);
            }

            if(osw == null){
                osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
                osw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));

                this.setOsw(osw);
            }
            if(bw == null){
                bw = new BufferedWriter(osw);
                this.setBw(bw);
            }


            if(isFirst){
                if(headdataList!=null && !headdataList.isEmpty()){
                    for(String data : headdataList){
                        bw.write(data+"\r\n");
                    }
                }
            }
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.write(data+"\r\n");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(islast){
                if(bw!=null){
                    try {
                        bw.close();
                        bw=null;
                    } catch (IOException e) {
                        logger.error("",e);
                    }
                }
                if(osw!=null){
                    try {
                        osw.close();
                        osw=null;
                    } catch (IOException e) {
                        logger.error("",e);
                    }
                }
            }else{
                if(bw!=null){
                    try {
                        bw.flush(); 
                    } catch (IOException e) {
                        logger.error("",e);
                    }
                }
                if(osw!=null){
                    try {
                        osw.flush();

                    } catch (IOException e) {
                        logger.error("",e);
                    }
                }
            }

        }
        return isSucess;
    }



    //写一些共有的静态方法
    public static String buidFileName (HttpServletRequest request, String name) throws UnsupportedEncodingException {
        String fileName = name  + ".csv";
        //获得浏览器信息并转换为大写
        String agent = request.getHeader("User-Agent").toUpperCase();
        //IE浏览器和Edge浏览器
        if (agent.indexOf("MSIE") > 0 || (agent.indexOf("GECKO") > 0 && agent.indexOf("RV:11") > 0)) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {  //其他浏览器
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }
        return fileName;
    }

    public static String dateFormat(Date date){
        if(date == null){
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(date);
        return s;
    }


}
