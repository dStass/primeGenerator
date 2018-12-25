import java.math.BigInteger;

public class Coordinate {
	private BigInteger x, y;
	public Coordinate() {
		x = BigInteger.ZERO;
		y = BigInteger.ZERO;
	}
	
	public void setX(BigInteger n) {
		x = new BigInteger(n.toString());
	}
	public void setY(BigInteger n) {
		y = new BigInteger(n.toString());
	}
	public BigInteger getX() {
		return x.multiply(BigInteger.ONE);
	}
	public BigInteger getY() {
		return y.multiply(BigInteger.ONE);
	}
}
