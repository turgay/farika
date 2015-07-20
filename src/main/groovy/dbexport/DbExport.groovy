package dbexport
import groovy.sql.Sql



public class DbExport {
	private ObjectFilter objectFilter;

	public static void  main(String[] args){
		
		DbExport dbExport = new DbExport()

		dbExport.prepareConfig(args)

		Sql sql = Config.getSql()

		if (Config.exportObjectTypes == null  || Config.exportObjectTypes.isEmpty()){
			println "ERROR:No exportObjectTypes defined in config file."
			return
		}

		Config.exportObjectTypes.each { typeStr ->
			println typeStr
			ObjectType objType = ObjectType.asType(typeStr.toUpperCase(Locale.ENGLISH))

			if (objType.requiresObjectExport){
				dbExport.exportUserObjects(objType, sql)
			} else{

				println "!!!   NOT SUPPORTED EXPORT TYPE: ${objType.key}"
			}
		}

		//exportTables(sql, "tables.sql");
		//exportUserDefinedTypes(sql, "types.sql")

		//new SequenceExport(sql).exportAll("seqs.sql")

		//	new DbObjectExport(sql).export("BAKIYE_LISTESI", "TYPE")

		println "DONE!"
		sql.close()
	}

	private  prepareConfig(String[] args) {
		if (args.length < 1) {
			println "Config file need to be provided!"
			println "Usage:"
			println "      dbexport export.cfg"

			return
		}
		def configFile = args[0]
		println "Using config file ${configFile}"

		Config.loadConfig(configFile);
		
		objectFilter = new ObjectFilter()

		println Config.CFG
	}

	private  exportUserObjects(ObjectType objType, Sql sql) {
		def exporter = new DbObjectExport(sql)

		def objects = exporter.getAllByType(objType)
		def baseDir = Config.workingDir
		baseDir = baseDir + objType.keyLowerCase()+"s\\"
		def baseDirFile = new File(baseDir)
		baseDirFile.mkdirs()

		objects.each { objName->
			if (! objectFilter.matches(objType, objName)) {
				println "Exporting ${objName} (${objType.keyLowerCase()})"
				exporter.export(objName, objType, baseDirFile)
			}
			else {
				println "Skipping ${objName} ( ${objType.keyLowerCase()})"
			}
		}
	}

	private  exportUserDefinedTypes(Sql sql, String outFileName) {

		new UserDefinedTypeExport(sql).exportAllTypes(outFileName)
	}

	private  exportTables(Sql sql, String outFileName) {

		//		def table= new TableExport(sql).export("ICOM_ISLEM_SONUC")
		//
		//		println table.asSql()
		//		println table.asSqlComments()

		def exporter = new TableExport(sql)

		def tables = exporter.allTableNames

		def skipList = [
			"WL_LLR_ADMINSERVER",
			"WL_LLR_ADM_NSERVER",
			"WL_LLR_MERKUR",
			"WL_LLR_URANUS",
			"WL_LLR_VENUS",
			"ADLIYE_MIGRATION2",
			"TMP_ACK",
			"TMP_ACK_LIST",
			"TMP_HOPE_AVEA_GTD",
			"TMP_HOPE_GTD_BASLIK",
			"TMP_KUL_OLSTR",
			"TMP_KUL_OLSTR2",
			"TMP_TMS_ADRES_DUZELT",
			"TMP_USERS",
			"TMP_VEKIL_OLSTR",
			"TMP_YETKI",
			"DR\$IDX_ACIK_ADRES\$I",
			"DR\$IDX_DOSYA_ANAHTAR_KELIME\$I",
			"TOAD_PLAN_TABLE",
			"SYS_EXPORT_TABLE_01",
			"MV_DOSYA_ADET",
			"MV_DOSYA_BORC_OZET",
			"MV_FINANSAL_HAREKET_MAX",
			"MV_ORG_BAZLI_TERKIN_EDILECEK",
			"PLSQL_PROFILER_DATA",
			"PLSQL_PROFILER_RUNS",
			"PLSQL_PROFILER_UNITS",
			"JAVA_NAMED_QUERY",
			"KULLANICI2",
			"INIT_SCRIPT",
			"INIT_SCRIPT_CLOB",
			"INIT_TABLE_LIST"
			//,"ID_ESLESTIRME"
		]


		def out = new File(outFileName).newWriter()

		tables.each { tname->
			if (!skipList.contains(tname)) {
				println tname
				def table = exporter.export(tname)
				out << "------------------ TABLE ${tname}--------------\n"
				out << table.asSql() +"\n"
				out << table.asSqlComments()
				out << "\n\n"
			}
		}
		out.close()
	}
}

