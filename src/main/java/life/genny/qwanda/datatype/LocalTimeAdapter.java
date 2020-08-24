package life.genny.qwanda.datatype;

import java.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
	@Override
	public LocalTime unmarshal(String s) throws Exception {
		return LocalTime.parse(s);
	}

	@Override
	public String marshal(LocalTime time) throws Exception {
		return time.toString();
	}
}
