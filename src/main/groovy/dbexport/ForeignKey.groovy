package dbexport

class ForeignKey {
	def fkName
	def fkTable
	def fkCols

	def ForeignKey(fkName, fkTable, fkCols) {
		this.fkName = fkName
		this.fkTable = fkTable
		this.fkCols = fkCols
	}

	public String toString() {
		str()
	}
	def str() {
		return "${fkName} ${fkTable} ${fkCols}"
	}
}