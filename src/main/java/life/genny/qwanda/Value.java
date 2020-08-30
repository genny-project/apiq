package life.genny.qwanda;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.annotations.Type;
import org.jboss.logging.Logger;

import io.quarkus.runtime.annotations.RegisterForReflection;
import life.genny.qwanda.datatype.DataType;

@Embeddable
@RegisterForReflection
public class Value implements Serializable/*,Comparable<Value> */{

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(Value.class);

	public DataType dataType;

	public Double valueDouble;
	public Integer valueInteger;
	public Long valueLong;
	public LocalDateTime valueDateTime;
	public LocalDate valueDate;
	public LocalTime valueTime;
	public Boolean valueBoolean;

	@Type(type = "text")
	public String valueString;

	public Boolean expired = false;

	/**
	 * Store the Refused boolean value if the value was refused
	 */
	public Boolean refused = false;

	/**
	 * Store the relative credibility of the value
	 */
	public Double weight=0.0;

	public Value() {
		this.dataType = new DataType(String.class.getCanonicalName()); // default
	}

	public Value(Object value) {
		dataType = DataType.getInstance(value.getClass().getName());
		setValue(value);
	}

	public Value(Object value, DataType dataType) {
		this.dataType = dataType;
		setValue(dataType);
	}

	@SuppressWarnings("unchecked")
	@JsonbTransient
	@Transient
	public <T> T getValue() {

		switch (dataType.getClassName()) {
		case "java.lang.String":
		case "String":
			return (T) valueString;
		case "java.lang.Integer":
		case "Integer":
			return (T) valueInteger;
		case "java.time.LocalDateTime":
		case "LocalDateTime":
			return (T) valueDateTime;
		case "java.time.LocalDate":
		case "LocalDate":
			return (T) valueDate;
		case "java.time.LocalTime":
		case "LocalTime":
			return (T) valueTime;
		case "java.lang.Long":
		case "Long":
			return (T) valueLong;
		case "java.lang.Double":
		case "Double":
			return (T) valueDouble;
		case "java.lang.Boolean":
		case "Boolean":
			return (T) valueBoolean;

		default:
			return (T) valueString;
		}

	}

	
	
	
	@JsonbTransient
	@Transient
	public <T> void setValue(final Object value) {
		
		if (value == null) {
			return;
		}
		
		if (value instanceof String) {
			String result = (String) value;
			try {
				if (dataType.getClassName().equalsIgnoreCase(String.class.getCanonicalName())) {
					valueString =result;
				} else {
					setValue(convertFromString(result));
				}
			} catch (Exception e) {
				log.error("Conversion Error :" + value + " for dataType "+dataType + " and SourceCode:"						);
			}
		} else {
		
		switch (dataType.getClassName()) {
		case "java.lang.String":
		case "String":
			valueString = (String) value;
			break;
		case "java.lang.Integer":
		case "Integer":
			valueInteger = (Integer) value;
			break;
		case "java.time.LocalDateTime":
		case "LocalDateTime":
			valueDateTime = (LocalDateTime) value;
			break;
		case "java.time.LocalDate":
		case "LocalDate":
			valueDate = (LocalDate) value;
			break;
		case "java.lang.Long":
		case "Long":
			valueLong = (Long) value;
			break;
		case "java.lang.Double":
		case "Double":
			valueDouble = (Double) value;
			break;
		case "java.lang.Boolean":
		case "Boolean":
			valueBoolean = (Boolean) value;
			break;

		
		default:
			valueString = (String) value;
			break;
		}
		}

	}

	@JsonbTransient
	@Transient
	public <T> T convertFromString(final String value) throws ParseException
	{
		switch (dataType.getClassName()) {
		case "java.lang.String":
			valueString = value;
			return (T)valueString;
		case "java.lang.Integer":
		case "Integer":
			final Integer integer = Integer.parseInt(value);
			valueInteger = integer;
			return (T)valueInteger;
		case "java.time.LocalDateTime":
		case "LocalDateTime":
			List<String> formatStrings = Arrays.asList("yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss",
					"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			for (String formatString : formatStrings) {
				try {
					Date olddate = new SimpleDateFormat(formatString).parse(value);
					final LocalDateTime dateTime = olddate.toInstant().atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					valueDateTime = dateTime;
					return (T)valueDateTime;
					
				} catch (ParseException e) {
				}

			}
			break;
		case "java.time.LocalDate":
		case "LocalDate":
			Date olddate = null;
			try {
				olddate = DateUtils.parseDate(value, "M/y", "yyyy-MM-dd", "yyyy/MM/dd",
						"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			} catch (java.text.ParseException e) {
				olddate = DateUtils.parseDate(value, "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss",
						"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			}
			final LocalDate date = olddate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			valueDate = date;
			return (T) valueDate;
		case "java.time.LocalTime":
		case "LocalTime":
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			final LocalTime time= LocalTime.parse(value, formatter);
			valueTime = time;
			return (T)valueTime;
		case "java.lang.Long":
		case "Long":
			final Long l = Long.parseLong(value);
			valueLong = l;
			return (T)valueLong;
		case "java.lang.Double":
		case "Double":
			final Double d = Double.parseDouble(value);
			valueDouble = d;
			return (T)valueDouble;
		case "java.lang.Boolean":
		case "Boolean":
			final Boolean b = Boolean.parseBoolean(value);
			valueBoolean = b;
			return (T)valueBoolean;

		
		default:
			valueString = (String) value;
			break;
		}
		return (T)valueString;
	}
	
	@JsonbTransient
	@Transient
	@Override
	public String toString() {
		String ret = "";
		if( valueString != null) {
			return valueString;
		}
		if(valueBoolean != null) {
			return valueBoolean ? "TRUE" : "FALSE";
		}

		if(valueInteger != null) {
			return valueInteger.toString();
		}
		if(valueDateTime != null) {
			String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
			String localDateTimeStr = valueDateTime.format(dateFormatter);
			return localDateTimeStr;
		}
		if(valueDate != null) {
			DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
			Date date = Date.from(valueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			String dout2 = df2.format(date);
			return dout2;
		}
		if(valueTime != null) {
			DateFormat df2 = new SimpleDateFormat("HH:mm");			
			String dout2 = df2.format(valueTime);
			return dout2;
		}
		if(valueLong != null) {
		    return valueLong.toString();
		}
		if(valueDouble != null) {
		    return valueDouble.toString();
		}
		
		return ret;
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataType, expired, refused, valueBoolean, valueDate, valueDateTime, valueDouble,
				valueInteger, valueLong, valueString, valueTime, weight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Value))
			return false;
		Value other = (Value) obj;
		return Objects.equals(dataType, other.dataType) && Objects.equals(expired, other.expired)
				&& Objects.equals(refused, other.refused) && Objects.equals(valueBoolean, other.valueBoolean)
				&& Objects.equals(valueDate, other.valueDate) && Objects.equals(valueDateTime, other.valueDateTime)
				&& Objects.equals(valueDouble, other.valueDouble) && Objects.equals(valueInteger, other.valueInteger)
				&& Objects.equals(valueLong, other.valueLong) && Objects.equals(valueString, other.valueString)
				&& Objects.equals(valueTime, other.valueTime) && Objects.equals(weight, other.weight);
	}

//	@Override
//	public int compareTo(Value obj) {
//		if (this == obj)
//			return 0;
//
//		switch (dataType.getClassName()) {
//		case "java.lang.Integer":
//		case "Integer":
//			return Integer.compare(valueInteger, obj.valueInteger); 
//		case "java.time.LocalDateTime":
//		case "LocalDateTime":
//			return new CompareToBuilder().append(this.valueDateTime, obj.valueDateTime).toComparison();
//		case "java.time.LocalTime":
//		case "LocalTime":
//			return new CompareToBuilder().append(this.valueTime, obj.valueTime).toComparison();
//		case "java.lang.Long":
//		case "Long":
//			return new CompareToBuilder().append(this.valueLong,obj.valueLong).toComparison();
//		case "java.lang.Double":
//		case "Double":
//			return new CompareToBuilder().append(this.valueDouble, obj.valueDouble).toComparison();
//		case "java.lang.Boolean":
//		case "Boolean":
//			return new CompareToBuilder().append(this.valueBoolean, obj.valueBoolean).toComparison();
//		case "java.time.LocalDate":
//		case "LocalDate":
//			return new CompareToBuilder().append(this.valueDate, obj.valueDate).toComparison();
//		case "org.javamoney.moneta.Money":
//		case "java.lang.String":
//		default:
//			return new CompareToBuilder().append(this.valueString, obj.valueString).toComparison();
//
//		}
//
//	}
//
//	public class compare implements Comparator<Value> {
//		 
//	    @Override
//	    public int compare(Value first, Value second) {
//	       return first.compareTo(second);
//	    }
//	 
//	}

}
