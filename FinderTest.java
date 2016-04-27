import java.io.File;
import java.util.List;
import java.util.Map;

/** Runs unit tests on the Finder class. */
public class FinderTest {

  /** Main method to run the unit tests on the Finder class. **/
  public static void main(String[] args) {
    Finder finder = new Finder();
    Map<String, List<Topic>> results = finder.run("topics.txt", "files");

    // check the topics
    List<Topic> topics = finder.getTopics();
    Assert.assertTrue("topics should not be empty", topics != null);
    Assert.assertTrue("17 topics should be parsed", topics.size() == 17);
    Topic faith = topics.get(0);
    Assert.assertTrue("First topic should be faith", faith.getName().equals("Faith"));
    Assert.assertTrue("First topic should have 6 keywords", 
        faith.getKeywords().size() == 6);
    Assert.assertTrue("First keyword of first topic should be 'faith'", 
        faith.getKeywords().get(0).equals("faith"));
    Assert.assertTrue("Last keyword of first topic should be 'trust'", 
        faith.getKeywords().get(5).equals("trust"));

    Topic happiness = topics.get(topics.size() - 1);
    Assert.assertTrue("Last topic should be 'happiness'", 
        happiness.getName().equals("Happiness"));
    Assert.assertTrue("Last topic should have 4 keywords", 
        happiness.getKeywords().size() == 4);
    Assert.assertTrue("First keyword of last topic should be 'happy'", 
        happiness.getKeywords().get(0).equals("happy"));
    Assert.assertTrue("Last keyword of last topic should be 'rejoice'", 
        happiness.getKeywords().get(3).equals("rejoice"));

    // now let's verify a file was correct
    // get the full filename
    List<Topic> ch01FileTopics = results.get("ch01.txt");
    Assert.assertTrue("file should contain faith", ch01FileTopics.contains(faith));

    File ch05 = new File("files/ch05.txt");
    List<Topic> ch05FileTopics = results.get("ch05.txt");
    Assert.assertTrue("file should not contain faith", !ch05FileTopics.contains(faith));
  }
}
