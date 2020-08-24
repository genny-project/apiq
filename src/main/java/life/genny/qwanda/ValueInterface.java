package life.genny.qwanda;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ValueInterface {

	
	/**
	 * @return the valueDouble
	 */
	public Double getValueDouble() ;

	/**
	 * @param valueDouble the valueDouble to set
	 */
	public void setValueDouble(Double valueDouble) ;
	/**
	 * @return the valueInteger
	 */
	public Integer getValueInteger() ;

	/**
	 * @param valueInteger the valueInteger to set
	 */
	public void setValueInteger(Integer valueInteger) ;

	/**
	 * @return the valueLong
	 */
	public Long getValueLong() ;

	/**
	 * @param valueLong the valueLong to set
	 */
	public void setValueLong(Long valueLong) ;

	/**
	 * @return the valueDateTime
	 */
	public LocalDateTime getValueDateTime() ;

	/**
	 * @param valueDateTime the valueDateTime to set
	 */
	public void setValueDateTime(LocalDateTime valueDateTime) ;

	/**
	 * @return the valueDate
	 */
	public LocalDate getValueDate() ;

	/**
	 * @param valueDate the valueDate to set
	 */
	public void setValueDate(LocalDate valueDate) ;

	/**
	 * @return the valueString
	 */
	public String getValueString() ;

	/**
	 * @param valueString the valueString to set
	 */
	public void setValueString(String valueString) ;
	/**
	 * @return the valueBoolean
	 */
	public Boolean getValueBoolean() ;

	/**
	 * @param valueBoolean the valueBoolean to set
	 */
	public void setValueBoolean(Boolean valueBoolean) ;

	/**
	 * @return the expired
	 */
	public Boolean getExpired() ;

	/**
	 * @param expired the expired to set
	 */
	public void setExpired(Boolean expired) ;

	/**
	 * @return the refused
	 */
	public Boolean getRefused() ;

	/**
	 * @param refused the refused to set
	 */
	public void setRefused(Boolean refused) ;

	/**
	 * @return the weight
	 */
	public Double getWeight() ;

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Double weight) ;

}
