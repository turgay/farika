package dbexport

import groovy.sql.Sql
import dbexport.config.ConfigurationLoader


public class Config {

	private static ConfigObject CFG

	def config

	private Config(cfg){
		this.config = cfg
	}

	static Config loadConfig(cfgFile) {
		CFG = new ConfigurationLoader().loadConf(cfgFile)
	}


	static getSchemaRef(){
		return dbConf().schemaRef
	}

	static getSchemaGen(){
		return dbConf().schemaGen
	}

	static getTablespaceGen(){
		return dbConf().tablespaceGen
	}
	
	static getWorkingDir(){
		return CFG.workingDir
	}
	
	static getReferenceDir(){
		return CFG.referenceDir
	}
	
	static getExportObjectTypes(){
		CFG.exportObjectTypes
	}
	
	static getExcludes(){
		CFG.excludes
	}

	static Sql getSql() {
		return Sql.newInstance(
				dbConf().connectionString,
				dbConf().dbUser,
				dbConf().dbPassword,"oracle.jdbc.driver.OracleDriver");
	}

	private static dbConf(){
		return CFG.database
	}
}

