import java.util.List;

/** 
 * Represents a Gospel topic with a list of keywords.
 * @author Stephen Nielson smnche6
 */
class Topic {

  /** Name of the topic. */
  private String name;

  /** The keywords associated with the topic name. */
  private List<String> keywords;

  /**
   *  Creates a new topic with the name and keywords provided
   * @param topicName the name of the topic. Must not be null.
   * @param topicKeywords the keywords that correspond to this topic. Can't be null.
   **/
  public Topic(String topicName, List<String> topicKeywords) {
    if (topicName == null) {
      throw new IllegalArgumentException("topicName cannot be null");
    }
    if (topicKeywords == null) {
      throw new IllegalArgumentException("topicKeywords cannot be null");
    }

    this.name = topicName;

    this.keywords = topicKeywords;  
  }

  /** Checks if any of the topic keywords can be found in the passed in content.
    * This method stops as soon as it finds any valid keyword.
    * @param content the content to check for any of our keywords
    * @return True if any of our keywords were found, false otherwise
    */ 
  public boolean isInContent(String content) {
    for (String keyword : this.keywords) {
      if (content.contains(keyword)) {
        return true;
      } 
    }

    return false;
  }


  /** Retrieves the name of the topic. **/
  public String getName() {
    return name;
  }

  /** Retrieves the list of keywords associated with this topic. **/
  public List<String> getKeywords() {
    return keywords;
  }

  /** String version of the topic. Useful for debugging */
  public String toString() {
    return "name: " + name + ", keywords: " + keywords.toString();
  }
}
