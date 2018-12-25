import java.math.BigInteger;
import java.util.Random;

public class Application {
	static BigInteger ONE = BigInteger.ONE;
	static BigInteger TWO = new BigInteger("2");

	
	public static void main(String[] args) {
		int digits = Integer.parseInt(args[0]); // first argument = number of digits of prime
		digits = 309; // TODO REMOVE
		BigInteger generatedNumber = generateRandomOddOfLength(digits);
		while (!isProbablePrime(generatedNumber)) { // while we don't have a prime, add 2 to generated number
			generatedNumber = generatedNumber.add(TWO);
			if (generatedNumber.toString().length() > digits) generatedNumber = generateRandomOddOfLength(digits);
		}
		
		System.out.println(generatedNumber.toString() + " is probably prime");
		
		

	}
	
	private static BigInteger generateRandomOddOfLength(int digits) {
		BigInteger toReturn;
		String toReturnStr = "";
		for (int i = 0; i < digits; i++) {
			int randomNum = (int) (Math.random()*10);
			if (i == 0) {
				while (randomNum == 0) { // make sure first number is not 0
					randomNum = (int) (Math.random()*10);
				}
			}
			if (i == digits - 1) { // on last number, generate such that it is odd
				if (randomNum % 2 == 0) randomNum++;
			}
			
			toReturnStr += (char) ('0'+randomNum);
		}
		toReturn = new BigInteger(toReturnStr);
		return toReturn;
	}
	
	// return whether a number is a probable prime based on miller-rabin test
	private static boolean isProbablePrime(BigInteger n) {
		int kTests = 100;
		for (int i = 0; i < kTests; i++) {
			if (applyMillerRabin(n) == false) return false;
		}
		return true;
	}
	
	
	
	
	// inspired: https://www.youtube.com/watch?v=qdylJqXCDGs
	private static boolean applyMillerRabin(BigInteger n) {
		int lastDigit = getLastDigit(n);
		if (lastDigit % 2 == 0 && lastDigit != 2) { // return false if n even
			return false;
		}
		
		// Step 1: find m such that n-1 = m*2^k
		BigInteger m = new BigInteger(n.toString());
		m = m.subtract(ONE);
		while (true) {
			if (getLastDigit(m) % 2 == 0) { // divisible by 2
				m = m.divide(TWO);
			} else break;
		}
		//System.out.println("n=" + n.toString() + ", m=" + m.toString());
		
		
		// Step 2: pick a s.t.: 1 < a < n-1
		BigInteger a;
		do {
			a = new BigInteger(n.bitLength(), new Random());
		} while (a.compareTo(n) >= 0);
	//	System.out.println("a="+a.toString());
		
		
		// Step 3: Compute b_0 = a^m (mod n), b_i = (b_(i-1))^2
		int iMAX = 150;
		BigInteger b_i = null;
		BigInteger N_MINUS_ONE = n.subtract(BigInteger.ONE);
		for (int i = 0; i < iMAX; i++) {
			if (i == 0) {
				b_i = MathFunctions.raiseNumToExponentModuloBig(a, m, n);
				if (b_i.compareTo(ONE) == 0 || b_i.compareTo(N_MINUS_ONE) == 0) return true; // is probably prime
			} else { // for i = 1, 2,..
				if (b_i.compareTo(ONE) == 0) return false; // composite for sure
				if (b_i.compareTo(N_MINUS_ONE) == 0) return true; // probably prime
			}
			
			// b_i = (b_(i-1))^2
			b_i = MathFunctions.raiseNumToExponentModuloBig(b_i, TWO, n);
		}

		return false;
	}
	
	
	// returns last digit of a BigInteger
	private static int getLastDigit(BigInteger n) {
		String numberStr = n.toString();
		char lastDigitChar = numberStr.charAt(numberStr.length()-1);
		int lastDigit = Integer.parseInt(new String() + lastDigitChar);
		return lastDigit;
	}
	

}
