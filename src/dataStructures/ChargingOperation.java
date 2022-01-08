package dataStructures;

import java.util.Comparator;

public class ChargingOperation {


	private final double mStart;
	private final double mEnd;
	private final Node mNode;

	public ChargingOperation(Node node, double start, double end) {
		super();
		this.mStart = start;
		this.mEnd = end;
		this.mNode = node;
	}

	public double getStart() {
		return mStart;
	}

	public double getEnd() {
		return mEnd;
	}

	public Node getNode() {
		return mNode;
	}

	@Override
	public String toString() {
		return "[" + mStart + " -> " + mEnd + "](" + mNode.getID() + ")";
	}

	public boolean startsBefore(ChargingOperation o) {
		return this.getStart() <= o.getStart();
	}

	public double getDuration() {
		return mEnd - mStart;
	}

	public static Comparator<ChargingOperation> ChargingOperationComparator = new Comparator<ChargingOperation>() {

		@Override
		public int compare(ChargingOperation o1, ChargingOperation o2) {
			int s = (int) Math.signum(o1.getStart() - o2.getStart());
			if (s == 0) {
				return (int) Math.signum(o1.getEnd() - o2.getEnd());
			} else {
				return s;
			}
		}

	};

}
