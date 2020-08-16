package com.maumjido.springboot.template.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

public class FileUtil {

  static Logger logger = Logger.getLogger(FileUtil.class.getName());
  private static final int BUFFER_SIZE = 1024;

  /**
   * 텍스트 포맷의 파일을 읽어 String 문자열로 반환한다.
   * 
   * @param filePath 파일경로
   * @return String
   * @throws IOException
   */
  public static String readFileForText(String filePath) throws IOException {
    return new String(readFileForBinary(filePath));
  }

  /**
   * 텍스트 및 바이너리 포맷의 파일을 읽어 Bynary Data(byte array) 로 반환한다.
   * 
   * @param filePath 파일경로
   * @return byte[]
   * @throws IOException
   */
  public static byte[] readFileForBinary(String filePath) throws IOException {
    InputStream is = null;
    ByteArrayOutputStream os = null;

    int size = 1024;
    byte[] buffer = new byte[size];
    int length = -1;
    byte[] retValue = null;
    try {
      is = new FileInputStream(filePath);
      os = new ByteArrayOutputStream();

      while ((length = is.read(buffer)) != -1) {
        os.write(buffer, 0, length);
      }
    } catch (IOException ioe) {
      throw ioe;
    } finally {
      if (is != null)
        is.close();
      if (os != null) {
        retValue = os.toByteArray();
        os.close();
      }
    }

    return retValue;
  }

  public static String readFile(InputStream is) throws IOException {
    ByteArrayOutputStream os = null;

    int size = 1024;
    byte[] buffer = new byte[size];
    int length = -1;
    String retValue = null;

    try {
      os = new ByteArrayOutputStream();

      while ((length = is.read(buffer)) != -1) {
        os.write(buffer, 0, length);
      }
    } catch (IOException ioe) {
      throw ioe;
    } finally {
      if (is != null)
        is.close();
      if (os != null) {
        retValue = os.toString();
        os.close();
      }
    }

    return retValue;
  }

  /**
   * 하위의 모든 파일과 디렉토리를 삭제한다.
   * 
   * @param file 삭제할 파일 또는 디렉토리
   */
  public static void removeDir(File file) {
    if (file.exists()) {
      if (file.isDirectory()) {
        File[] list = file.listFiles();
        for (File f : list) {
          removeDir(f);
        }
        file.delete();
      } else {
        file.delete();
      }
    }
  }

  /**
   * 하위의 모든 파일과 디렉토리를 삭제한다.
   * 
   * @param fileStr
   */
  public static void removeDir(String fileStr) {
    removeDir(new File(fileStr));
  }

  /**
   * 파일을 삭제한다.
   * 
   * @param filename 전체경로를 포함하는 삭제할 파일명
   */
  public static void removeFile(String filename) {
    File file = new File(filename);

    if (file.exists()) {
      file.delete();
    }
  }

  /**
   * 디렉토리로부터 파일목록을 가져온다. (하위 디렉터리 포함)
   * 
   * @param path 파일을 가져올 디렉토리
   * @return File[]
   */
  public static File[] getFileListFromDir(String path) {
    File dir = new File(path);
    File[] list = null;

    if (dir.exists()) {
      if (dir.isDirectory()) {
        list = dir.listFiles();
      }
    }
    return list;
  }

  /**
   * 디렉토리로부터 파일목록을 가져온다. (하위 디렉터리&파일 포함)
   * 
   * @param path 파일을 가져올 디렉토리
   * @return File[]
   */
  public static List<File> getFileListFromDirWithSubFile(String path, FileFilter fileFilter) {
    File dir = new File(path);
    List<File> fileList = new ArrayList<File>();
    if (dir.exists()) {
      if (dir.isDirectory()) {
        for (File subFile : dir.listFiles(fileFilter)) {
          fileList.add(subFile);
          if (subFile.isDirectory())
            fileList.addAll(getFileListFromDirWithSubFile(subFile.getAbsolutePath(), fileFilter));
        }
      }
    }
    return fileList;
  }

  public static String[] getAllFileStrListFromDir(String path) {

    File dir = new File(path);
    String[] list = null;

    if (dir.exists()) {
      if (dir.isDirectory()) {
        list = dir.list();
      }
    }
    return list;
  }

  /**
   * 디렉토리로부터 파일목록을 가져온다. (하위 디렉터리 미포함)
   * 
   * @param path
   * @return
   */
  public static String[] getFileStrListFromDir(String path) {
    File dir = new File(path);
    String[] lists = null;
    List<String> files = new ArrayList<String>();

    if (dir.exists()) {
      if (dir.isDirectory()) {
        lists = dir.list();
      }
    }

    for (String list : lists) {
      File file = new File(path + File.separator + list);
      if (file.isDirectory())
        continue;
      files.add(list);
    }
    return (String[]) files.toArray(new String[0]);
  }

  /**
   * fromPath 하위의 모든 경로들을 basePath 를 기준으로한 상대경로로 가져온다.
   * 
   * return String[]
   */
  public static String[] getAllSubRelDirs(String fromPath, String basePath) {
    String[] toPath = null;
    ArrayList<Object> toPathArray = new ArrayList<Object>();

    File folder = new File(fromPath);
    toPathArray.add(folder.getAbsolutePath());

    getAllSubDirs(fromPath, toPathArray);

    toPath = new String[toPathArray.size()];
    toPathArray.toArray(toPath);

    for (int i = 0; i < toPath.length; i++) {
      toPath[i] = ltrim(toPath[i], basePath);
      toPath[i] = toPath[i].replace('\\', '/');
      toPath[i] = ltrim(toPath[i], "/");
      if (!toPath[i].endsWith("/")) {
        toPath[i] = toPath[i] + "/";
      }
    }

    return toPath;
  }

  private static String ltrim(String source, String trimStr) {
    if (source != null && source.startsWith(trimStr)) {
      return source.substring(trimStr.length());
    }
    return source;
  }

  /**
   * 주어진 경로로 부터 하위의 모든 경로들을 ArrayList 에 넣는다.
   */
  public static void getAllSubDirs(String fromPath, List<Object> pathArray) {
    File folder = new File(fromPath);
    File[] subFiles = folder.listFiles();

    for (int i = 0; i < subFiles.length; i++) {
      if (subFiles[i].isDirectory()) {
        pathArray.add(subFiles[i].getAbsolutePath());

        getAllSubDirs(subFiles[i].getAbsolutePath(), pathArray);
      }
    }
  }

  /**
   * 주어진 경로로 부터 하위의 모든 파일들을 상대 경로로 반환한다.
   * 
   * @param fromPath
   * @throws Exception
   * @reuturn List<String>
   */
  public static List<String> getAllSubFiles(String fromPath) throws Exception {
    return getAllSubFiles(fromPath, null, null);
  }

  /**
   * 주어진 경로로 부터 하위의 특정확장자를 가진 파일들을 상대 경로로 반환한다.
   * 
   * @param fromPath
   * @param ext      확장자 명 List<String>
   * @throws Exception
   * @reuturn List<String>
   */
  public static List<String> getAllSubFiles(String fromPath, List<String> ext) throws Exception {
    return getAllSubFiles(fromPath, null, ext);
  }

  /**
   * 주어진 클래스패스 경로로 부터 하위의 특정확장자를 가진 파일들을 클래스패스 경로로 반환한다.
   * 
   * @param fromPath   가져올 클래스패스 경로
   * @param parentPath 부모 클래스패스 경로
   * @param ext        확장자 명 List<String>
   * @return
   * @throws Exception
   */
  private static List<String> getAllSubFiles(String fromPath, String parentPath, List<String> ext) throws Exception {
    if (parentPath == null)
      parentPath = "";
    else
      parentPath += "/";

    String dirName = parentPath + fromPath;
    File folder = new File(getClasspathRootAbsPath(dirName));
    File[] subFiles = folder.listFiles();
    List<String> fileList = new ArrayList<String>();

    if (ext == null) {
      for (int i = 0; i < subFiles.length; i++) {
        if (subFiles[i].isDirectory()) {
          fileList.addAll(getAllSubFiles(subFiles[i].getName(), dirName, null));
        } else {
          fileList.add(dirName + "/" + subFiles[i].getName());
        }
      }
    } else {
      for (int i = 0; i < subFiles.length; i++) {
        if (subFiles[i].isDirectory()) {
          fileList.addAll(getAllSubFiles(subFiles[i].getName(), dirName, ext));
        } else {
          String[] fs = subFiles[i].getName().split("\\.");
          if (fs.length > 0 && ext.contains(fs[1]))
            fileList.add(dirName + "/" + subFiles[i].getName());
        }
      }
    }

    return fileList;
  }

  /**
   * 텍스트 포맷의 파일을 줄단위로 읽어 배열로 반환한다.
   * 
   * @param filePath 파일경로
   * @return String[]
   * @throws IOException
   */
  public static String[] readFileByLine(String filePath) throws IOException {

    ArrayList<Object> resultList = new ArrayList<Object>();
    String[] result = null;

    String line = "";
    BufferedReader in = null;

    try {
      in = new BufferedReader(new FileReader(filePath));

      while ((line = in.readLine()) != null) {
        if (!line.trim().equals("")) {
          resultList.add(line.trim());
        }
      }

    } catch (IOException ioe) {
      throw ioe;
    } finally {
      if (in != null)
        in.close();
    }

    result = new String[resultList.size()];
    resultList.toArray(result);

    return result;
  }

  /**
   * 텍스트 포맷의 파일 마지막에 문자열을 추가한다.
   * 
   * @param filePath 파일경로
   * @param str      추가할 문자열
   * @throws IOException
   */
  public static void addLineToFile(String filePath, String str) throws IOException {

    PrintWriter pw = null;

    try {
      pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
      pw.println(str);
      pw.flush();

    } catch (IOException ioe) {
      throw ioe;
    } finally {
      if (pw != null)
        pw.close();
    }
  }

  /**
   * 파일을 새로 등록한다.
   * 
   * @param filePath 파일경로
   * @param String   [] 문자열 목록
   * @throws IOException
   */
  public static void updateFile(String filePath, String[] strList) throws IOException {

    PrintWriter pw = null;

    try {
      pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)));
      for (int i = 0; i < strList.length; i++) {
        pw.println(strList[i]);
      }
      pw.flush();

    } catch (IOException ioe) {
      throw ioe;
    } finally {
      if (pw != null)
        pw.close();
    }
  }

  public static File rename(File file, String newName) throws Exception {

    if (file == null || !file.exists())
      return null;
    if (newName == null || newName.equals(""))
      return file;

    File newFile = new File(file.getParent() + File.separator + newName);
    file.renameTo(newFile);

    return newFile;
  }

  public static File copy(String src, String targetDir) throws Exception {

    File srcFile = new File(src);
    if (!srcFile.exists())
      return null;

    File tgtDir = new File(targetDir);
    if (!tgtDir.exists())
      throw new IOException(targetDir + " not exists");
    if (!tgtDir.isDirectory())
      throw new IOException(targetDir + " not dir");

    File copied = null;
    if (srcFile.isDirectory()) {
      copied = new File(targetDir + File.separator + srcFile.getName());
      copied.mkdirs();
      String[] subs = srcFile.list();
      for (int i = 0; i < subs.length; i++) {
        copy(src + File.separator + subs[i], copied.getAbsolutePath());
      }
    } else {
      copied = copyFile(srcFile, targetDir);
    }

    return copied;
  }

  public static File copyTree(String dir, String targetDir) throws Exception {
    return copy(dir, targetDir);
  }

  public static File copyFile(File src, String targetDir) throws Exception {

    if (!src.exists())
      return null;

    File tDir = new File(targetDir);
    if (!tDir.exists()) {
      tDir.mkdirs();
    } else if (!tDir.isDirectory()) {
      throw new IOException(targetDir + " not dir");
    }

    String fileName = src.getName();

    if (fileName != null && !fileName.equals("") && targetDir != null && !targetDir.equals("")) {
      while (existFile(fileName, targetDir)) {
        fileName = renameFile(fileName);
      }
    }

    File tFile = new File(targetDir + File.separator + fileName);

    InputStream in = new FileInputStream(src);
    OutputStream out = new FileOutputStream(tFile);

    int read = 0;
    byte[] buf = new byte[BUFFER_SIZE];
    while ((read = in.read(buf)) != -1)
      out.write(buf, 0, read);

    in.close();
    out.flush();
    out.close();
    fileName = "";

    return tFile;
  }

  public static boolean existFile(String fileName, String targetDir) throws Exception {

    boolean result = false;
    File[] files = getFileListFromDir(targetDir);
    for (int i = 0; files != null && i < files.length; i++) {
      if (files[i].getName().equals(fileName)) {
        result = true;
        break;
      }
    }
    return result;
  }

  public static boolean existFile(String fileFullPath) {
    File file = new File(fileFullPath);
    return file.exists();
  }

  public static boolean existDirectory(String fullPath) {
    File file = new File(fullPath);
    return file.isDirectory();
  }

  public static String renameFile(String fileName) throws Exception {

    if (fileName == null || fileName.equals(""))
      return fileName;
    int lastIdx = fileName.lastIndexOf(".");

    if (lastIdx == -1) {
      fileName = fileName + "1";
    } else {
      fileName = fileName.substring(0, lastIdx) + "1" + fileName.substring(lastIdx);
    }

    return fileName;
  }

  public static void move(String src, String targetDir) throws Exception {

    if (src == null || src.equals("") || targetDir == null || targetDir.equals(""))
      return;

    File srcFile = new File(src);
    if (!srcFile.exists())
      return;

    File targetPDir = new File(targetDir);
    if (!targetPDir.exists())
      throw new IOException("Destination dir not exists");

    File targetFile = new File(targetDir + File.separator + srcFile.getName());
    srcFile.renameTo(targetFile);
  }

  public static void moveTree(String src, String targetDir) throws Exception {
    move(src, targetDir);
  }

  public static void makeAndMove(String src, String target) throws Exception {

    if (src == null || src.equals("") || target == null || target.equals(""))
      return;

    File srcFile = new File(src);
    if (!srcFile.exists())
      return;

    File targetPDir = new File(target);
    if (!targetPDir.exists())
      targetPDir.mkdirs();

    File targetFile = new File(target + File.separator + srcFile.getName());
    srcFile.renameTo(targetFile);
  }

  public static void makeAndMoveTree(String src, String target) throws Exception {
    makeAndMove(src, target);
  }

  public static void remove(String path) throws Exception {

    File deleteFile = new File(path);

    if (!deleteFile.exists())
      return;

    if (deleteFile.isDirectory()) {
      String[] subs = deleteFile.list();
      for (int i = 0; i < subs.length; i++) {
        removeTree(path + File.separator + subs[i]);
      }
      if (!deleteFile.delete())
        throw new IOException("Fail to remove " + path);
    } else {
      if (!deleteFile.delete())
        throw new IOException("Fail to remove " + path);
    }
  }

  public static void removeTree(String path) throws Exception {
    remove(path);
  }

  public static void removeEmpty(String path) throws Exception {

    File deleteFile = new File(path);

    if (!deleteFile.exists())
      return;

    if (deleteFile.isDirectory()) {
      String[] subs = deleteFile.list();
      for (int i = 0; i < subs.length; i++) {
        removeEmpty(path + File.separator + subs[i]);
      }
      if (deleteFile.list().length == 0 && !deleteFile.delete())
        throw new IOException("Fail to remove " + path);
    } else {
      if (deleteFile.length() == 0 && !deleteFile.delete())
        throw new IOException("Fail to remove " + path);
    }
  }

  public static long size(String src) throws Exception {

    long result = 0;

    File file = new File(src);
    if (!file.exists())
      return 0;

    if (file.isFile()) {
      result = file.length();
    } else {
      File[] subs = file.listFiles();
      for (int i = 0; i < subs.length; i++) {
        if (subs[i].isFile())
          result += subs[i].length();
      }
    }

    return result;
  }

  public static long sizeTree(String src) throws Exception {

    long result = 0;

    File file = new File(src);
    if (!file.exists())
      return 0;

    if (file.isFile()) {
      result = file.length();
    } else if (file.isDirectory()) {
      String[] subs = file.list();
      for (int i = 0; subs != null && i < subs.length; i++) {
        result += sizeTree(src + File.separator + subs[i]);
      }
    }

    return result;
  }

  public static int countFile(String src) throws Exception {

    int result = 0;

    File file = new File(src);
    if (!file.exists())
      return 0;

    if (file.isFile()) {
      result = 1;
    } else if (file.isDirectory()) {
      File[] subs = file.listFiles();
      for (int i = 0; i < subs.length; i++) {
        if (subs[i].isFile())
          result++;
      }
    }

    return result;
  }

  public static int countFileTree(String src) throws Exception {

    int result = 0;

    File file = new File(src);
    if (!file.exists())
      return 0;

    if (file.isFile()) {
      result = 1;
    } else if (file.isDirectory()) {
      String[] subs = file.list();
      for (int i = 0; i < subs.length; i++) {
        result += countFileTree(src + File.separator + subs[i]);
      }
    }

    return result;
  }

  /**
   * path에 해당되는 디렉터리 생성
   * 
   * @param path
   * @return
   */
  public static boolean makeDirectory(String path) {
    boolean success = true;
    File directory = new File(path);

    if (!directory.exists()) {
      success = directory.mkdirs();
    }

    return success;
  }

  /**
   * 클래스패스 기준으로 절대경로를 가져온다.
   * 
   * @return
   */
  public static String getClasspathRootAbsPath(String classpath) throws Exception {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    URL url = loader.getResource(classpath);
    if (url == null || url.getFile() == null)
      throw new Exception(classpath + " is not exists.");

    logger.fine("classpath : " + classpath);
    logger.fine("url.getFile() : " + url.getFile());
    File file = new File(url.getFile());
    if (file == null || !file.exists())
      throw new Exception(classpath + " is not exists.");
    String path = file.getAbsolutePath();
    return path;
  }

  /**
   * 해당 클래스의 패키지내 클래스 목록을 가져온다
   * 
   * @param packageName 패키지명
   * @return
   * @throws IOException
   */
  public static List<String> getClassList(Class<?> clz) {
    List<String> classList = new ArrayList<String>();
    logger.fine("clz.getResource : " + clz.getResource(""));
    File ncpCommonUtil = null;

    try {
      ncpCommonUtil = new File(clz.getResource("").getFile());
    } catch (Exception e) {
      logger.severe(e.toString());
      return null;
    }

    if (ncpCommonUtil.exists() && ncpCommonUtil.isDirectory()) {
      logger.fine("is Classes");
      String packageName = FileUtil.class.getPackage().getName();
      for (String className : ncpCommonUtil.list()) {
        classList.add(packageName + "." + className.replace(".class", ""));
      }
    } else {
      logger.fine("is Jar");
      String path = ncpCommonUtil.getPath().substring(6).replace('\\', '/');
      String[] info = path.split("!/");
      JarFile jar = null;
      try {
        jar = new JarFile(info[0]);
      } catch (IOException e1) {
        logger.severe(e1.toString());
      }
      Enumeration<JarEntry> e = jar.entries();
      while (e.hasMoreElements()) {
        String className = e.nextElement().getName();
        if (className.startsWith(info[1]) && className.length() > 16) {
          classList.add(className.replace("/", ".").replace(".class", ""));
        }
      }
    }

    return classList;
  }

  /**
   * Jar파일을 순회하면서 지정한 확장자 파일들 이름을 키로 파일의 InputStream을 value로 담는다.
   * 
   * @param classpath
   * @param extList
   * @return
   */
  public static Map<String, InputStream> getFileListInJar(String classpath, List<String> extList) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    URL url = loader.getResource(classpath);
    String path = url.getFile().substring(6).replace('\\', '/');
    String[] info = path.split("!/");
    JarFile jar = null;
    Map<String, InputStream> result = new HashMap<String, InputStream>();
    try {
      jar = new JarFile(info[0]);

    } catch (IOException e1) {
    }
    Enumeration<JarEntry> e = jar.entries();
    while (e.hasMoreElements()) {
      ZipEntry ze = e.nextElement();
      String className = ze.getName();

      if (className.startsWith(info[1])) {
        try {
          String[] fs = className.split("\\.");
          if (fs.length > 1 && extList.contains(fs[1])) {
            logger.fine(className);
            result.put(className, jar.getInputStream(ze));
          }

        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    }

    return result;
  }

  public static JarFile getJarFile(String classpath) throws IOException {
    logger.fine("getJarFile - classpath - " + classpath);
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    URL url = loader.getResource(classpath);

    logger.fine("getJarFile - url.getFile() - " + url.getFile());
    String path = url.getFile().replace('\\', '/').replaceFirst("^file:", "");

    String[] info = path.split("!/");
    JarFile jar = null;
    try {
      jar = new JarFile(info[0]);

    } catch (IOException e1) {
      logger.severe(e1.toString());
      throw e1;
    }

    return jar;
  }
}
