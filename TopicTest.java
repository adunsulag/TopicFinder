import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Runs the unit tests on the Topic class. **/
public class TopicTest {
  /** Runs unit tests on Topic class. **/
  public static void main(String[] args) {
    Topic topic = new Topic("Jesus", Arrays.asList("Savior", "Christ", "Charity", "Bread of Life"));
    
    Assert.assertTrue("Content should have topic", topic.isInContent("Christ is the one we should turn to."));
    Assert.assertTrue("Content should have topic with multiple words", topic.isInContent("The Bread of Life is source of all happiness."));
    Assert.assertTrue("Content should have not have topic with partial words", !topic.isInContent("The Bread of Water is not the source of all happiness."));
    Assert.assertTrue("Satan should not be in topic", !topic.isInContent("Satan is not the one we should turn to"));
  }
}
