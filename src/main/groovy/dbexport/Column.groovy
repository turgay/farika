package dbexport

class Column {
	def colName
	def colType
	def colSize
	def decDigits
	def nullable
	def colDef

	def Column(colName, colType, colSize, decDigits, nullable, colDef) {
		this.colName = colName
		this.colType = colType
		this.colSize = colSize
		this.decDigits = decDigits
		this.nullable = nullable
		this.colDef = colDef
	}

	public String toString() {
		//return "${colName} ${colType} ${colSize} ${decDigits} ${nullable} ${colDef}"
		str()
	}
	def str() {
		String s = "${colName} ${colType}"
		if (colSize != null && colSize.toInteger() > 0 && !colType.startsWith("TIMESTAMP")
		&& !colType.startsWith("DATE") && !colType.startsWith("CLOB") && !colType.startsWith("BLOB") && !colType.startsWith("XMLTYPE")) {
			String prec = (decDigits == null?"0":decDigits)

			if (colType.startsWith("FLOAT") || colType.startsWith("VARCHAR") || colType.startsWith("NVARCHAR")|| colType.startsWith("CHAR")){
				s += "(${colSize})"
			} else {
				s += "(${colSize}, ${prec})"
			}
		}
		if (colDef != null ){
			String defaultValue = colDef.trim()
			if (defaultValue.startsWith("(")){
				defaultValue = defaultValue.replace("(", "").replace(")", "")
			}

			s += " DEFAULT "+ defaultValue
		}
		if (nullable == "0"){
			s += " NOT NULL"
		}
		return s
	}
}