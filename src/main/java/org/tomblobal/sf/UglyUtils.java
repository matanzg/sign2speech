package org.tomblobal.sf;

import java.io.File;

/**
 * Created by Matan on 9/12/2015.
 */
public class UglyUtils {

    public static void createFolderStructure(String path){
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
