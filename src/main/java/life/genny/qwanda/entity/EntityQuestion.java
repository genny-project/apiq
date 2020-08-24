package life.genny.qwanda.entity;

import java.lang.invoke.MethodHandles;
import org.apache.logging.log4j.Logger;
import life.genny.qwanda.Link;

public class EntityQuestion implements java.io.Serializable, Comparable<Object> {

	 static final long serialVersionUID = 1L;

	protected static final Logger log = org.apache.logging.log4j.LogManager
			.getLogger(MethodHandles.lookup().lookupClass().getCanonicalName());

   String valueString;

   Double weight;

	 Link link;

  public EntityQuestion() {}

  public EntityQuestion(Link link) {
    this.link = link;
  }

	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
