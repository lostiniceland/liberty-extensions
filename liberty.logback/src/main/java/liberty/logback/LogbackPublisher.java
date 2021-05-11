package liberty.logback;

import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.LogRecord;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;

@Component()
public class LogbackPublisher{
	
	PatternLayoutEncoder encoder;
	ConsoleAppender<ILoggingEvent> consoleAppender;
	RollingFileAppender<ILoggingEvent> fileAppender;
	Logger logger;
	java.util.logging.Logger featureLogger = java.util.logging.Logger.getLogger(LogbackPublisher.class.getName());
	
//	@Reference
//	private LogbackConfiguration config;
	
	@Activate
	protected void activate() {
		featureLogger.fine("Initializing...");
		LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
		encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setCharset(Charset.forName("utf-8"));
		encoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
		encoder.start();
		
		consoleAppender = new ConsoleAppender<>();
		consoleAppender.setName("console");
		consoleAppender.setContext(loggerContext);
		consoleAppender.setEncoder(encoder);
		consoleAppender.start();
		
		fileAppender = new RollingFileAppender<>();
		fileAppender.setName("file");
		fileAppender.setContext(loggerContext);
		fileAppender.setEncoder(encoder);
//		fileAppender.setFile(config.getConfiguredFile());
		fileAppender.setFile("/home/marc/test.log");
		
		
		logger = loggerContext.getLogger("ROOT");
		logger.setAdditive(false);
//		logger.setLevel(config.getConfiguredRootLevel().orElse(Level.WARN));
		logger.setLevel(Level.INFO);
		logger.addAppender(consoleAppender);
		
		featureLogger.fine("DONE");
	}
	
	
	
	@Deactivate
	protected void stop() {
		consoleAppender.stop();
		if(fileAppender.isStarted()) {
			fileAppender.stop();
		}
		encoder.stop();
	}

	public void log(String msg, LogRecord record) {
		featureLogger.fine("Received LogRecord for message: " + msg + " :" + record.toString());
		if(record.getLevel() == java.util.logging.Level.SEVERE) {
			logger.error(record.getMessage());
		}else if(record.getLevel() == java.util.logging.Level.WARNING) {
			logger.warn(record.getMessage());
		}else if(record.getLevel() == java.util.logging.Level.FINE) {
			logger.info(record.getMessage());
		}else if(record.getLevel() == java.util.logging.Level.FINER) {
			logger.debug(record.getMessage());
		}else if(record.getLevel() == java.util.logging.Level.FINEST) {
			logger.trace(record.getMessage());
		}
	}
	
}
