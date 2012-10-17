package xi;

import java.util.ArrayList;
import java.util.Collections;
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
	
	public List<Integer> list() {
		return list;
	}
	
	public int get(int index) {
		return list.get(index < 0 ? list.size() + index : index);
	}
	
	public int max() {
		return Collections.max(list);
	}
	
	public int min() {
		return Collections.min(list);
	}
	
	public void shuffle() {
		Collections.shuffle(list);
	}
	
	/*
	 * Map Syntax:
	 * e.g.
	 * @ [1 2 3 4] {** . 2}  -->  [1 4 9 16]
	 */
	public void map(String exp) {
		exp = exp.replaceAll("[\\{\\}]", "");
		List<Integer> newList = new ArrayList<Integer>(list.size());
		for (int a : list)
			newList.add(((XiNum)(new ExpressionTree(exp.replaceAll("\\.", "" + a)).evaluate())).val());
		list = newList;
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	@Override
	public String toString() {
		return list.toString().replaceAll(",", "");
	}
	
	@Override
	public boolean equals(Object o) {
		return list.equals(((XiList)o).list());
	}
	
}