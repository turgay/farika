package dbexport

class ObjectFilter {
	private excludePatternMap =[:]

	ObjectFilter() {
		buildExcludeMap()
	}
	
	def matches(ObjectType objType, String objName) {
		def patterns = excludePatternMap[objType.keyLowerCase()]
		if (patterns == null){
			return false
		}
		return matchAnyPattern(objName, patterns)
	}
	
	def matchAnyPattern(objName, patterns){
		boolean found = false
		for(String pattern: patterns) {
			if (patternMatches(objName, pattern)){
				found = true
				break
			}
		}
		return found
	}

	def patternMatches(objName, pattern) {
		return objName.toLowerCase(Locale.ENGLISH).matches(pattern)
	}

	def patternize(value){
		return value.trim().replaceAll("\\*", "\\(\\.*\\)").toLowerCase(Locale.ENGLISH)
	}

	private buildExcludeMap(){
		Config.excludes.each {key, patternsValue ->
			def patterns = []
			if (patternsValue instanceof Collection){
				for(String patternStr : patternsValue){
					patterns.add(patternize(patternStr))
				}
			} else {
				patterns.add(patternize(patternsValue))
			}
			excludePatternMap[key] = patterns;
		}
		println "Using filters ${excludePatternMap}"
	}
}