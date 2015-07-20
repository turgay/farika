package dbexport
import groovy.sql.Sql


//TODO incomplete
public class ForeignKeyExport {

	private Sql sql

	def ForeignKeyExport(Sql sql){
		this.sql = sql
	}

	def export(String tableName) {

		def meta = sql.connection.metaData
		def result = meta.getExportedKeys(Config.schemaRef, Config.schemaRef, tableName)

		def fkeys= [] 
		while (result.next()) {
			ForeignKey fkey =  new ForeignKey(
					cols.getString("FK_NAME"),
					cols.getString("FKTABLE_NAME"),
					cols.getString("FKCOLUMN_NAME")
					)
			println fkey
			fkeys.push(fkey)
		}


		return fkeys
	}
}

