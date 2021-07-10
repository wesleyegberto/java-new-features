import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class SwitchWithPatternMatching {
	public static void main(String[] args) {
		System.out.println(stringify(Integer.valueOf(42)));
		System.out.println(stringify("Some text"));
		System.out.println(stringify(new Object()));
		System.out.println(stringify(new int[] { 4, 2 }));
		System.out.println(stringify(null));

		System.out.println("\n\nAssets prices:");
		Stream.of(
				new Stock("BUG", "Me", 100),
				new Bond("BUG-1", "Me", 102, 0.5f, LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1)),
				new Bond("BUG-2", "Me", 102, 0.5f, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 7, 1)),
				new CertificateOfDeposit("CD-1", "Me", 200, 0.4f, LocalDate.of(2021, 1, 1), Term.ONE_YEAR),
				new CertificateOfDeposit("CD-2", "Me", 200, 0.4f, LocalDate.of(2021, 1, 1), Term.SIX_MONTH),
				null
			)
			.forEach(asset -> {
				try {
					long totalPrice = calculateAssetPrice(asset);
					System.out.printf("Asset %s worths %d %n", asset, totalPrice);

				} catch (RuntimeException ex) {
					System.out.printf("Asset %s is invalid: %s%n", asset, ex.getMessage());
				}
			});
	}

	static String stringify(Object value) {
		switch (value) {
			// we can test null here, without check before the switch =)
			case null: return "The value is `null`";
			// test and declare a var to use inside the case
			case String s: return "Is String: " + s;
			case Number n: return "Is a Number: " + n;
			case int[] ar: return "Is an array of number: " + ar;
			default: return "Is untested type =(: " + value.toString();
		}
	}

	static long calculateAssetPrice(Asset asset) {
		switch (asset) {
			case Stock s: return s.calculatePrice();
			// guard
			case Bond b && b.isExpired(): throw new IllegalStateException("The bond is expired");
			case Bond b: return b.calculatePrice();
			case CertificateOfDeposit cd && cd.isExpired(): throw new IllegalStateException("The CD is expired");
			case CertificateOfDeposit cd: return cd.calculatePrice();
			case null:
				throw new IllegalArgumentException("Invalid asset");
			default:
				throw new IllegalArgumentException("Not supported");
		}
	}
}

sealed abstract class Asset permits Bond, Stock, CertificateOfDeposit {
	private String issuer;
	private String holder;
	private long totalAmount;

	protected Asset(String issuer, String holder, long totalAmount) {
		this.issuer = issuer;
		this.holder = holder;
		this.totalAmount = totalAmount;
	}

	public long getTotalAmount() {
		return this.totalAmount;
	}

	public abstract long calculatePrice();
}

final class Bond extends Asset {
	private LocalDate issuedDate;
	private float interestRate;
	private LocalDate maturityDate;

	Bond(String issuer, String holder, int totalAmount, float interestRate, LocalDate issuedDate, LocalDate maturityDate) {
		super(issuer, holder, totalAmount);
		this.interestRate = interestRate;
		this.issuedDate = issuedDate;
		this.maturityDate = maturityDate;
	}

	public boolean isExpired() {
		return maturityDate.isBefore(LocalDate.now());
	}

	public long calculatePrice() {
		var totalOfMonths = YearMonth.of(this.issuedDate.getYear(), this.issuedDate.getMonth())
			.until(YearMonth.of(this.maturityDate.getYear(), this.maturityDate.getMonth()), ChronoUnit.MONTHS)
			+ 1;
		return getTotalAmount() + (int) (totalOfMonths * this.interestRate);
	}
}

final class Stock extends Asset {
	Stock(String issuer, String holder, long totalAmount) {
		super(issuer, holder, totalAmount);
	}

	public long calculatePrice() {
		return getTotalAmount();
	}
}

final class CertificateOfDeposit extends Asset {
	private float interestRate;
	private LocalDate issuedDate;
	private Term term;

	CertificateOfDeposit(String issuer, String holder, int totalAmount, float interestRate, LocalDate issuedDate, Term term) {
		super(issuer, holder, totalAmount);
		this.interestRate = interestRate;
		this.issuedDate = issuedDate;
		this.term = term;
	}

	public LocalDate calculateMaturityDate() {
		switch (term) {
			case SIX_MONTH: return issuedDate.plusMonths(6);
			case ONE_YEAR: return issuedDate.plusYears(1);
			case EIGHTEEN_MONTH: return issuedDate.plusMonths(18);
			case TWO_YEAR: return issuedDate.plusYears(2);
			default: throw new IllegalStateException();
		}
	}

	public boolean isExpired() {
		return this.calculateMaturityDate().isBefore(LocalDate.now());
	}

	public long calculatePrice() {
		var maturityDate = calculateMaturityDate();
		var totalOfMonths = YearMonth.of(this.issuedDate.getYear(), this.issuedDate.getMonth())
			.until(YearMonth.of(maturityDate.getYear(), maturityDate.getMonth()), ChronoUnit.MONTHS)
			+ 1;
		return getTotalAmount() + (int) (totalOfMonths * this.interestRate);
	}
}

enum Term {
	SIX_MONTH,
	ONE_YEAR,
	EIGHTEEN_MONTH,
	TWO_YEAR
}