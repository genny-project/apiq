package life.genny.qwanda.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

//
//import javax.json.bind.annotation.JsonbTransient;
//import javax.persistence.Embedded;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//import javax.validation.constraints.NotNull;
//
import org.jboss.logging.Logger;
//
//import io.quarkus.runtime.annotations.RegisterForReflection;
//import life.genny.qwanda.Value;
//
@Entity
@Table(name = "qentity_q")
@RegisterForReflection
public class EntityQuestion  extends PanacheEntity implements Serializable/*, Comparable<Object>*/ {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(EntityQuestion.class);
//
//	@Embedded
//	@NotNull
//	public Value value;
//
////	private Link link;
//
	public EntityQuestion() {
	}
//
////  public EntityQuestion(Link link) {
////    this.link = link;
////  }
//
//	@Override
//	public int compareTo(Object o) {
//		return 0;
//	}
//
//	@SuppressWarnings("unchecked")
//	@JsonbTransient
//	@Transient
//	public <T> T getValue() {
//		return value.getValue();
//
//	}
//
//	@JsonbTransient
//	@Transient
//	public <T> void setValue(final Object value) {
//		this.value.setValue(value);
//	}
//
//	@Transient
//	public Double getWeight() {
//		return value.weight;
//	}
//
//	@Transient
//	public void setWeight(Double weight) {
//		value.weight = weight;
//	}
}
