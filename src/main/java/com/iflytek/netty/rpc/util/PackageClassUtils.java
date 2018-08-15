package com.iflytek.netty.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * create by 叶云轩 at 2018/3/3-下午2:13
 * contact by tdg_yyx@foxmail.com
 */
public class PackageClassUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(PackageClassUtils.class);

    /**
     * 解析包参数
     *
     * @param basePackage 包名
     * @return 包名字符串集合
     */
    public static List<String> resolver(String basePackage) {
        //以";"分割开多个包名
        String[] splitFHs = basePackage.split(";");
        List<String> classStrs = new ArrayList<>();
        //s: com.yyx.util.*
        for (String s : splitFHs) {
            LOGGER.info("[加载类目录] {}", s);
            //路径中是否存在".*" com.yyx.util.*
            boolean contains = s.contains(".*");
            if (contains) {
                //截断星号  com.yyx.util
                String filePathStr = s.substring(0, s.lastIndexOf(".*"));
                //组装路径 com/yyx/util
                String filePath = filePathStr.replaceAll("\\.", "/");
                //获取路径 xxx/classes/com/yyx/util
                File file = new File(PackageClassUtils.class.getResource("/").getPath() + "/" + filePath);
                //获取目录下获取文件
                getAllFile(filePathStr, file, classStrs);
            } else {
                String filePath = s.replaceAll("\\.", "/");
                LOGGER.debug("==== file_path : " + filePath);
                String resourcePath = PackageClassUtils.class.getResource("/").getPath();
                resourcePath = resourcePath.replaceAll("!","");
                LOGGER.debug("==== resource_path : " + resourcePath);
//                URL pathURL;
//                File file = null;
//                try {
//                    pathURL = ResourceUtils.getURL("classpath:" + filePath);
//                    pathURL = pathURL.rep
//                    LOGGER.debug("===== URL" + pathURL);
//                    file =  ResourceUtils.getFile(pathURL);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                File file = new File(PackageClassUtils.class.getResource("/").getPath() + "/" + filePath);

                classStrs = getClassReferenceList(classStrs, file, s);
            }
        }
        return classStrs;
    }

    /**
     * 添加全限定类名到集合
     *
     * @param classStrs 集合
     * @return 类名集合
     */
    private static List<String> getClassReferenceList(List<String> classStrs, File file, String s) {
        File[] listFiles = file.listFiles();
        if (listFiles != null && listFiles.length != 0) {
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    String name = file2.getName();
                    String fileName = s + "." + name.substring(0, name.lastIndexOf('.'));
                    LOGGER.info("[加载完成] 类文件：{}", fileName);
                    classStrs.add(fileName);
                }
            }
        }
        return classStrs;
    }


    /**
     * 获取一个目录下的所有文件
     *
     * @param s
     * @param file
     * @param classStrs
     */
    private static void getAllFile(String s, File file, List<String> classStrs) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    getAllFile(s, file1, classStrs);
                }
            }
        } else {
            String path = file.getPath();
            String cleanPath = path.replaceAll("/", ".");
            String fileName = cleanPath.substring(cleanPath.indexOf(s), cleanPath.length());
            LOGGER.info("[加载完成] 类文件：{}", fileName);
            classStrs.add(fileName);
        }
    }
}
