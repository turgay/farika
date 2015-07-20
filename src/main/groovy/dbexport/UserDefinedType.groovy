package dbexport

class UserDefinedType {
	def name
	def attrList = []

	def UserDefinedType(name){
		this.name = name
	}
	def add(Column col){
		attrList << col
	}

	def asSql() {
		String cols = attrList.join(", ")
		String s = "CREATE TYPE ${Config.schemaGen}.${this.name} (${cols}) TABLESPACE ${Config.tablespaceGen};"
	}
}