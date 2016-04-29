import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Given a topic file and a directory of gospel files it lists out each file and the gospel topics
 * found in each file.
 * @author Stephen Nielson / smnche6
 * @Collaborator: Benjamin Wright
 */
class Finder {
  /** The topics to find in the gospel files. **/
  private List<Topic> topics;

  
  /** 
    * Given a filename, it loads the properties file from that filename.
    * @return The properties that have been loaded from the properties file.  
    *          Null if we couldn't open the stream.
    * @throws Exception if the file can not be read or could not be found.
    */
  private Properties loadPropertiesFromFile(String filename) throws Exception {
    try {
      Properties properties = new Properties();
      FileReader fr = new FileReader(filename);
      properties.load(fr);
      return properties;
    } catch (IOException ex) {
      throw new Exception("Could not load properties file from filename " + filename, ex); 
    }
  }

  /**
   * Lists each gospel file in a directory with their associated topics referenced in the file.
   */
  public static void main(String[] args) {
    String instructions = "Error: Incorrect program usage please call the program "
      + "using Finder <properties file>. Or with the jar file java -jar GospelTopicsFinder.jar <properties file>";
    if (args.length < 1) {
      System.out.println(instructions);
          
      return;
    }
    if (args[0].trim().isEmpty()) {
      System.out.println(instructions);
      return;
    }

    // <topicFile> <gospelFilesDirectory>

    //String topicFile = args[0];

    try { 
      Finder finder = new Finder();
      Map<String, List<Topic>> results = finder.run(args[0]);

      for (String filename : results.keySet()) {
        System.out.print(filename + " ");
        List<Topic> topics = results.get(filename);
        // example string joining from: http://stackoverflow.com/a/669165
        String delim = "";
        for (Topic topic : topics) {
          System.out.print(delim + topic.getName());
          delim = ",";
        }
        System.out.println();
      }
    } catch (Exception ex) {
      System.err.println("Error occurred and could not run finder.  Detailed error below:");
      ex.printStackTrace();
    }
  }

  /** 
   * Return a map of files in the gospel files directory to the topics they 
   * contained as listed in the topicFile. 
   * @throws Exception if the Finder properties could not be loaded 
   *         or the topics file could not be found 
   */
  public Map<String, List<Topic>> run(String propertiesFilename) throws Exception {

    Properties properties = loadPropertiesFromFile(propertiesFilename);

    String topicFile = properties.getProperty("topicFile");
    String gospelFilesDirectory = properties.getProperty("gospelFilesDirectory");

    // given a topic file create a collection of topics contained in that topic file
    // store that collection internally
    this.topics = getTopicsFromFile(topicFile);
    if (this.topics == null) {
      throw new Exception("Error: Could not retrieve topics from file.  Cannot continue.");
    }

    // now get a list of all the file names in the directory
    List<String> gospelFilenames = getGospelFilenamesFromDirectory(gospelFilesDirectory);

    return generateTopicsByFile(gospelFilenames);
  }

  /**
   * Retrieves the sorted filenames of all the gospel files listed 
   * for the passed in directoryPath.
   * @param directoryPath the directory to search for the gospel files
   * @return The sorted list of gospel files found in the directory.
   */
  List<String> getGospelFilenamesFromDirectory(String directoryPath) {
    List<String> filenames = new ArrayList<String>();
    File dir = new File(directoryPath);
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        filenames.add(file.getAbsolutePath());
      }
    }
    // I sort the file list as the files come out nicer that way
    // otherwiser things look pretty wierd on the output with the files
    // being in whatever order the FS returned them as.
    Collections.sort(filenames);
    return filenames;
  }

  /**
   * Given a list of gospel file names generate and return a mapping of the file 
   * to the topics that the file contains.
   * @param gospelFiles the files that we will generate a topic list mappings for.
   * @return Map where the key is the file and the value is the topics found for that file. 
   */
  private Map<String, List<Topic>> generateTopicsByFile(List<String> gospelFiles) {

    // linked hash map maintains the insertion order of the file list
    Map<String, List<Topic>> topicsByFile = new LinkedHashMap<String, List<Topic>>();
    for (String filePath: gospelFiles) {
      List<Topic> fileTopics = new ArrayList<Topic>();
      try {
        String fileContent = getFileContent(filePath);
        for (Topic topic : this.topics) {
          if (topic.isInContent(fileContent)) {
            fileTopics.add(topic); 
          }
        }
        topicsByFile.put(getFileNameFromPath(filePath), fileTopics);
      } catch (IOException ex) {
        System.err.println("Could not parse file " + filePath);
        ex.printStackTrace();
      }
    }
    return topicsByFile;
  }

  /** Returns the name of just the file.
    * @param filePath the path to the file
    * @return just the name and extension part of the file
    **/
  private String getFileNameFromPath(String filePath) {
    return new File(filePath).getName();
  }

  /** 
   * Returns the lowercased string content of the file 
   * @param filename The name of the file we will parse for keywords.
  **/
  private String getFileContent(String filename) throws IOException {
    // Simple shortcut to get all of a file into a string http://stackoverflow.com/a/326440
    // assumption is the file is fairly small... otherwise reading a chunk at a time would be
    // more appropriate.
    byte[] encoded = Files.readAllBytes(Paths.get(filename));
    return new String(encoded, StandardCharsets.UTF_8).toLowerCase();
  }

  /** Creates a list of topics from the data contained in the topicFile passed in. **/
  private List<Topic> getTopicsFromFile(String topicFile) {
    List<Topic> topics = new ArrayList<Topic>();

    try {
      List<String> lines = Files.readAllLines(Paths.get(topicFile), StandardCharsets.UTF_8);
      for (String string : lines) {
        Topic topic = parseTopicFromLine(string);
        topics.add(topic);
      }
    } catch (IOException | IllegalArgumentException ex) {
      System.out.println("Failed to parse topic file " + topicFile);
      ex.printStackTrace();
      return null;
    }
    return topics;
  }

  /** Given a topic line from a topic file, create a topic and return it.
   * @param line The topic line to parse the name and keywords from.
   * @return The Topic object containing the name and keywords of the topic.
   */
  private Topic parseTopicFromLine(String line) {
    String[] topicParts = line.split(":"); 
    if (topicParts.length < 2) {
      throw new IllegalArgumentException("Topic line was not formatted properly.  " 
        + "Expected <name>:<keywords>.  Received: " + line);
    }
    String topicName = topicParts[0];
    String topicKeywords = topicParts[1];

    String[] keywords = topicKeywords.split(",");
    if (topicParts.length <= 0) {
      throw new IllegalArgumentException("Topic line was not formatted properly. "
        + "Expected keywords for topic but none found");
    }
    List<String> lcKeywords = new ArrayList<String>();
    for (String keyword : keywords) {
      lcKeywords.add(keyword.toLowerCase());
    }

    return new Topic(topicName, lcKeywords);
  }

  /** Return the topics found during the finder. */
  public List<Topic> getTopics() {
    return this.topics;
  }
}
