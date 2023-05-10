package solita.citybike.utils;


import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.tinylog.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Unzipper {


    private final String ZIP_LOCATION = "csv/*";


    private Resource[] getAllCsvZips(){

        PathMatchingResourcePatternResolver pattern = new PathMatchingResourcePatternResolver();

        try {
            return pattern.getResources(ZIP_LOCATION);


        } catch (IOException e) {
            Logger.error(e);
        }

        return null;

    }

    public ArrayList<String[]> getJourneysFromCsv(){

        Resource[] zipsInDir = getAllCsvZips();
        Logger.debug(zipsInDir.length);

        ArrayList<String []> csvList = new ArrayList<>();
        for(Resource f : zipsInDir){
            csvList.add(getZipAsStringArray(f));
        }

        Logger.debug(csvList.size());

        return csvList;
   }

    private String[] getZipAsStringArray(Resource f) {

        try {
            SevenZFile sevenZFile = new SevenZFile(new File(f.getURI()));

            ArchiveEntry entry = null;

            while((entry = sevenZFile.getNextEntry()) != null){

                byte[] fileContent = new byte[(int) entry.getSize()];

                sevenZFile.read(fileContent, 0, fileContent.length);
                return new String(fileContent, StandardCharsets.UTF_8).split("\n");
            }

        } catch (IOException e) {
            Logger.error(e);
        }

        return null;
    }


}
