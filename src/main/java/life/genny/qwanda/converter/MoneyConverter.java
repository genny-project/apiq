package life.genny.qwanda.converter;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;
import io.vertx.core.json.JsonObject;

@Converter
public class MoneyConverter implements AttributeConverter<Money, String> {

	 static final Logger log = org.apache.logging.log4j.LogManager
			.getLogger(MethodHandles.lookup().lookupClass().getCanonicalName());

	@Override
	public String convertToDatabaseColumn(final Money money) {
		String ret = "";
		
		ret = JsonObject.mapFrom(money).toString();


		return ret;

	}

	@Override
	public Money convertToEntityAttribute(String moneyStr) {
//	    Optional<String> moneyStrOfNullable = Optional.ofNullable(moneyStr);

//	    System.out.println(moneyStrOfNullable.isEmpty());
//	    Money money = moneyStrOfNullable
//	        .filter(Objects::nonNull)
//	        .filter(d -> !d.equals("null"))
//	        .map(JsonObject::mapFrom)
//	        .map(d -> d.mapTo(Money.class))
//	        .orElse(Money.of(0.0, "AUD"));

		return Money.of(0.0, "AUD");
	}


}
