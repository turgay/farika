package dbexport.config;

public class ConfigurationLoader {

	ConfigObject loadConf(String cfgFile) {
		def cfgUrl  = getConfigLocation(cfgFile)
		ConfigSlurper slurper = new ConfigSlurper()
		return slurper.parse(cfgUrl)
	}

	def getConfigLocation(cfgFileName) {
		GroovyClassLoader classLoader = new GroovyClassLoader()

		File cfgFile  = new File(cfgFileName)
		if (!cfgFile.exists()){
			throw new IllegalArgumentException(cfgFileName + " cannot be found.")
		}
		def configLocation =  new URL("file:${cfgFileName}")

		if (configLocation == null) {
			throw new IllegalArgumentException("configLocation cannot be null")
		}
		configLocation
	}
}