package org.xiscript.xi.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.xiscript.xi.compilation.VariableSuite;
import org.xiscript.xi.core.VariableCache;
import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.XiDict;
import org.xiscript.xi.datatypes.XiVar;
import org.xiscript.xi.datatypes.collections.ArgumentList;
import org.xiscript.xi.datatypes.collections.CollectionWrapper;
import org.xiscript.xi.datatypes.collections.XiList;
import org.xiscript.xi.datatypes.collections.XiSet;
import org.xiscript.xi.datatypes.collections.XiTuple;
import org.xiscript.xi.exceptions.ErrorHandler;
import org.xiscript.xi.exceptions.ErrorHandler.ErrorType;
import org.xiscript.xi.nodes.singletons.SepNode;
import org.xiscript.xi.nodes.singletons.ToNode;

public class CollectionNode extends DataNode<CollectionWrapper<?>> {

	private static final long serialVersionUID = 0L;

	public static final int TYPE_LIST = 0;
	public static final int TYPE_TUPLE = 1;
	public static final int TYPE_SET = 2;
	public static final int TYPE_DICT = 3;

	private Node[] elements;
	private int type;
	private boolean literal;
	private ArgumentList argList;

	public CollectionNode(Node[] elements, int type) {
		super(null);
		this.elements = elements;
		this.type = type;
		literal = false;
	}

	@Override
	public void literalize() {
		literal = true;
	}

	private int getSize() {
		int i = 1;
		for (Node node : elements)
			if (node == SepNode.instance())
				i++;
		return i;
	}

	@Override
	public DataType evaluate(VariableCache cache) {
		if (literal) {
			if (argList != null)
				return argList;

			if (elements.length > 1) {
				for (int i = 0; i < elements.length - 1; i += 2)
					if (special(elements[i])
							|| elements[i + 1] != SepNode.instance())
						ErrorHandler
								.invokeError(ErrorType.MALFORMED_ARGUMENT_LIST);

				if (special(elements[elements.length - 1]))
					ErrorHandler.invokeError(ErrorType.MALFORMED_ARGUMENT_LIST);
			}

			XiVar[] array = new XiVar[(elements.length + 1) / 2];

			int i = 0;
			for (Node node : elements) {
				if (node == SepNode.instance())
					continue;
				VarNode vnode = (VarNode) node;
				vnode.literalize();
				array[i] = (XiVar) vnode.evaluate(cache);
				array[i].setPersistent(true);
				i++;
			}

			return argList = new ArgumentList(array);
		}

		switch (type) {
		case TYPE_LIST:
			return createList(cache);
		case TYPE_TUPLE:
			return createTuple(cache);
		case TYPE_SET:
			return createSet(cache);
		case TYPE_DICT:
			return createDict(cache);
		}

		return null;
	}

	private void addElementsTo(Collection<DataType> col, VariableCache scope) {
		for (Node node : elements)
			if (node != SepNode.instance())
				col.add(node.evaluate(scope));
	}

	private XiList createList(VariableCache scope) {
		List<DataType> result = new ArrayList<DataType>(getSize());
		addElementsTo(result, scope);
		return new XiList(result);
	}

	private XiTuple createTuple(VariableCache scope) {
		List<DataType> result = new ArrayList<DataType>(getSize());
		addElementsTo(result, scope);
		return new XiTuple(result);
	}

	private XiSet createSet(VariableCache scope) {
		Set<DataType> result = new HashSet<DataType>(getSize());
		addElementsTo(result, scope);
		return new XiSet(result);
	}

	private static boolean special(Node node) {
		return node == SepNode.instance() || node == ToNode.instance();
	}

	private XiDict createDict(VariableCache cache) {
		Map<DataType, DataType> result = new HashMap<DataType, DataType>(
				getSize());

		int i = 0;
		while (i < elements.length) {
			if (elements[i] == SepNode.instance()) {
				i++;
				continue;
			}

			Node key = elements[i], to = elements[i + 1], value = elements[i + 2];

			if (special(key)
					|| special(value)
					|| to != ToNode.instance()
					|| (i + 3 < elements.length && elements[i + 3] != SepNode
							.instance())) {
				ErrorHandler.invokeError(ErrorType.MALFORMED_DICT);
			}

			result.put(key.evaluate(cache), value.evaluate(cache));
			i += 4;
		}

		return new XiDict(result);
	}

	@Override
	public void emitBytecode(MethodVisitor mv, VariableSuite vs) {
		switch (type) {
		case TYPE_LIST:
			mv.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList");
			mv.visitInsn(Opcodes.DUP);
			mv.visitIntInsn(Opcodes.BIPUSH, getSize());
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList",
					"<init>", "(I)V");

			for (Node node : elements) {
				if (node != SepNode.instance()) {
					node.emitBytecode(mv, vs);
					mv.visitMethodInsn(Opcodes.INVOKEINTERFACE,
							"java/util/List", "add", "(Ljava/lang/Object;)Z");
				}
			}
		}
	}

}