package compare

class DirectoryComparator {
	public static void  main(String[] args){
		DirectoryComparator dc = new DirectoryComparator()
		
		String refPath = "D:\\work\\hukuk_workspace\\hukuk\\hopedb\\src\\main\\resources\\dbscripts\\repeatable\\"
		
		String updatedPath = "D:\\dev\\workspaces\\ggts-workspace\\groovy-utils\\out\\merkur\\"

		def diff = dc.compare(refPath, updatedPath);
		
		diff.print()
	}

	class Diff {
		def added =[] as Set
		def updated = [] as Set
		def deleted = [] as Set

		def merge(Diff diff){
			added.addAll(diff.added)
			updated.addAll(diff.updated)
			deleted.addAll(diff.deleted)
		}
		
		def print() {
			printList("ADDED", added)
			printList("REMOVED", deleted)
			printList("UPDATED", updated)
			
		}
		
		def printList(name, list){
			println "-------------${name}--------------"
			println "${list.size()} file(s)"
			list.each{ file -> println file.absolutePath }
		
		}
	}
	
	
	def compare( String refPath, String updatedPath) {
		println updatedPath
		println refPath

		return compareDirs(refPath, updatedPath)
		
	}

	def compareDirs(refPath, updatedPath) {

		Diff diff = new Diff()
		def refDir = new File(refPath)
		def updatedDir = new File(updatedPath)
		
		updatedDir.listFiles().each {
			def updatedFilePath = it.absolutePath
			if( !updatedFilePath.matches('.+\\.svn.+') ) {

				def refCompare = new File(refDir.absolutePath + java.io.File.separator + it.name)
				if( it.isDirectory() ) {
					def dirResults = compareDirs(refCompare.absolutePath, updatedFilePath)
					diff.merge(dirResults)
				} else {

					if( !refCompare.exists()) {
						diff.added << it
					} else if(hasDiffLineByLine(it, refCompare)) {
						diff.updated << it
					}
				}
			}
		}
		refDir.listFiles().each {
			def refFilePath = it.absolutePath
			if( !refFilePath.matches('.+\\.svn.+') ) {

				def updatedCompare = new File(updatedDir.absolutePath + java.io.File.separator + it.name)
				if( it.isDirectory() ) {
					def dirResults = compareDirs(refFilePath, updatedCompare.absolutePath)
					diff.merge(dirResults)
				} else {

					if( !updatedCompare.exists()) {
						diff.deleted << it
					}
				}
			}
		}
		
		return diff
	}
	
	def hasDiff(File file1, File file2){
		def baseFile = file1.getText('UTF-8').trim()
		def cmpFile = file2.getText('UTF-8').trim()
		if (! baseFile.equalsIgnoreCase(cmpFile)) {
			println "Files dont match : ${file1.absolutePath} vs ${file2.absolutePath}"
			return true
		}
		return false
	}
	
	def hasDiffLineByLine(File file1, File file2){
		def baseLines = fileAsLineList(file1)
		def cmpLines = fileAsLineList(file2)
		if (baseLines.size() != cmpLines.size()){
			//println "Size doesnt match : ${file1.absolutePath} (${baseLines.size()} lines) vs ${file2.absolutePath} (${cmpLines.size()} lines)"
			return hasDiff(file1, file2)
		}
		for (int i=0;i<baseLines.size(); i++){
			if (!baseLines[i].equals(baseLines[i])){
				println "Line doesnt match at  ${file1.absolutePath} and ${file2.absolutePath} "
				return true
			}
		}
		return false
	}
	
	def fileAsLineList(File file){
		def lines = []
		file.eachLine { line ->
			lines << line
		}
		lines
	}
	
	
}
