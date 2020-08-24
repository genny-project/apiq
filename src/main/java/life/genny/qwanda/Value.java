package life.genny.qwanda;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.javamoney.moneta.Money;

import com.fasterxml.jackson.annotation.JsonIgnore;

import life.genny.qwanda.converter.MoneyConverter;
import life.genny.qwanda.datatype.DataType;

@Embeddable
public class Value implements java.io.Serializable {


 /**
	 * 
	 */
	 static final long serialVersionUID = 1L;


 DataType dataType;

public Value(Object value) {
	dataType = DataType.getInstance(value.getClass().getName());
	setValue(value);
}
 
public Value(Object value, DataType dataType)
{
	this.dataType = dataType;
	setValue(dataType);
}
  
	/**
	 * Store the Double value of the value
	 */
	 Double valueDouble;

	/**
	 * Store the Integer value of the value
	 */
	 Integer valueInteger;

	/**
	 * Store the Long value of the value
	 */
	 Long valueLong;

	/**
	 * Store the LocalDateTime value of value
	 */
	 LocalDateTime valueDateTime;

	/**
	 * Store the LocalDate value of the value
	 */
	 LocalDate valueDate;

	/**
	 * Store the String value of the value
	 */
	@Type(type="text")
	 String valueString;

	/**
	 * Store the Boolean value of value
	 */
	 Boolean valueBoolean;

	@Column(name = "money", length = 128)
	@Convert(converter = MoneyConverter.class)
	Money valueMoney;

	/**
	 * Store the Expired boolean value if the value entry was expired
	 */
	 Boolean expired = false;

	/**
	 * Store the Refused boolean value if the value was refused
	 */
	 Boolean refused = false;

	/**
	 * Store the relative credibility of  the value
	 */
	 Double weight;

	/**
	 * @return the dataType
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the valueDouble
	 */
	public Double getValueDouble() {
		return valueDouble;
	}

	/**
	 * @param valueDouble the valueDouble to set
	 */
	public void setValueDouble(Double valueDouble) {
		this.valueDouble = valueDouble;
	}

	/**
	 * @return the valueInteger
	 */
	public Integer getValueInteger() {
		return valueInteger;
	}

	/**
	 * @param valueInteger the valueInteger to set
	 */
	public void setValueInteger(Integer valueInteger) {
		this.valueInteger = valueInteger;
	}

	/**
	 * @return the valueLong
	 */
	public Long getValueLong() {
		return valueLong;
	}

	/**
	 * @param valueLong the valueLong to set
	 */
	public void setValueLong(Long valueLong) {
		this.valueLong = valueLong;
	}

	/**
	 * @return the valueDateTime
	 */
	public LocalDateTime getValueDateTime() {
		return valueDateTime;
	}

	/**
	 * @param valueDateTime the valueDateTime to set
	 */
	public void setValueDateTime(LocalDateTime valueDateTime) {
		this.valueDateTime = valueDateTime;
	}

	/**
	 * @return the valueDate
	 */
	public LocalDate getValueDate() {
		return valueDate;
	}

	/**
	 * @param valueDate the valueDate to set
	 */
	public void setValueDate(LocalDate valueDate) {
		this.valueDate = valueDate;
	}

	/**
	 * @return the valueString
	 */
	public String getValueString() {
		return valueString;
	}

	/**
	 * @param valueString the valueString to set
	 */
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	/**
	 * @return the valueBoolean
	 */
	public Boolean getValueBoolean() {
		return valueBoolean;
	}

	/**
	 * @param valueBoolean the valueBoolean to set
	 */
	public void setValueBoolean(Boolean valueBoolean) {
		this.valueBoolean = valueBoolean;
	}

	
	
	/**
	 * @return the valueMoney
	 */
	public Money getValueMoney() {
		return valueMoney;
	}

	/**
	 * @param valueMoney the valueMoney to set
	 */
	public void setValueMoney(Money valueMoney) {
		this.valueMoney = valueMoney;
	}

	/**
	 * @return the expired
	 */
	public Boolean getExpired() {
		return expired;
	}

	/**
	 * @param expired the expired to set
	 */
	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	/**
	 * @return the refused
	 */
	public Boolean getRefused() {
		return refused;
	}

	/**
	 * @param refused the refused to set
	 */
	public void setRefused(Boolean refused) {
		this.refused = refused;
	}

	/**
	 * @return the weight
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	@Transient
	@XmlTransient
	public <T> T getValue() {

		switch (getDataType().getClassName()) {
		case "java.lang.Integer":
			return (T) getValueInteger();
		case "java.time.LocalDateTime":
			return (T) getValueDateTime();
		case "java.time.LocalDate":
			return (T) getValueDate();
		case "java.lang.Long":
			return (T) getValueLong();
		case "java.lang.Double":
			return (T) getValueDouble();
		case "java.lang.Boolean":
			return (T) getValueBoolean();
		case "org.javamoney.moneta.Money":
			 return (T) getValueMoney();
		case "java.lang.String":
		default:
			return (T) getValueString();
		}

	}

	@JsonIgnore
	@Transient
	@XmlTransient
	public <T> void setValue(final Object value) {
		switch (getDataType().getClassName()) {
		case "java.lang.Integer":
			setValueInteger((Integer) value);
			break;
		case "java.time.LocalDateTime":
			setValueDateTime((LocalDateTime) value);
			break;
		case "java.time.LocalDate":
			setValueDate((LocalDate) value);
			break;
		case "java.lang.Long":
			setValueLong((Long) value);
			break;
		case "org.javamoney.moneta.Money":
			setValueMoney((Money) value);
			break;
		case "java.lang.Double":
			setValueDouble((Double) value);
			break;
		case "java.lang.Boolean":
			setValueBoolean((Boolean) value);
			break;

		case "java.lang.String":
		default:
			setValueString((String) value);
			break;
		}

	}	
	

}
