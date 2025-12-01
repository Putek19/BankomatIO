package Model;

import java.math.BigDecimal;
public interface IModel {

	public String pobierzDaneKarty(int aId);

	public boolean sprawdzPin(int aId, String aPin);

	public BigDecimal sprawdzSaldo(int aId);

	public void aktualizujSaldo(int aId, BigDecimal aKwota);

	public void zablokujKarte(int aId);

	public void zablokujBankomat();

	public void zarejestrujZdarzenie(String aOpis);

	public void usuniecieKlienta(int aNrKlienta);

	public void usuniecieKarty(int aIdKarty);
}