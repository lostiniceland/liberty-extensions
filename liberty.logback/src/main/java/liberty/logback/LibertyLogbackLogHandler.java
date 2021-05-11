package liberty.logback;

import java.util.logging.LogRecord;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;

import com.ibm.wsspi.logging.LogHandler;

/**
 * This Service implements a Liberty SPI interface.
 * Liberty will forward JUL LogRecords to all registered LogHandlers.
 */
@Component
public class LibertyLogbackLogHandler implements LogHandler {
	
	@Reference
	private LogbackPublisher publisher;

	@Override
	public void publish(String msg, LogRecord record) {
		// TODO Auto-generated method stub
		this.publisher.log(msg, record);
	}
	
}
