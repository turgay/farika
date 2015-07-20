package dbexport
import groovy.sql.Sql



//TODO incomplete

public class UserDefinedTypeExport {

	private Sql sql

	def UserDefinedTypeExport(Sql sql){
		this.sql = sql
	}
//SELECT dbms_metadata.get_ddl('TYPE', 'DOKUMAN_HIT_TYPE') FROM DUAL;

	def getAllUserDefinedTypes(){
		def udTypes =  []
		sql.eachRow( 'select type_name from user_types' ) { 
			udTypes << it.type_name
			
		}
		udTypes = udTypes.sort()
		println udTypes.size()
		udTypes
	}
	
	def export(String typeName) {
		
		
		
		def meta = sql.connection.metaData
		def attrs = meta.getUDTs(Config.schemaRef,Config.schemaRef,typeName.toUpperCase(Locale.ENGLISH),null)


		UserDefinedType udt = new UserDefinedType(typeName)
		while (attrs.next()) {
			
		
			
			// tcol =  new Column(
			println		attrs.getString("TYPE_NAME")+ " C=" +attrs.getString("CLASS_NAME") +" DT=" + attrs.getString("DATA_TYPE") +" R=" +	attrs.getString("REMARKS")
				//	)
		//	udt.add(tcol)
		}
		

		udt
	}
	
	def exportAllTypes(String fileName){
		
		def out = new File(fileName).newWriter()
		def allTypes = getAllUserDefinedTypes()
		
	allTypes.each { tname->
			println tname
			def typeSql = export(tname)
			out << "------------------ TYPE ${tname}--------------\n"
			//out << table.asSql() +"\n"
			out << typeSql
			out << "\n\n"
		}
	}
		
}

