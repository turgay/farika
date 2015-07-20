package dbexport

class Table {
	def name
	def columnList = []
	def tableComment
	def colComments = [:]

	def Table(name){
		this.name = name
	}
	def add(Column col){
		columnList << col
	}

	def asSql() {
		String cols = columnList.join(", ")
		
		return "CREATE TABLE ${Config.schemaGen}.${this.name} (${cols}) TABLESPACE ${Config.tablespaceGen};"
	}

	def comment(comments){
		this.tableComment = comments
	}

	def colComment(coln, comments) {
		this.colComments[coln] = comments
	}

	def asSqlComments(){
		String s = ""
		if (tableComment != null) {
			def comment = escapeComment(tableComment)
			s += "COMMENT ON TABLE ${Config.schemaGen}.${this.name} IS '${comment}';\n"
		}
		colComments.each { column, columnComment ->
			if (columnComment != null) {
				def comment = escapeComment(columnComment)
				s += "COMMENT ON COLUMN ${Config.schemaGen}.${this.name}.${column} IS '${comment}';\n"
			}
		}
		return s
	}

	def escapeComment(String comment) {
		return comment.replace("'", "''")
	}
}