package dbexport
import groovy.sql.Sql



public class DbObjectExport {
	private static final String newLine = System.getProperty("line.separator").toString()

	private Sql sql

	def DbObjectExport(Sql sql){
		this.sql = sql
	}

	def getAllByType(ObjectType objType){
		def objects =  []
		sql.eachRow( 'select object_name from user_objects where object_type = ?', [objType.key]) { 
			objects << it.object_name
			
		}
		objects = objects.sort()
		println "Found ${objects.size()} ${objType.key} object(s)"
		objects
	}
	
	def export(String objectName, ObjectType objType, File baseDir) {
		def out = new File(baseDir, objectName.toLowerCase(Locale.ENGLISH) + objType.extension)
		
		if (objType == ObjectType.VIEW) {
			
			out << exportView(objectName, objType)
		} else {
			out << exportObject(objectName, objType)
			
			if (objType == ObjectType.PACKAGE) {
				def pckBody = exportObject(objectName, ObjectType.PACKAGE_BODY).toString()
				if (!pckBody.empty){
					out <<newLine+newLine
					out << pckBody
				}
				
			}
		}
		out << newLine
	}
	
	private exportView(String objectName, ObjectType objType) {
		def body = new StringBuilder()
		def dbOwner = Config.schemaRef
		
		def sqlSmt = "select text as kodSatir from user_views where view_name=UPPER('"+objectName+"')"
		sql.eachRow(sqlSmt){ line->
				body << 'create or replace force view '+Config.schemaGen +'.' + objectName + ' as' + newLine
				body << line.kodSatir
		}
		return body.toString().trim() + ";"+newLine
	}

	private exportObject(String objectName, ObjectType objType) {
		def body = new StringBuilder()
		def dbOwner = Config.schemaRef
		
		def sqlSmt = "select text as kodSatir from dba_source where type= '"+objType.key+"' and NAME=upper('"+objectName+"') and owner='"+dbOwner+"' order by line"
	
		//println "Running ${sqlSmt}"
		
		int index = 0;
		sql.eachRow(sqlSmt){ line->
			if (index == 0){
				def lowerType = objType.keyLowerCase()
				int initialLen = line.kodSatir.length()
				
				def firstLine =  line.kodSatir - lowerType; //remove type from the beginning
				if (firstLine.length() == initialLen) { //not changed
					firstLine =  line.kodSatir - objType.keyUpperCase(); //remove as upper case
				}
				
				firstLine =  (firstLine - objType) - " "; //remove type from the beginning in uppercase 
	
				body << 'create or replace '+lowerType+" "+Config.schemaGen +'.' + firstLine
			} else {
				body << line.kodSatir
			}
			index++
		}
		def result = body.toString().trim()
		if (result.size() > 0) {
			return result + newLine+"/"+newLine
		}
		return result
		
	}

} 

