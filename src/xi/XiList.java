package xi;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;

public class XiList extends DataType {
	
	private List<DataType> list;
	
	public XiList(List<DataType> list) {
		this.list = list;
	}
	
	public XiList(int n) {
		this(new ArrayList<DataType>(n));
		for (int i = 0 ; i < n ; i++)
			list.add(new XiNum(i));
	}
	
	public static XiList parse(String exp) {
		String[] split = exp.replaceAll("[\\[\\]]", "").split(" ");
		List<DataType> list = new ArrayList<DataType>(split.length);
		for (String s : split)
			list.add(new XiNum(Integer.parseInt(s)));
		return new XiList(list);
	}
	
	public List<DataType> list() {
		return list;
	}
	
	public DataType get(int index) {
		index %= list.size();
		return list.get(index < 0 ? list.size() + index : index);
	}
	
	/*
	public DataType max() {
		return Collections.max(list);
	}
	
	public int min() {
		return Collections.min(list);
	}
	
	public void shuffle() {
		Collections.shuffle(list);
	}
	*/
	
	/*
	 * Map Syntax:
	 * e.g.
	 * @ [1 2 3 4] {** . 2}  -->  [1 4 9 16]
	 */
	public XiList map(XiBlock block) {
		List<DataType> newList = new ArrayList<DataType>(list.size());
		for (DataType a : list)
			newList.add(block.evaluate(((XiNum)a).val()));
		return new XiList(newList);
	}

	public XiNum sum() {
		int n = 0;
		for (DataType data : list)
			n += ((XiNum)data).val();
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