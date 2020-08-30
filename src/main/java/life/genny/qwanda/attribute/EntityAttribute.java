package life.genny.qwanda.attribute;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.jboss.logging.Logger;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import life.genny.notes.utils.LocalDateTimeAdapter;
import life.genny.qwanda.Value;

//
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity

@Table(name = "qbaseentity_attribute" )
//indexes = {
//	//	@Index(columnList = "baseEntityCode", name = "ba_idx"),
//		@Index(columnList = "attributeCode", name = "ba_idx"),
//		@Index(columnList = "valueString", name = "ba_idx"),
//        @Index(columnList = "valueBoolean", name = "ba_idx")
//    },
//uniqueConstraints = @UniqueConstraint(columnNames = {"attributeCode","baseEntityCode","realm"})
//)

@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@RegisterForReflection
public class EntityAttribute extends PanacheEntity {
//
	private static final Logger log = Logger.getLogger(EntityAttribute.class);
	
	private static final String REGEX_REALM = "[a-zA-Z0-9]+";
	private static final String DEFAULT_REALM = "genny";

	@NotEmpty
	@JsonbTransient
	@Pattern(regexp = REGEX_REALM, message = "Must be valid Realm Format!")
	public String realm=DEFAULT_REALM;
	
	@JsonbTypeAdapter(LocalDateTimeAdapter.class)
	public LocalDateTime created = LocalDateTime.now(ZoneId.of("UTC"));

	@JsonbTypeAdapter(LocalDateTimeAdapter.class)
	public LocalDateTime updated;
//
//	@JsonbTypeAdapter(AttributeAdapter.class)
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	public Attribute attribute;
	
	@Embedded
	@NotNull
	public Value value;
	

	public Boolean readonly = false;
	

	@Transient
	public Integer index=0;  // used to assist with ordering 


	/**
	 * Store the relative importance of the attribute for the baseEntity
	 */
	public Boolean inferred = false;

	/**
	 * Store the privacy of this attribute , i.e. Don't display
	 */
	public Boolean privacyFlag = false;


	public EntityAttribute() {
	}

	@PreUpdate
	public void autocreateUpdate() {
		updated = LocalDateTime.now(ZoneId.of("UTC"));
	}

	@PrePersist
	public void autocreateCreated() {
		if (created == null)
			created = LocalDateTime.now(ZoneId.of("UTC"));
	}

	@Transient
	@JsonbTransient
	public Date getCreatedDate() {
		final Date out = Date.from(created.atZone(ZoneId.systemDefault()).toInstant());
		return out;
	}

	@Transient
	@JsonbTransient
	public Date getUpdatedDate() {
		if (updated==null) return null;
		final Date out = Date.from(updated.atZone(ZoneId.systemDefault()).toInstant());
		return out;
	}

	@SuppressWarnings("unchecked")
	@JsonbTransient
	@Transient
	public String getAttributeCode()
	{
		return attribute.code;
	}
	
	

	@SuppressWarnings("unchecked")
	@JsonbTransient
	@Transient
	public <T> T getValue() {
		return value.getValue();
	      
	}

	@JsonbTransient
	@Transient
	public <T> void setValue(final Object value) {
		if (this.readonly) {
			log.error("Trying to set the value of a readonly EntityAttribute! "+getAttributeCode());
			return; 
		}

		setValue(value,true);
	}
	@JsonbTransient
	@Transient
	public <T> void setValue(final Object value, final Boolean lock) {
		if (this.readonly) {
			log.error("Trying to set the value of a readonly EntityAttribute! "+getAttributeCode());
			return; 
		}

		this.value.setValue(value);
		// if the lock is set then 'Lock it in Eddie!'. 
		if (lock)
		{
			this.readonly = true;
		}

	}
	


	@JsonbTransient
	@Transient
	public <T> void setLoopValue(final Object value) {
		setValue(value,false);
	}
	

	@JsonbTransient
	@Transient
	public String getAsString() {

	return value.toString();
	}

	@JsonbTransient
	@Transient
	public String getAsLoopString() {
	return value.toString();
	}
	
	@SuppressWarnings("unchecked")
	@JsonbTransient
	@Transient
	public  <T> T getLoopValue() {
	return getValue();
		
	}

	public int compareTo(EntityAttribute obj) {
		if (this == obj)
			return 0;

		//return value.compareTo(obj.value);
		return 0;
	}


	@Override
	public String toString() {
		return "attributeCode=" + getAttributeCode() + ", value="
				+ value + ", weight=" + /*weight +*/ ", inferred=" + inferred + "] be="/*+this.getBaseEntityCode()*/;
	}

	@SuppressWarnings("unchecked")
	@JsonbTransient
	@Transient
	public <T> T getObject() {

		return getValue();
	}

	@JsonbTransient
	@Transient
	public String getObjectAsString() {

			return value.toString();

	}

	

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}


	@Transient
	public Double getWeight()
	{
		return value.weight;
	}
	
	@Transient
	public void setWeight(Double weight)
	{
		value.weight = weight;
	}
}
