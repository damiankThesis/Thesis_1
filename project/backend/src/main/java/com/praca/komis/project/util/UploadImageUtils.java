package com.praca.komis.project.util;

import com.github.slugify.Slugify;
import org.apache.commons.io.FilenameUtils;

public class UploadImageUtils {

    public static String slugifyFilename(String filename) {
        Slugify slugify = new Slugify();
        String slugifyName = slugify
                .withCustomReplacement("_", "-")
                .slugify(FilenameUtils.getBaseName(filename));
        return slugifyName + "." + FilenameUtils.getExtension(filename);
    }
}
