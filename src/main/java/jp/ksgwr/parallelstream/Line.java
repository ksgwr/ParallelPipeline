package jp.ksgwr.parallelstream;

/**
 * 1 Line Data Class
 * @author ksgwr
 *
 */
public class Line implements Comparable<Line>{
	/** line str */
	public String str;

	/** line column number */
	public long num;

	/**
	 * constructor
	 * @param line line str
	 * @param num line column number
	 */
	public Line(String line, long num) {
		this.str = line;
		this.num = num;
	}

	@Override
	public int compareTo(Line o) {
		return num < o.num ? -1 : 1;
	}

	@Override
	public String toString() {
		return num+":"+str;
	}
}
