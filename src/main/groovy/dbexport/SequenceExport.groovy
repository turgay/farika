package dbexport
import groovy.sql.Sql



public class SequenceExport {

	private Sql sql

	def SequenceExport(Sql sql){
		this.sql = sql
	}

	def getAllSeqeunces(){
		def seqs =  []
		sql.eachRow( 'select sequence_name from all_sequences where sequence_owner =?', [Config.schemaRef]) {
			seqs.push(new Sequence(it.sequence_name))
		}
		seqs = seqs.sort()
		println seqs.size() +" sequences found!"
		seqs
	}

	def exportAll(String fileName){

		def out = new File(fileName).newWriter()
		def seqs = getAllSeqeunces()

		seqs.each { seq->
			println seq.name
			out << seq.asSql()
			out << "\n\n"
		}
		out.close()
	}
}

