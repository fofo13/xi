package org.xiscript.xi.datatypes.numeric;

import org.xiscript.xi.datatypes.DataType;
import org.xiscript.xi.datatypes.functional.HiddenFunc;
import org.xiscript.xi.datatypes.functional.XiFunc;

public class XiMath {

	private XiMath() {
	}

	public static final XiFunc sin = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.sin(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc cos = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.cos(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc tan = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.tan(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc cot = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(1 / Math.tan(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc sec = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(1 / Math.cos(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc csc = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(1 / Math.sin(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc asin = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.asin(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc acos = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.acos(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc atan = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.atan(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc acot = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.PI / 2
					- Math.atan(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc asec = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.acos(1 / ((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc acsc = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.asin(1 / ((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc sinh = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.sinh(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc cosh = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.cosh(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc tanh = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.tanh(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc coth = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(1 / Math.tanh(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc sech = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(1 / Math.cosh(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc csch = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(1 / Math.sinh(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc log = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.log(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc log10 = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.log(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc floor = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.floor(((XiFloat) args[0]).num()));
		}
	};

	public static final XiFunc ceil = new HiddenFunc(1) {
		private static final long serialVersionUID = 0L;

		@Override
		public DataType evaluate(DataType... args) {
			return new XiFloat(Math.ceil(((XiFloat) args[0]).num()));
		}
	};

}