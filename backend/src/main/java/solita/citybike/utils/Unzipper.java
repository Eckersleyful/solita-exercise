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

    /**
     * Returns a list of files found at the constant of ZIP_LOCATION.
     * In case of wanting to scan a different directory, a wildcard has to be present
     * in the given path.
     * e.g <NEW_PATH/*>
     *
     * @return List of all files found at ZIP_LOCATION constant. Empty Resource list
     * in case of failing the lookup
     */

    private Resource[] getAllCsvZips(){

        PathMatchingResourcePatternResolver pattern = new PathMatchingResourcePatternResolver();

        try {
            return pattern.getResources(ZIP_LOCATION);

        } catch (IOException e) {
            Logger.error(e);
        }

        return new Resource[0];

    }

    public ArrayList<String[]> getJourneysFromCsv(){

        Resource[] zipsInDir = getAllCsvZips();
        Logger.info("Found " + zipsInDir.length + " files in the target directory");

        ArrayList<String []> csvList = new ArrayList<>();

        //Iterate through all the found files and return their content as an array of Strings
        for(Resource f : zipsInDir){

            csvList.add(getZipAsStringArray(f));
        }

        Logger.info("From those " + zipsInDir.length + " zipped files, managed to unzip and read "
            + csvList.size()
        );

        return csvList;
   }

    private String[] getZipAsStringArray(Resource f) {

        try {

            SevenZFile sevenZFile = new SevenZFile(new File(f.getURI()));

            ArchiveEntry entry = null;

            while((entry = sevenZFile.getNextEntry()) != null){

                //Create a byte array with the size of the currently read entry
                byte[] fileContent = new byte[(int) entry.getSize()];

                //Read the file buffer equal to the amount of bytes in the entry
                sevenZFile.read(fileContent, 0, fileContent.length);

                /*
                Create a new String from the read bytes and split it from newline operator to
                 get each line of the file as their own element
                 */
                return new String(fileContent, StandardCharsets.UTF_8).split("\n");

            }

        } catch (IOException e) {
            Logger.error(e);
        }

        return null;
    }


}
