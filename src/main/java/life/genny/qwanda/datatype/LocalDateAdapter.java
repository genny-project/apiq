package life.genny.qwanda.datatype;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.logging.log4j.Logger;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
	
	protected static final Logger log = org.apache.logging.log4j.LogManager
			.getLogger(MethodHandles.lookup().lookupClass().getCanonicalName());

	@Override
	public LocalDate unmarshal(String s) throws Exception {

		LocalDate ret =null;
		
		try {
			ret  = LocalDate.parse(s);
		} catch (Exception e) {
			log.error("Bad date: ["+s+"]");
		}
		
		return ret;
	}

	@Override
	public String marshal(LocalDate date) throws Exception {
		return date.toString();
	}
}
