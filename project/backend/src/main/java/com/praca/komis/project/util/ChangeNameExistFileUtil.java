package com.praca.komis.project.util;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class ChangeNameExistFileUtil {

    public static String renameIfExist(Path uploadDir, String filename) {
        if(Files.exists(uploadDir.resolve(filename)))
            return renameCheck(uploadDir, filename);

        return filename;
    }

    private static String renameCheck(Path uploadDir, String filename) {
        String newFilename = renameFilename(filename);

        if(Files.exists(uploadDir.resolve(newFilename)))
            newFilename = renameCheck(uploadDir, newFilename);  //recursive check

        return newFilename;
    }

    private static String renameFilename(String filename) {
        String baseName = FilenameUtils.getBaseName(filename);
        String[] split = baseName.split("-(?=[0-9]+$)");    //separate filename
        int counter = split.length>1 ? Integer.parseInt(split[1])+1 : 1;    //increase index of filename if exits
        return split[0] + "-" + counter + "." + FilenameUtils.getExtension(filename);
    }
}
