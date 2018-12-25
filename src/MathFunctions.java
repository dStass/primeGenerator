import java.math.BigInteger;
import java.util.HashMap;

public class MathFunctions {
	public final static int TOTAL_ASCII_DIGITS = 128;
	
	
	
	
	
	public static long raiseNumToExponentModulo(long x, long n, long m) {
		return raiseNumToExponentModulo(x, n, m, false);
		
	}
	
	
	// return x^n (mod m)
	// TODO optimisations, fast ways of raising powers:
	// keep raising until we reach x^k = 1 mod m, then factor exponent out 
	// maybe recursive?
	public static long raiseNumToExponentModulo(long x, long n, long m, boolean optimise) {
		long[] calculations;
		if (optimise) calculations = new long[TOTAL_ASCII_DIGITS];
		else calculations = new long[(int) n]; // optimise: keep arraylist, store what we have to store
		
		int cycles = 0;
		long ans = x;
		long xMod = x % m;
		ans %= m;
		calculations[0] = ans;
    	for (int i = 1; i < n; i++) {
    		cycles++;
    		ans *= xMod; 
    		ans %= m;
    		ans += (ans < 0 ? m : 0);
    		if (optimise) {
    			 if (ans < TOTAL_ASCII_DIGITS) calculations[(int) ans] = cycles;
    		} else calculations[cycles] = ans;
    		if (ans == 1) {
    			if (optimise) {
    				for (int j = 1; j < TOTAL_ASCII_DIGITS; j++) {
    					int storedLocation = (int) (n % (cycles + 1));
        				if ((storedLocation - 1) == calculations[j]) return j; 
    				}
    			} else {
    				int storedLocation = (int) (n % (cycles+1));
        			return calculations[storedLocation-1];
    			}
    		}
    	}
		return ans;
	}
	
	public static long raiseNumToExponentModuloOptimised(long x, long n, long m) {
		if (n == 0) return 1;
		if (n == 1) return (x%m);
		long xMod = x % m;
		long xMod2 = xMod * xMod;
		xMod2 %= m;
		long nHalf = n / 2;
		if (n % 2 == 0) {
			return raiseNumToExponentModuloOptimised(xMod2, nHalf, m);
		} else {
			return ((raiseNumToExponentModuloOptimised(xMod2, (n-1)/2 , m) * xMod) % m);
		}
	}
	
	public static BigInteger raiseNumToExponentModuloBig(BigInteger x, BigInteger n, BigInteger m) {
		BigInteger zero = new BigInteger("0");
		BigInteger one = new BigInteger("1");
		BigInteger two = new BigInteger("2");
		
		if (n.compareTo(zero) == 0) return one;
		if (n.compareTo(one) == 0) return x; // x already modded by m
		BigInteger xMod = x.mod(m);
		BigInteger xMod2 = xMod.multiply(xMod);
		xMod2 = xMod2.mod(m);
		if (n.mod(two).compareTo(zero) == 0) {
			return raiseNumToExponentModuloBig(xMod2, n.divide(two), m);
		} else {
			BigInteger pow = (n.subtract(one)).divide(two);
			return raiseNumToExponentModuloBig(xMod2, pow, m).multiply(xMod).mod(m);
		}
	}
	
	
	
	
	// solution from https://discuss.codechef.com/questions/1440/algorithm-to-find-inverse-modulo-m
	public static BigInteger getInverseModulo(BigInteger a, BigInteger m) { // find inverse to a (modulo m)
		Coordinate c = new Coordinate();
		applyExtendedEuclidean(a,m,c);
		if (c.getX().compareTo(BigInteger.ZERO) == -1) c.setX(c.getX().add(m));
		return c.getX();
		
	}
	
	
	public static void applyExtendedEuclidean(BigInteger a, BigInteger b, Coordinate c) {
		if (a.mod(b).compareTo(BigInteger.ZERO) == 0) {
			c.setX(BigInteger.ZERO);
			c.setY(BigInteger.ONE);
			return;
		}
		applyExtendedEuclidean(b, a.mod(b), c);
		BigInteger temp = c.getX();
		c.setX(c.getY());
		c.setY(temp.subtract(c.getY().multiply((a.divide(b)))));
	}
}
