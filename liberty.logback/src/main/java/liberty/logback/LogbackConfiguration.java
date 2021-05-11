package liberty.logback;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import ch.qos.logback.classic.Level;

//@Component(configurationPolicy = ConfigurationPolicy.REQUIRE, configurationPid = {"logback"})
public class LogbackConfiguration {

	private static final String CONFIG_FILE = "file";
	private static final String CONFIG_ROOT_LEVEL = "level";
	private static final String CONFIG_LOGGER = "logger";
	private static final String CONFIG_LOGGER_NAME = "name";
	private static final String CONFIG_LOGGER_LEVEL = "level";
	
	private AtomicReference<String> fileRef = new AtomicReference<>();
	private AtomicReference<Optional<Level>> rootLevelRef = new AtomicReference<>();
	private AtomicReference<Collection<LoggerConfig>> configuredLoggersRef = new AtomicReference<>();
	
	@Reference 
	volatile ConfigurationAdmin configAdmin;
	
	@Activate
	protected void start() {
		parseConfiguration();
	}
	
	@Modified
	public void updated() {
		parseConfiguration();
	}
	
	String getConfiguredFile() {
		return fileRef.get();
	}
	
	Optional<Level> getConfiguredRootLevel(){
		return rootLevelRef.get();
	}
	
	Collection<LoggerConfig> getConfiguredLoggers(){
		return Collections.unmodifiableCollection(configuredLoggersRef.get());
	}
	
	private void parseConfiguration() {
		Dictionary<String,?> properties;
		try {
			properties = configAdmin.getConfiguration("logback").getProperties();
		}catch(IOException e) {
			e.printStackTrace();
			properties = new Hashtable<>();
		}
		
		
		String file = (String)properties.get(CONFIG_FILE);
		String rootLevel = (String)properties.get(CONFIG_ROOT_LEVEL);
		
		String[] loggerPids = (String[]) properties.get(CONFIG_LOGGER);
		
		List<LoggerConfig> loggers = Arrays.stream(loggerPids)
				.map(this::parseLogger)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		
		fileRef.set(file);
		rootLevelRef.set(Optional.ofNullable(Level.toLevel(rootLevel)));
		configuredLoggersRef.set(loggers);
		
	}
	
	LoggerConfig parseLogger(String pid) {
		try {
			Configuration configuration = configAdmin.getConfiguration(pid, null);
			return new LoggerConfig(
					(String)configuration.getProperties().get(CONFIG_LOGGER_NAME),
					(String)configuration.getProperties().get(CONFIG_LOGGER_LEVEL));
		}catch(IOException e) {
			// TODO log, maybe with LogService?
			e.printStackTrace(); // FIXME
			return null;
		}
	}
	

	static final class LoggerConfig {
		private String name;
		private Level level;
		
		private LoggerConfig(String name, String level) {
			Objects.requireNonNull(name);
			Objects.requireNonNull(level);
			this.name = name;
			this.level = Level.toLevel(level);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name, level.levelInt);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LoggerConfig other = (LoggerConfig) obj;
			return Objects.equals(level, other.level) && Objects.equals(name, other.name);
		}
	}
}
