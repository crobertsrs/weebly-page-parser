# weebly-page-parser
General process and parser for extracting content from unpublished Weebly pages using [jsoup](https://github.com/jhy/jsoup/). 

## Why?
We ran into a situation in which we had two web pages made with web hosting platform Weebly ([website](https://www.weebly.com), [github](https://github.com/weebly)) that needed to be unpublished, reviewed, internally archived, and potentially deleted. We did not find an easy way to save a local copy of the unpublished sites using Weebly or other tools. This process and code provide a foundation for semi-automatic content extraction of unpublished pages.

## What It Does
As is, this tool extracts 4 kinds of text from each page: Titles of blog-like content, paragraph text content, external links, and embedded YouTube links.

**Titles** – Some pages on the site were set up like blogs in that there were a whole bunch of separate posts written and posted on different days. The name of each post will show up in this Titles section. The text of the blog posts will be in the next section and that should all be in the same order, so it’s straightforward to match up the title and the text of the post.
 
**Paragraph Content** – This should be all of the words that show up on each page (except blog titles, menus, etc.). Sometimes paragraph headings are on their own line and sometimes everything is all on one big line. It should be readable enough though.

**External Links** – Any link to a website that is not another page on the Weebly site should show up here along with the text that would have been highlighted. This includes buttons, linked text in the paragraph content, and probably other things too.
 
**YouTube Videos** – On Weebly sites, embedded YouTube videos seem to kind of be their own thing, so here is a section for them. Unfortunately, there usually isn’t much other contextual information about the videos, so you’ll just have to follow the links and see what they are.

## How to Use It

### Overview
The process has the following steps:
1. Installation
2. Manual Page Extraction
3. Parsing the HTML

### Installation
To run this code, you will need Java, this code, a copy of jsoup, and a bit of knowledge about how to get all of that to run. 

#### Java
You can check if you already have Java installed by opening a command prompt and typing:
```
java --version
```
You should see something like the following:
```
openjdk 11.0.2 2019-01-15
OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode)
```

#### Getting This Code
This process assumes the following directory structure:
```
somefolder
  |-jsoup.jar
  |-parsed_pages
  |-raw_pages
  |-weebly-page-parser
     |-README.md
     |-parser.java
```

In some folder on your computer, save a copy of jsoup (see below), make a folder for this code, then save a copy of or clone this repo into it.

You can download a copy of this code by clicking the code button, copying the url, then running the following command in the directory on your computer you want the code to go:
```
git clone https://github.com/rainierscholars/weebly-page-parser.git
```
See the [GitHub docs](https://docs.github.com) for detailed instructions.

#### jsoup
[jsoup](https://github.com/jhy/jsoup/) is a library for getting, parsing, and doing a lot of other things to HTML. This project just uses its parsing abilities. Visit [jsoup.org](https://jsoup.org/) to download the latest version or for instructions on cloning from GitHub. 

To use a .jar when using this program from the command line, you will need to include it as a classpath in both your compilation and execution. On OSX using ZSH and JDK11 and the jsoup jar one directory above, compilation and execution looked like this:
```
javac -cp .:../jsoup.jar parser.java
```
Later, when it is time to run the program, you will execute it like this:
```
java -cp .:../jsoup.jar parser
```

Alternatively, if you are using an IDE like [VSCode](https://code.visualstudio.com/), you should be able to add the .jar as a Java dependency, then right click parser.java and choose "run".

### Manual Page Extraction
The final prep step is actually getting the content to parse. This was done by opening the Weebly Site Editor, going to each page, and using the browser's built-in developer tools to inspect the page. The editor's structure is like an html page within an html page. The general structure looks something like this, though this omits many details and additional divs:

```
<!DOCTYPE html>
<html class="..." lang="en">
  <head>...</head>
  <body id="body" ... >
    <div id="view-container">
      <iframe id="editor-frame" ...>
        #document
        <!DOCTYPE html>
        <html class="..."> </html> <!-- this is the one you need to copy -->
      </iframe>
    </div>
  </body>
</html>
```

Using the inspector, highlight and copy the <html ...> element buried in the iframe. Paste the contents into a text file, and add it to the "raw_pages" folder. Do this for each page you want to extract the content for.


### Parsing the HTML
Once you have a text file of HTML for each page, you can compile and run the parser by running these commands or using your IDE:
```
javac -cp .:../jsoup.jar parser.java
java -cp .:../jsoup.jar parser
```
By default, this will add one text file per page into the "parsed_pages" folder with the parsed content from each page.

#### Adjusting the Parser
The main customizable element in this tool is the WRITE_TO_FILE flag which creates and writes parsing output to files if true and prints to the console otherwise. 

jsoup is a very rich library, and the [jsoup website](https://jsoup.org) offers examples and detailed usage explanations. This code makes use of the \*= selector which finds elements whose attributes contain the value that follows it. For example, in one of the blog pages on our site, the only place in the code that seemed to show a blog title was an <a class="blog-title-link blog-link">. To get just those <a> tags, we used `Elements titles = doc.select("a[class*=title]");`. In addition to \*= to find elements containing a phrase, ^= can be used to find elements with attributes beginning with something, and $= finds elements that end with something. See the jsoup documentation about syntax selectors for more information.

