package dbexport

enum ObjectType {
	FUNCTION("FUNCTION", ".fnc", true),
	PACKAGE("PACKAGE", ".pck", true),
	PACKAGE_BODY("PACKAGE BODY",".pck", true),
	PROCEDURE("PROCEDURE", ".prc", true),
	VIEW("VIEW", ".vw", true),
	TRIGGER("TRIGGER", ".trg", true);

	def key
	def extension
	def requiresObjectExport

	private ObjectType(key, extension, requiresObjExport){
		this.key =key
		this.extension = extension
		this.requiresObjectExport = requiresObjExport
	}

	static asType(String key){
		for(ObjectType objType in values()) {
			if (objType.key.equalsIgnoreCase(key)){
				return objType
			}
		}
		throw new IllegalArgumentException("Unknown object type:" + key)
	}
	
	def keyUpperCase(){
		return key.toUpperCase(Locale.ENGLISH)
	}
	
	def keyLowerCase(){
		return key.toLowerCase(Locale.ENGLISH)
	}
}