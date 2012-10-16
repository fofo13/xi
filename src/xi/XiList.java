package xi;

import java.util.ArrayList;
import java.util.List;

public class XiList extends DataType {
	
	private List<Integer> list;
	
	public XiList(List<Integer> list) {
		this.list = list;
	}
	
	public static XiList parse(String exp) {
		String[] split = exp.replaceAll("[\\[\\]]", "").split(" ");
		List<Integer> list = new ArrayList<Integer>(split.length);
		for (String s : split)
			list.add(Integer.parseInt(s));
		return new XiList(list);
	}
	
	/*
	 * Map Syntax:
	 * e.g.
	 * @ [1 2 3 4] {** . 2}  -->  [1 4 9 16]
	 */
	public void map(String exp) {
		List<Integer> newList = new ArrayList<Integer>(list.size());
		for (int a : list)
			newList.add((new ExpressionTree(exp.replaceAll("\\.", "" + a)).evaluate()));
		list = newList;
	}
	
	@Override
	public DataType performOperation(UnaryOperation op) {
		return null;
	}
	
	@Override
	public DataType performOperation(BinaryOperation op, DataType data) {
		return null;
	}
	
	@Override
	public String toString() {
		return list.toString().replaceAll(",", "");
	}
	
}