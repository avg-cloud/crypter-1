package com.mannea;

import java.io.File;
import java.io.IOException;

/**
 * Created by mannea on 1/22/15.
 */
public class Main {

    public static void main(String[] args) {

        FileIO fileIO = new FileIO();

        if (args.length == 2) {
            if (args[0].equals("-e")) {

                File file = new File(args[1]);
                fileIO.encryptFile(file);

            } else if (args[0].equals("-d")) {
                try {
                    File file = new File(args[1]);
                    fileIO.decryptFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                prtinHelp();
            }
        } else {
            prtinHelp();
        }
    }

    private static void prtinHelp() {
        String help = "Available options are:: \n" +
                "-e [file to encrypt]\n" +
                "-d [file to decrypt] \n" +
                "\n" +
                "Ex:  \n" +
                "Java -jar crypter.jar -e file.doc" +
                "Java -jar crypter.jar -d file.doc.crypter" +
                "\n\n" +
                "File must be .decrypter to decrypt, and a matching .decrypter.key must be in the same directory as the .decrypter";
        System.out.println(help);
    }

}
