package com.lang.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipUtil {

    private static Logger log = Logger.getLogger(ZipUtil.class);

    public static boolean doZip(String[] files, String zipFileName) {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFileName));
            for (String file : files) {
                File f = new File(file);
                doZip(out, f, f.getName());
            }

            return true;
        } catch (IOException e) {
            log.info("doZip error", e);

            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public static boolean doZip(String filesDirPath, String zipFilePath) {
        try {
            doZip(new File(filesDirPath), zipFilePath);

            return true;
        } catch (IOException e) {
            log.info("doZip error", e);

            return false;
        }
    }

    private static void doZip(File inputFile, String zipFileName) throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFileName));
            doZip(out, inputFile, "");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static void doZip(ZipOutputStream out, File f, String base) throws IOException {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                doZip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            while ((in.read(buffer)) != -1) {
                out.write(buffer);
            }
            in.close();
        }
    }

    public static void main(String[] args) {
        unZip("D:\\wara\\wara.zip", "D:\\warb\\", false);
    }

    @SuppressWarnings("rawtypes")
    public static boolean unZip(String srcFile, String dest, boolean deleteFile) {
        try {
            File file = new File(srcFile);
            if (!file.exists()) {
                log.info("unZip error,file not exists.");
                return false;
            }
            ZipFile zipFile = new ZipFile(file);
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) e.nextElement();
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(dest + name);
                    f.mkdirs();
                } else {
                    File f = new File(dest + zipEntry.getName());
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                    InputStream is = zipFile.getInputStream(zipEntry);
                    FileOutputStream fos = new FileOutputStream(f);
                    int length = 0;
                    byte[] b = new byte[1024];
                    while ((length = is.read(b, 0, 1024)) != -1) {
                        fos.write(b, 0, length);
                    }
                    is.close();
                    fos.close();
                }
            }

            if (zipFile != null) {
                zipFile.close();
            }

            if (deleteFile) {
                file.deleteOnExit();
            }

            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
