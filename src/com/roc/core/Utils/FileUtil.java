package com.roc.core.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Lu Tingming
 * Time: 2010-11-11 20:53:54
 * Desc: 文件工具
 */
public class FileUtil {

    /**
     * 日志工具
     */
    public static Logger logger = LogManager.getLogger(FileUtil.class.getName());

    /**
     * 系统分隔符
     */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * 写入内容到路径指定的文件
     *
     * @param s
     * @param path
     */
    public static void append(String s, String path) {
        BufferedWriter writer = null;

        try {
            File file = new File(path);
            mkdirIfNotExist(file.getParent());
            writer = new BufferedWriter(new FileWriter(file, true));

            writer.write(s);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入内容到路径指定的文件
     *
     * @param s
     * @param path
     */
    public static void write(String s, String path) {
        BufferedWriter writer = null;

        try {
            File file = new File(path);
            mkdirIfNotExist(file.getParent());
            writer = new BufferedWriter(new FileWriter(file));

            writer.write(s);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * 杨沛的方法
     * 指定编码写入到文件
     */
    public static void write(List fileContentList, String path, String encode) throws Exception {
        FileOutputStream out = null;
        try {
            FileUtil.mkdirIfNotExist(FileUtil.getDir(path));
            out = new FileOutputStream(new File(path));
            for (Object obj : fileContentList) {
                if (obj instanceof String) {
                    out.write(((String) obj).getBytes(encode));
                }
                if (obj instanceof byte[]) {
                    out.write((byte[]) obj);
                }
            }
            out.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * 杨沛的方法
     * 指定编码写入到文件
     */
    public static void write(String s, String path, String encode) throws Exception {
        BufferedWriter out = null;
        try {
            FileUtil.mkdirIfNotExist(FileUtil.getDir(path));
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(path)), encode));
            out.write(s);
            out.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 杨沛的方法
     * 指定编码写入到文件
     *
     * @param path
     * @param encode
     * @return
     */
    public static String readFile(String path, String encode) {
        StringBuffer sb = new StringBuffer();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
            //reader.skip(1);
            String strLine;
            while (true) {
                strLine = reader.readLine();
                // System.out.println(strLine);
                if (strLine == null) break;
                sb.append(strLine).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    /**
     * 写入行的列表到路径对应的文件
     *
     * @param list
     * @param path
     */
    public static void writeLines(List<String> list, String path) {
        BufferedWriter writer = null;

        try {
            File file = new File(path);

            writer = new BufferedWriter(new FileWriter(file));

            for (String s : list) {
                writer.write(s);
                writer.write('\n');
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    /**
     * 读入文件到字符串
     *
     * @param path
     * @return
     */
    public static String readFile(String path) throws Exception {
        StringBuffer sb = new StringBuffer();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            //reader.skip(1);
            String strLine;
            while (true) {
                strLine = reader.readLine();
                //System.out.println(strLine);
                if (strLine == null) break;
                sb.append(strLine).append("\n");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return sb.toString();
    }


    /**
     * 读入文件到字符串
     * 一定行数的内容（用户处理大文件）
     *
     * @param path
     * @return
     */
    public static StringBuffer readFile(String path, int maxLines) {
        StringBuffer sb = new StringBuffer();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            //reader.skip(1);
            String strLine;
            int i = 0;
            while (true) {
                i++;
                if (i > maxLines) {
                    break;
                }

                strLine = reader.readLine();
                //System.out.println(strLine);
                if (strLine == null) break;
                sb.append(strLine).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return sb;
    }

    /**
     * 将输入流转换成字符串，不忽略输入流中的换行符
     */
    public static String ConvertToString(final InputStream is, final boolean closeStream) {
        StringBuilder strb = new StringBuilder();
        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = is.read(b)) > 0) {
                strb.append(new String(b, 0, len, "UTF-8"));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (closeStream) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return strb.toString();
    }

    /**
     * 找到第一个符合指定正则表达式的匹配字符串
     *
     * @param path
     * @param regex
     * @return
     */
    public static String findFirst(String path, String regex) {
        File file = new File(path);
        BufferedReader reader = null;
        Pattern pattern = Pattern.compile(regex);
        String result = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String strLine;
            while (true) {
                strLine = reader.readLine();
//                System.out.println(strLine);
                if (strLine == null) break;
                Matcher matcher = pattern.matcher(strLine);
                if (matcher.find()) {
                    result = matcher.group(1);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 将文件的每一行存入hashset
     *
     * @param path
     * @return
     */
    public static HashSet<String> readLinesIntoHashSet(String path) {
        return new HashSet<String>(readLines(path));
    }

    /**
     * 将文件的每一行存入hashset
     *
     * @param path
     * @return
     */
    public static HashSet<String> readLowercaseLinesIntoHashSet(String path) throws Exception {
        return new HashSet<String>(readLowercaseLines(path));
    }

    /**
     * 读入文件内容到行的列表
     *
     * @param path
     * @return
     */
    public static ArrayList<String[]> readStringArray(String path) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String strLine;

            // 处理第一行
            strLine = reader.readLine();
            if (strLine != null) {
                if (strLine.length() > 0 && strLine.charAt(0) == 65279) {
                    strLine = strLine.substring(1);
                }
                list.add(strLine.split("\t"));

                while (true) {
                    strLine = reader.readLine();
                    if (strLine == null) break;
                    list.add(strLine.split("\t"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return list;
    }

    /**
     * 读入文件内容到行的列表
     *
     * @param path
     * @return
     */
    public static List<String> readLowercaseLines(String path) throws Exception {
        List<String> lines = readLines(path);
        List<String> lowercaseLines = new ArrayList<String>(lines.size());
        for (String line : lines) {
            lowercaseLines.add(line.toLowerCase());
        }

        return lowercaseLines;
    }

    /**
     * 读入文件内容到行的列表
     */
    public static List<String> readLines(String path) {
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            if (file.exists()) {
                // ok
            } else {
                logger.warn("Can not find file by path: " + path);
                if (path.contains(":") || path.startsWith("/")) {
                    throw new FileNotFoundException(path);
                } else {
                    URL url = FileUtil.class.getClassLoader().getResource(path);
                    path = url.getPath();
                    logger.warn("Try " + path);
                    file = new File(path);
                }
            }
            reader = new BufferedReader(new FileReader(file));

            String strLine;

            // 处理第一行
            strLine = reader.readLine();
            if (strLine != null) {
                if (strLine.length() > 0 && strLine.charAt(0) == 65279) {
                    strLine = strLine.substring(1);
                }
                list.add(strLine);

                while (true) {
                    strLine = reader.readLine();
                    if (strLine == null) break;
                    list.add(strLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return list;
    }


    /**
     * 读入文件内容到行的列表
     */
    public static List<String> readLines(File file) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String strLine;

            // 处理第一行
            strLine = reader.readLine();
            if (strLine != null) {
                if (strLine.length() > 0 && strLine.charAt(0) == 65279) {
                    strLine = strLine.substring(1);
                }
                list.add(strLine);

                while (true) {
                    strLine = reader.readLine();
                    if (strLine == null) break;
                    list.add(strLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return list;
    }


    /**
     * 读入文件内容的前n行到列表
     */
    public static List<String> readLines(String path, int maxLineCount) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            if (file.exists()) {
                // ok
            } else {
                logger.warn("Can not find file by path: " + path);
                if (path.contains(":") || path.startsWith("/")) {
                    throw new FileNotFoundException(path);
                } else {
                    URL url = FileUtil.class.getClassLoader().getResource(path);
                    path = url.getPath();
                    logger.warn("Try " + path);
                    file = new File(path);
                }
            }
            reader = new BufferedReader(new FileReader(file));

            String strLine;

            // 处理第一行
            strLine = reader.readLine();
            if (strLine != null) {
                if (strLine.length() > 0 && strLine.charAt(0) == 65279) {
                    strLine = strLine.substring(1);
                }
                list.add(strLine);

                int i = 0;
                while (true) {
                    strLine = reader.readLine();
                    if (strLine == null) break;
                    list.add(strLine);
                    i++;
                    if (i >= maxLineCount) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return list;
    }

    /**
     * 读入文件内容的前n行到列表
     */
    public static void print(String path, int maxLineCount) throws Exception {
        File file = new File(path);
        BufferedReader reader = null;
        try {
            if (file.exists()) {
                // ok
            } else {
                logger.warn("Can not find file by path: " + path);
                if (path.contains(":") || path.startsWith("/")) {
                    throw new FileNotFoundException(path);
                } else {
                    URL url = FileUtil.class.getClassLoader().getResource(path);
                    path = url.getPath();
                    logger.warn("Try " + path);
                    file = new File(path);
                }
            }
            reader = new BufferedReader(new FileReader(file));

            String strLine;

            // 处理第一行
            strLine = reader.readLine();
            if (strLine != null) {
                if (strLine.length() > 0 && strLine.charAt(0) == 65279) {
                    strLine = strLine.substring(1);
                }
                System.out.println(strLine);

                int i = 0;
                while (true) {
                    strLine = reader.readLine();
                    if (strLine == null) break;
                    System.out.println(strLine);
                    i++;
                    if (i >= maxLineCount) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 计算文件行数
     */
    public static int countLines(String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            // ok
        } else {
            logger.warn("Can not find file by path: " + path);
            if (path.contains(":") || path.startsWith("/")) {
                throw new FileNotFoundException(path);
            } else {
                URL url = FileUtil.class.getClassLoader().getResource(path);
                path = url.getPath();
                logger.warn("Try " + path);
                file = new File(path);
            }
        }

        return countLines(file);
    }


    /**
     * 计算文件行数
     */
    public static int countLines(File file) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            int count = 0;
            while (true) {
                String strLine = reader.readLine();
                if (strLine != null) {
                    count++;
                    if(count % 1000000 == 0) {
                        System.out.println(count);
                    }
                } else {
                    break;
                }
            }

            System.out.println(count);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 读入文件内容到行的列表
     *
     * @param path
     * @param chaset
     * @return
     */
    public static ArrayList<String> readLines(String path, String chaset) {
        ArrayList<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), chaset));
            String strLine;
            while (true) {
                strLine = reader.readLine();
                //System.out.println(strLine);
                if (strLine == null) break;
                list.add(strLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return list;
    }

    /**
     * 从路径得到文件名（包括扩展名）
     *
     * @param path
     * @return
     */
    public static String fullNameFromPath(String path) {
        int idx = path.lastIndexOf("\\");
        int idx1 = path.lastIndexOf("/");
        if (idx1 > idx) idx = idx1;
        return path.substring(idx + 1);
    }


    /**
     * 从路径得到主文件名
     *
     * @param path
     * @return
     */
    public static String mainNameFromPath(String path) {
        return mainNameFromName(fullNameFromPath(path));
    }


    /**
     * 在主文件名后插入字符串
     *
     * @param path
     * @return
     */
    public static String appendMainNameInPath(String path, String insertion) {
        String dir = getDir(path);
        String fullName = path.substring(dir.length());
        String mainName = mainNameFromName(fullName);
        String newName;
        if (fullName.equals(mainName)) {
            newName = fullName + insertion;
        } else {
            String extName = extNameFromName(fullName);
            newName = mainName + insertion + "." + extName;
        }
        return dir + newName;
    }


    /**
     * 替换扩展名
     *
     * @param path
     * @return
     */
    public static String replaceExtName(String path, String newExtName) {
        String dir = getDir(path);
        String fullName = path.substring(dir.length());
        String mainName = mainNameFromName(fullName);

        return dir + mainName + "." + newExtName;
    }


    /**
     * 从文件名得到主文件名（不包括扩展名）
     *
     * @param fullName
     * @return
     */
    public static String mainNameFromName(String fullName) {
        String name = fullNameFromPath(fullName);
        int idx = name.lastIndexOf('.');
        if (idx >= 0) {
            return name.substring(0, idx);
        } else {
            return name;
        }
    }


    /**
     * 从文件名得到扩展名（不包括.）
     *
     * @param fullName
     * @return
     */
    public static String extNameFromName(String fullName) {
        int idx = fullName.lastIndexOf('.');
        if (idx >= 0) {
            return fullName.substring(idx + 1);
        } else {
            return null;
        }
    }

    /**
     * 将对象序列化到文件
     *
     * @param obj
     * @param path
     * @return
     */
    public static void writeObject(Object obj, String path) throws IOException {
        //DefaultLogger.logStart("write " + obj.getClass().getName() + " to " + path);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            FileUtil.mkdirIfNotExist(FileUtil.getDir(path));
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (IOException e) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            throw e;
        }
    }

    /**
     * 将对象序列化到文件
     *
     * @param obj
     * @param dir
     * @param fileName
     * @return
     */
    public static void writeObject(Object obj, String dir, String fileName) throws IOException {
        String path = dir + FILE_SEPARATOR + fileName;
        writeObject(obj, path);
    }

    /**
     * 读入对象
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static Object readObject(String dir, String fileName) {
        String path = dir + FILE_SEPARATOR + fileName;
        return readObject(path);
    }

    /**
     * 读入对象
     *
     * @param path
     * @return
     */
    public static Object readObject(String path) {
        ////DefaultLogger.logStart("Load from \"" + path + "\"");
        Object obj;
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            obj = ois.readObject();
            ois.close();
        } catch (Exception e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            obj = null;
        }
        ////DefaultLogger.logFinish("Load");
        return obj;
    }

    /**
     * 得到子目录
     *
     * @param s_dir
     * @return
     */
    public static ArrayList<File> getSubDirList(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();
        ArrayList<File> dirList = new ArrayList<File>();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isDirectory()) {
                dirList.add(fileArray[i]);
            }
        }
        return dirList;
    }

    /**
     * 得到目录下的文件列表
     *
     * @param s_dir
     * @return
     */
    public static ArrayList<File> getFileList(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();
        ArrayList<File> fileList = new ArrayList<File>();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileList.add(fileArray[i]);
            }
        }
        return fileList;
    }

    /**
     * 得到目录下的文件列表（递归包含子目录下的文件）
     *
     * @param dir
     * @param pattern
     */
    public static void getFileListR(File dir, String pattern) {
        ArrayList<File> list_all = getSubDicAndFileListR(dir);
        for (File file : list_all) {
            if (file.getAbsolutePath().contains(pattern)) {
                System.out.println(file.getAbsolutePath());
            }
        }
    }

    /**
     * 得到目录下的文件列表（递归包含子目录下的文件）（返回结果包括目录）
     *
     * @param dir
     * @return
     */
    public static ArrayList<File> getSubDicAndFileListR(File dir) {
        ArrayList<File> list = new ArrayList<File>();

        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            list.add(files[i]);
            if (files[i].isFile()) {
            } else {
                list.addAll(getSubDicAndFileListR(files[i]));
            }
        }

        return list;
    }

    /**
     * 得到目录下的文件列表（递归包含子目录下的文件）（返回结果不包括目录）
     *
     * @param dir
     * @return
     */
    public static ArrayList<File> getSubFileListR(File dir) {
        ArrayList<File> list = new ArrayList<File>();

        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                list.add(files[i]);
            } else {
                list.addAll(getSubFileListR(files[i]));
            }
        }

        return list;
    }

    /**
     * 对指定目录下的文件（递归包含子目录）修改文件名
     *
     * @param dir
     * @param regex
     * @param replacement
     */
    public static void renameR(File dir, String regex, String replacement) {
        ArrayList<File> list = getSubDicAndFileListR(dir);

        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            final String oldName = file.getName();
            String newName = oldName.replaceAll(regex, replacement);
            if (oldName.equals(newName)) {
                System.out.println("No need to change.");
            } else {
                String newPath = file.getParentFile().getAbsolutePath() + FILE_SEPARATOR + newName;
                System.out.println(file.getAbsolutePath() + "\t->");
                System.out.println(newPath);
                boolean b = file.renameTo(new File(newPath));
                System.out.println(b ? "OK" : "Failed!");
            }
        }
    }

    /**
     * 得到指定目录下的文件数组
     *
     * @param s_dir
     * @return
     */
    public static File[] getFiles(String s_dir) {
        File dir = new File(s_dir);

        return dir.listFiles();
    }

    /**
     * 得到指定目录下的文件的路径的数组
     *
     * @param s_dir
     * @return
     */
    public static String[] getFilePathes(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();

        String[] fileNames = new String[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileNames[i] = fileArray[i].getAbsolutePath();
            }
        }

        return fileNames;
    }

    /**
     * 得到指定目录下的文件的名称的列表
     *
     * @param s_dir
     * @return
     */
    public static String[] getFileNames(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();

        String[] fileNames = new String[fileArray.length];
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileNames[i] = fileArray[i].getName();
            }
        }

        return fileNames;
    }

    /**
     * 得到指定目录下的文件的路径的列表
     *
     * @param s_dir
     * @return
     */
    public static ArrayList<String> getFilePathList(String s_dir) {
        File dir = new File(s_dir);
        File[] fileArray = dir.listFiles();
        ArrayList<String> fileList = new ArrayList<String>();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isFile()) {
                fileList.add(fileArray[i].getAbsolutePath());
            }
        }
        return fileList;
    }

    /**
     * 得到指定目录下的文件的名称的列表
     *
     * @param s_dir
     * @return
     */
    public static ArrayList<String> getFileNameList(String s_dir) {
        ArrayList<String> pathList = getFilePathList(s_dir);
        ArrayList<String> nameList = new ArrayList<String>();

        for (String path : pathList) {
            nameList.add(fullNameFromPath(path));
        }

        return nameList;
    }

    /**
     * 移动目录
     *
     * @param srcDir
     * @param destDir
     */
    public static void moveDir(String srcDir, String destDir) {
        File f_srcDir = new File(srcDir);
        File[] list = f_srcDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".obj");
            }
        });

        File f_destDir = new File(destDir);
        if (!f_destDir.exists()) {
            f_destDir.mkdirs();
        }

        for (int i = 0; i < list.length; i++) {
            File f_src = list[i];
            File f_desc = new File(destDir + FILE_SEPARATOR + f_src.getName());
            f_src.renameTo(f_desc);
        }
    }

    /**
     * 移动文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void move(String srcPath, String destPath) {
        File f_src = new File(srcPath);
        File f_desc = new File(destPath);
        f_src.renameTo(f_desc);
    }

    /**
     * 创建目录
     *
     * @param dir
     */
    public static void mkdir(String dir) {
        File f = new File(dir);
        f.mkdir();
    }

    /**
     * 如果目录不存在，那么创建目录
     *
     * @param dir
     * @return
     */
    public static boolean mkdirIfNotExist(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            return f.mkdirs();
        }

        return true;
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        return (new File(path)).exists();
    }

    /**
     * 得到指定目录/文件的父目录
     *
     * @param path d:\a\b
     * @return d:\a\
     */
    public static String getDir(String path) {
        int idx0 = path.lastIndexOf(FILE_SEPARATOR);
        int idx1 = path.lastIndexOf("/");
        int idx = Math.max(idx0, idx1);
        return path.substring(0, idx + 1);
    }


    /**
     * copy file
     *
     * @param srcPath
     * @param destPath
     * @return
     * @throws Exception
     */
    public static boolean copy(String srcPath, String destPath) throws Exception {
        return copy(new File(srcPath), new File(destPath));
    }


    /**
     * copy file
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws Exception
     */
    public static boolean copy(File srcFile, File destFile) throws Exception {
        if (!srcFile.exists()) {
            throw new Exception("Cannot find srcFile:" + srcFile.getAbsolutePath());
        }

        boolean result = false;

        FileInputStream is = null;                                                   //置源流
        FileOutputStream os = null;                                                     //置目标流
        try {
            mkdirIfNotExist(destFile.getParent());
            is = new FileInputStream(srcFile);               //创建输入流
            os = new FileOutputStream(destFile);                     //创建输出流
            byte[] buffer = new byte[4096];                             //保存文件内容到buffer
            int bytes_read;                                                         //缓冲区大小
            while ((bytes_read = is.read(buffer)) != -1) {    //读直到文件末尾
                //写入buffer
                os.write(buffer, 0, bytes_read);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {                                                                         //关闭流（永远不要忘记）
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (os != null) try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 拷贝文件
     *
     * @param srcDirPath
     * @param desDirPath
     * @throws IOException
     */
    public static void copyFiles(String srcDirPath, String desDirPath) throws IOException {
        mkdirIfNotExist(desDirPath);

        Runtime.getRuntime().exec("cmd.exe /c start copy " + srcDirPath + "\\*.* " + desDirPath);
    }

    public static String removeNonValidChar(String filename) {
        return filename.replace("\\", "").replace("/", "")
                .replace(":", "").replace("*", "")
                .replace("?", "").replace("\"", "")
                .replace("<", "").replace(">", "")
                .replace("|", "");
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {
            flag = file.delete();
            if (!flag) {
                System.gc();
                flag = file.delete();
            }
        }
        return flag;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在  
        if (!file.exists()) {  // 不存在返回 false  
            return flag;
        } else {
            // 判断是否为文件  
            if (file.isFile()) {  // 为文件时调用删除文件方法  
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法  
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件  
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录  
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录  
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 读取配置文件
     */
    public static Properties loadProperties(String path) {
        Properties p = new Properties();
        InputStream is = null;
        //读取配置文件
        try {
            is = FileUtil.class.getClassLoader().getResourceAsStream(path);
            p.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return p;
    }


    /**
     * 为目录加上系统分隔符
     *
     * @param dir
     * @return
     */
    public static String appendSeparater(String dir) {
        if (dir == null) {
            return null;
        }

        if (dir.endsWith("\\") || dir.endsWith("/")) {
            // ok
        } else {
            dir += "/";
        }

        return dir;
    }

    /**
     * 将流写入到文件
     *
     * @param inputStream
     * @param filePath
     * @return
     */
    public static boolean writeStreamToFile(InputStream inputStream, String filePath) {
        //写入状态初始化为非
        boolean writeStatus = false;
        //若输入流为空，直接返回false
        if (inputStream == null) {
            return writeStatus;
        }
        //新建文件
        File storeFile = new File(filePath);
        mkdirIfNotExist(storeFile.getParent());
        //新建1KB的缓冲
        byte buffer[] = new byte[1024];
        //一次从流中读入的长度
        int length = 0;
        try {
            //打开文件的输出流
            FileOutputStream output = new FileOutputStream(storeFile);
            //若从输入流中读取的文件长度不为-1，将缓冲里的流写入文件中
            while ((length = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            //写入状态设为真
            writeStatus = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //返回写入状态
        return writeStatus;
    }


    public static String getPathFromResource(String resourcePath) {
        URL resource = FileUtil.class.getClassLoader().getResource(resourcePath);
        String temp = resource.getPath();
        if (temp.contains(":")) {
            if (temp.startsWith("/")) {
                return temp.substring(1);
            }
        }
        return temp;
    }


    public static void write(String filePath, byte[] bytes) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
            os.write(bytes);
        } catch (Exception e) {
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }


    /**
     * 读取文件内容到字节数组
     */
    public static byte[] read(String filePath) throws Exception {
        File file = new File(filePath);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new Exception("文件太大，超过" + Integer.MAX_VALUE + "字节");
        }

        FileInputStream is = null;
        try {
            is = new FileInputStream(filePath);
            byte[] bytes = new byte[(int) length];
            is.read(bytes);
            return bytes;
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    /**
     * 在文件中数关键字，一行最多计一次
     * @param keyword
     * @param path
     * @return
     */
    public static int countInLine(String keyword, String path) throws Exception {
        Stopwatch sw = new Stopwatch();
        File file = new File(path);
        double gbs = file.length() / (1024 * 1024 * 1024.0);
        System.out.println("file size: " + NumberUtil.format(gbs, 3, 0) + "GB");

        BufferedReader reader = null;
        int count = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            while (true) {
                String strLine = reader.readLine();
                if (strLine == null) break;
                if(strLine.contains(keyword)) {
                    count++;
                    if(count % 1000 == 0) {
                        System.out.println(count+" found");
                    }
                }
            }
        } catch (Exception e) {
            count=-1;
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        System.out.println(sw.stopAndGetFormattedTime());
        System.out.println(count + " found");
        double gbsPerSecond = gbs / sw.getSeconds();
        System.out.println(NumberUtil.format(gbsPerSecond, 2, 0) + " GB/s");
        return count;
    }

    /**
     * 获得文件夹（包含子文件夹）下的所有文件
     * @param folder
     * @return
     */
    public static List<String> getFilesRecursively(String folder){
        List<String> file_list = new ArrayList<String>();
        // get file list where the path has
        File file = new File(folder);
        // get the folder list
        File[] array = file.listFiles();

        for(int i=0;i<array.length;i++){
            if(array[i].isFile()){
                file_list.add(array[i].getPath());
            }else if(array[i].isDirectory()){
                file_list.addAll(getFilesRecursively(array[i].getPath()));
            }
        }
        return file_list;
    }

    public static void main(String[] args) throws Exception {
//        int count = countInLine("INSERT INTO", "D:\\Wikipedia\\en20151102\\enwiki-20151102-pagelinks.sql");
        countLines("D:\\Datasets_of_EL\\GoogleCrossWikiDictionary\\dictionary");
    }
}


