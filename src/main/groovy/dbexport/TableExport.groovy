package dbexport
import groovy.sql.Sql



public class TableExport {

	private Sql sql

	def TableExport(Sql sql){
		this.sql = sql
	}

	def getAllTableNames(){
		def tables =  []
		sql.eachRow( 'select table_name from user_tables' ) { 
			tables << it.table_name
			
		}
		tables = tables.sort()
		println tables.size()
		tables
	}
	def export(String tableName) {

		def meta = sql.connection.metaData
		def cols = meta.getColumns(Config.schemaRef, Config.schemaRef, tableName.toUpperCase(Locale.ENGLISH), null)


		Table table = new Table(tableName)
		while (cols.next()) {
			Column tcol =  new Column(
					cols.getString("column_name"),
					cols.getString("type_name"),
					cols.getString("COLUMN_SIZE"),
					cols.getString("decimal_digits"),
					cols.getString("nullable"),
					cols.getString("COLUMN_DEF")
					)
			table.add(tcol)
		}
		
		cols.close()

		sql.eachRow( "select table_name, comments from all_tab_comments where table_name = ? and OWNER = ?", [tableName,Config.schemaRef] ){
			table.comment(it.comments)
		}


		sql.eachRow( "select column_name,comments from user_col_comments where table_name = ?", [tableName] ) {
			table.colComment(it.column_name, it.comments)
		}

		return table
	}
}

