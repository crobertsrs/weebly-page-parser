/*
parser.java

This tool uses jsoup to parse four kinds of elements from html files:
    - Links elements with "title" in the class. This is used to capture the titles of blog posts.
    - Div elements with "paragraph" in the class. This is used to capture the majority of text onctent on the page.
    - Link elements with "http" in the href. This is used to capture external links from the page.
    - iframe elements with "youtube" in the src. This is used to capture a link to embedded YouTube videos.

When run, the parsing code is run on each file found in RAWDIR, the directory of text files with html copied from unpublished Weebly sites using a browser's inspector developer tool.

The main customizable element is the WRITE_TO_FILE flag which creates and writes parsing output to files if true and prints to the console otherwise.
*/

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import org.jsoup.*;
import org.jsoup.helper.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class parse_p_a {
    public static void main(String[] args) throws IOException {

        // If true, make an output file for each input file (with the same name) and
        // save the parsed data there.
        // If false, print parsed content to the console.
        final Boolean WRITE_TO_FILE = true;

        final String RAWDIR = "../raw_pages/";
        final String PARSEDDIR = "../parsed_pages/";

        final File dir = new File(RAWDIR);
        final String FILENAMES[] = dir.list();

        for (int i = 0; i < FILENAMES.length; i++) {

            File input = new File(RAWDIR + FILENAMES[i]);
            Document doc = Jsoup.parse(input, "UTF-8");

            // See jsoup docs for parsing details
            // The *= selector used here finds elements with an attribute that contains the
            // value that follows it.
            Elements titles = doc.select("a[class*=title]");
            Elements ps = doc.select("div[class*=paragraph]");
            Elements links = doc.select("a[href*=http]");
            Elements yt = doc.select("iframe[src*=youtube]");

            // Writing to a file or printing to the console.
            // For each found element, print relevant information from it.
            if (WRITE_TO_FILE) {
                BufferedWriter out = new BufferedWriter(new FileWriter(PARSEDDIR + FILENAMES[i]));

                // Extra whitespace added for writing to files
                out.write("\nTitles\n\n");
                for (Element src : titles) {
                    String text = src.text();
                    if (text.length() > 0) {
                        out.write(src.text() + '\n');
                    }
                }
                out.write('\n');

                out.write("\n\nParagraph Content\n\n");
                for (Element src : ps) {
                    out.write(src.text() + '\n');
                }
                out.write('\n');

                out.write("\n\nExternal Links\n\n");
                for (Element src : links) {
                    out.write(src.text() + ": " + src.attr("href") + '\n');
                }
                out.write('\n');

                out.write("\n\nYouTube Videos\n\n");
                for (Element src : yt) {
                    out.write(src.attr("src") + '\n');
                }

                out.close();

            } else {
                System.out.print("\nTitles\n\n");
                for (Element src : titles) {
                    System.out.println(src.text());
                }

                System.out.print("\n\nParagraph Content\n\n");
                for (Element src : ps) {
                    System.out.println(src.text());
                }

                System.out.print("\n\nExternal Links\n\n");
                for (Element src : links) {
                    System.out.println(src.text() + ": " + src.attr("href"));
                }

                System.out.print("\n\nYouTube Videos\n\n");
                for (Element src : yt) {
                    System.out.println(src.attr("src"));
                }
            }
        }
    }
}