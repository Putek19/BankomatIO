package Model;
import java.math.BigDecimal;

public interface IKarta {

	public int dajId();

	public boolean sprawdzPin(String aPin);

	public BigDecimal pobierzSaldo();

	public void zmienSaldo(BigDecimal aKwota);

	public boolean czyZablokowana();
}