package RestExample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Map;

/**
 * 
 * @author anteneh
 * logs the incoming json request
 */
public class RequestEndPoint implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println(exchange.getIn().getBody(Map.class).toString());
	}

}
