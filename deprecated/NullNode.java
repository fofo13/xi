package xi.nodes;

import xi.datatypes.XiNull;

public class NullNode extends DataNode {

	private static final XiNull xinull = new XiNull();
	
	public NullNode() {
		data = xinull;
	}
	
}