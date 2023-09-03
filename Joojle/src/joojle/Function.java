package joojle;

import java.lang.reflect.Executable;

public class Function {

	private Executable executable;
	private boolean isConstructor;
	private String signature;
	
	public Function (Executable executable, boolean isConstructor) {
		assert executable != null;
		
		this.executable = executable;
		this.isConstructor = isConstructor;
		this.signature = Searcher.forgeSignature(executable);
	}
	
	public String getSignature () {
		return signature;
	}
	
	public boolean getIsConstructor () {
		return isConstructor;
	}
	
	@Override
	public String toString() {
		return executable.toString() +((isConstructor)? " [CONSTRUCTOR]" : "");
	}
	
}
