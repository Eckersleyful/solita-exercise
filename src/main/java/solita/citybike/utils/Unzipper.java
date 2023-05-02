package solita.citybike.utils;

import org.tinylog.Logger;
import solita.citybike.models.BikeJourney;

import java.io.File;
import java.net.URL;
import java.util.List;

public class Unzipper {


    private final static String ZIP_LOCATION = "resources/csv/";


    public static File[] getAllCsvZips(){

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        URL url = loader.getResource(ZIP_LOCATION);
        String path = url.getPath();
        return new File(path).listFiles();


    }

    public static List<BikeJourney> getJourneysFromCsv(){

        File[] filesInDir = getAllCsvZips();
        System.out.println(filesInDir.length);
        for(File f : filesInDir){
            Logger.debug(f.getName());
        }

        return null;
   }





}
