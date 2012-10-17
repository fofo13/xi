package xi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XiList extends DataType {
	
	private List<Integer> list;
	
	public XiList(List<Integer> list) {
		this.list = list;
	}
	
	public XiList(int n) {
		this(new ArrayList<Integer>(n));
		for (int i = 0 ; i < n ; i++)
			list.add(i);
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
	public XiList map(XiBlock block) {
		List<Integer> newList = new ArrayList<Integer>(list.size());
		for (int a : list)
			newList.add(((XiNum)block.evaluate(a)).val());
		return new XiList(newList);
	}

	public XiNum sum() {
		int n = 0;
		for (int i : list)
			n += i;
		return new XiNum(n);
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