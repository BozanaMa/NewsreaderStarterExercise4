package at.ac.fhcampuswien.newsanalyzer.downloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public abstract class Downloader {

    public static final String HTML_EXTENSION = ".html";
    public static final String DIRECTORY_DOWNLOAD = "./download/";

    public abstract int process(List<String> urls);

    public String saveUrl2File(String urlString) {
        InputStream is = null;
        OutputStream os = null;
        String fileName = "";
        try {
            URL url4download = new URL(urlString);
            is = url4download.openStream();

            fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
            if (fileName.isEmpty()) {
//                fileName = url4download.getHost() + HTML_EXTENSION;
                fileName = url4download.getPath() + HTML_EXTENSION;
            }

            fileName = fileName.replaceAll("[:\\\\/*?|<>\"]", "");
            if (!fileName.endsWith(HTML_EXTENSION)){
                fileName = fileName + HTML_EXTENSION;
            }

            os = new FileOutputStream(DIRECTORY_DOWNLOAD + fileName);

            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.printf("\nDownloading file %s was not successful, please try again later!\n", fileName);
            System.err.println(e.getMessage());
        } finally {
            try {
                Objects.requireNonNull(is).close();
                Objects.requireNonNull(os).close();
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println("An error occurred!");
                System.err.println(e.getMessage());
            }
        }
        return fileName;
    }


}
