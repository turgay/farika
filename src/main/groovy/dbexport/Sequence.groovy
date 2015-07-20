package dbexport

class Sequence {
	def name
	
	def Sequence(name){
		this.name = name
	}
	
	def asSql() {
		return "CREATE SEQUENCE ${Config.schemaGen}.${this.name};"
	}
}