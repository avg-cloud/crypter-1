package com.mannea;

import java.io.IOException;

/**
 * Created by mannea on 1/22/15.
 */
public class Main {

    public static void main(String[] args) {

        FileIO fileIO = new FileIO();

        if (args.length == 2) {
            if (args[0].equals("-e")) {

                try {
                    fileIO.encryptFile(args[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                prtinHelp();
            }
        } else if (args.length == 3) {
            if (args[0].equals("-d")) {
                try {
                    fileIO.decryptFile(args[1], args[2]);
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
                "-d [file to decrypt] [key]\n" +
                "\n" +
                "Ex:  Java -jar crypter.jar -d file.doc B688C7E90EE9FF758E3645B6D1E2F3E7ED87AB7A0434F40C38880E2EB6616DFC" +
                "\n\n";
        System.out.println(help);
    }

}
