import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Runs the unit tests on the Topic class. **/
public class TopicTest {
  /** Runs unit tests on Topic class. **/
  public static void main(String[] args) {
    Topic topic = new Topic("Jesus", Arrays.asList("Savior", "Christ", "Charity"));
    
    Assert.assertTrue("Christ should be in topic", topic.hasKeyword("Christ"));
    Assert.assertTrue("Satan should not be in topic", !topic.hasKeyword("Satan"));
    Set<String> set1 = new HashSet<String>(Arrays.asList( "Savior", "Blasphemer", "Pharisee" ));
    Set<String> set2 = new HashSet<String>(Arrays.asList("Judas", "Blasphemer", "Pharisee" ));
    Assert.assertTrue("Topic has at least one of the keywords", topic.containsAnyKeyword(set1));
    Assert.assertTrue("Topic should not have any of the keywords", !topic.containsAnyKeyword(set2));
  }
}
