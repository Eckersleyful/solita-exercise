package solita.citybike.utils;


import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Unzipper {


    private static final String ZIP_LOCATION = "csv/*";



    /**
     * Returns a list of files found at the constant of ZIP_LOCATION.
     * In case of wanting to scan a different directory, a wildcard has to be present
     * in the given path.
     * e.g <NEW_PATH/*>
     *
     * @return List of all files found at ZIP_LOCATION constant. Empty Resource list
     * in case of failing the lookup
     */

    private static Resource[] getAllCsvZips(){

        PathMatchingResourcePatternResolver pattern = new PathMatchingResourcePatternResolver();

        try {
            return pattern.getResources(ZIP_LOCATION);

        } catch (IOException e) {
            Logger.error(e);
        }

        return new Resource[0];

    }

    /**
     * Iterates through the files returned by getAllCsvZips()
     * and calls getZipAsStringArray on each file, to receive
     * its content as unzipped String arrays.
     *
     * @return
     */

    public ArrayList<String[]> getJourneysFromCsv(){

        Resource[] zipsInDir = getAllCsvZips();
        Logger.info("Found " + zipsInDir.length + " files in the target directory");

        ArrayList<String []> csvList = new ArrayList<>();

        //Iterate through all the found files and return their content as an array of Strings
        for(Resource f : zipsInDir){

            if(f != null){
                csvList.add(getZipAsStringArray(f));
            }

        }

        Logger.info("From those " + zipsInDir.length + " zipped files, managed to unzip and read "
            + csvList.size()
        );

        return csvList;
   }


    /**
     * Takes a file, unzips it and reads it's content into a String array
     * @param f The file to be unzipped and read
     * @return An array of strings, if reading fails, an empty array of strings.
     */
    private String[] getZipAsStringArray(Resource f) {


        Logger.info("Starting to unzip file " + f.getFilename());

        try {
            //The hackiest way ever to grab a zip file from inside the jar
            //Does not work from outside of jar context

            //We grab the absolute path ending from the found file
            String filePath = String.valueOf(f.getURL()).split("classes!")[1];

            //Create an InputStream from the filePath because we can't create a File object
            //directly from jar context
            InputStream in = getClass().getResourceAsStream(filePath);

            //Lord forgive me
            File newFile = new File("dummyPath");

            //Copy the InputStream content to the dummy File object
            FileUtils.copyInputStreamToFile(in, newFile);

            SevenZFile sevenZFile = new SevenZFile(newFile);
            ArchiveEntry entry = null;

            while ((entry = sevenZFile.getNextEntry()) != null) {

                //Create a byte array with the size of the currently read entry
                byte[] fileContent = new byte[(int) entry.getSize()];

                //Read the file buffer equal to the amount of bytes in the entry
                sevenZFile.read(fileContent, 0, fileContent.length);

                /*
                Create a new String from the read bytes and split it from newline operator to
                get each line of the file as their own element
                */
                sevenZFile.close();
                return new String(fileContent, StandardCharsets.UTF_8).split("\n");

            }

        } catch (IOException e) {
            Logger.error(e);
        }
        return new String[0];
    }


}
